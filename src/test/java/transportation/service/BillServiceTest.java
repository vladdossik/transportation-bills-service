package transportation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import transportation.AbstractInitialization;
import transportation.exception.EntityNotFoundException;
import transportation.mapper.BillsMapper;
import transportation.model.Bills;
import transportation.repository.BillsRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BillServiceTest extends AbstractInitialization {

    @Mock
    private BillsRepository billsRepository;

    @Mock
    private BillsMapper billsMapper;

    @InjectMocks
    private BillsService billsService;

    @Test
    void saveBillTest() {
        Mockito.when(billsRepository.save(Mockito.any(Bills.class)))
                .thenReturn(bills);
        Mockito.when(billsMapper.toResponseDto(Mockito.any(Bills.class)))
                .thenReturn(billResponseDto);

        billsService.save(billPostDto);

        Mockito.verify(billsRepository, Mockito.times(1))
                .save(Mockito.any(Bills.class));
        Mockito.verify(billsMapper, Mockito.times(1))
                .toResponseDto(Mockito.any(Bills.class));
    }

    @Test
    void deleteBillByIdTest() {
        Mockito.doNothing().when(billsRepository).delete(Mockito.any(UUID.class));
        assertEquals(billsService.deleteById(billExternalId), "Платеж удален");
    }

    @Test
    void deleteBillsTest() {
        Mockito.doNothing().when(billsRepository).delete();
        assertEquals(billsService.delete(), "Платежи удалены");
    }

    @Test
    void updateBillTest() {
        Mockito.when(billsRepository.findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(bills));
        Mockito.doNothing().when(billsRepository).update(
                Mockito.any(UUID.class),
                Mockito.anyDouble(),
                Mockito.any(LocalDate.class),
                Mockito.anyBoolean());
        Mockito.when(billsRepository.findByExternalId(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(bills));
        Mockito.when(billsMapper.toResponseDto(Mockito.any(Bills.class)))
                .thenReturn(billResponseDto);

        billsService.update(billExternalId, billPutDto);

        Mockito.verify(billsRepository, Mockito.times(1))
                .findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class));
        Mockito.verify(billsRepository, Mockito.times(1))
                .findByExternalId(Mockito.any(UUID.class));
        Mockito.verify(billsMapper, Mockito.times(1))
                .toResponseDto(Mockito.any(Bills.class));
    }

    @Test
    void updateBillNotFoundTest() {
        Mockito.when(billsRepository.findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> billsService.update(billExternalId, billPutDto))
                .isInstanceOf(EntityNotFoundException.class);

        Mockito.verify(billsRepository, Mockito.times(1))
                .findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class));
    }

    @Test
    void reestablishBillTest() {
        Mockito.doNothing().when(billsRepository).reestablish(Mockito.any(UUID.class));
        Mockito.when(billsRepository.findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(bills));
        Mockito.when(billsMapper.toResponseDto(bills))
                .thenReturn(billResponseDto);

        billsService.reestablish(billExternalId);

        Mockito.verify(billsRepository, Mockito.times(1))
                .findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class));
        Mockito.verify(billsMapper, Mockito.times(1))
                .toResponseDto(Mockito.any(Bills.class));
    }

    @Test
    void reestablishBillNotFoundTest() {
        Mockito.when(billsRepository.findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> billsService.reestablish(billExternalId))
                .isInstanceOf(EntityNotFoundException.class);

        Mockito.verify(billsRepository, Mockito.times(1))
                .findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class));
    }

    @Test
    void getAllBillsWithShowPaidBillsAndUserIdTest() {
        Mockito.when(billsRepository.getAllByDeleteDateIsNullAndWasPaidFalseAndUserId(
                Mockito.any(Pageable.class), Mockito.any(UUID.class)))
                .thenReturn(page);
        Mockito.when(billsMapper.toPageResponseDto(Mockito.any(Page.class), Mockito.anyString(), Mockito.any(Sort.Direction.class)))
                .thenReturn(billPageResponse);

        billsService.getAll(page.getNumber(), page.getSize(), billPageResponse.getSortBy(), Sort.Direction.ASC, bills.getWasPaid(), billExternalId);

        Mockito.verify(billsRepository, Mockito.times(1))
                .getAllByDeleteDateIsNullAndWasPaidFalseAndUserId(Mockito.any(Pageable.class), Mockito.any(UUID.class));
        Mockito.verify(billsMapper, Mockito.times(1))
                .toPageResponseDto(Mockito.any(Page.class), Mockito.anyString(), Mockito.any(Sort.Direction.class));
    }

    @Test
    void getAllBIllsWithoutShowPaidBillsTest() {
        Mockito.when(billsRepository.getAllByDeleteDateIsNullAndUserId(
                        Mockito.any(Pageable.class), Mockito.any(UUID.class)))
                .thenReturn(page);
        Mockito.when(billsMapper.toPageResponseDto(Mockito.any(Page.class), Mockito.anyString(), Mockito.any(Sort.Direction.class)))
                .thenReturn(billPageResponse);

        billsService.getAll(page.getNumber(), page.getSize(), billPageResponse.getSortBy(), Sort.Direction.ASC, true, billExternalId);

        Mockito.verify(billsRepository, Mockito.times(1))
                .getAllByDeleteDateIsNullAndUserId(Mockito.any(Pageable.class), Mockito.any(UUID.class));
        Mockito.verify(billsMapper, Mockito.times(1))
                .toPageResponseDto(Mockito.any(Page.class), Mockito.anyString(), Mockito.any(Sort.Direction.class));
    }

    @Test
    void getAllBillsWithoutUserIdTest() {
        Mockito.when(billsRepository.getAllByDeleteDateIsNullAndWasPaidFalse(Mockito.any(Pageable.class)))
                .thenReturn(page);
        Mockito.when(billsMapper.toPageResponseDto(Mockito.any(Page.class), Mockito.anyString(), Mockito.any(Sort.Direction.class)))
                .thenReturn(billPageResponse);

        billsService.getAll(page.getNumber(), page.getSize(), billPageResponse.getSortBy(), Sort.Direction.ASC, bills.getWasPaid(), null);

        Mockito.verify(billsRepository, Mockito.times(1))
                .getAllByDeleteDateIsNullAndWasPaidFalse(Mockito.any(Pageable.class));
        Mockito.verify(billsMapper, Mockito.times(1))
                .toPageResponseDto(Mockito.any(Page.class), Mockito.anyString(), Mockito.any(Sort.Direction.class));
    }

    @Test
    void getAllBillsWithoutShowPaidBillsAndUserIdTest() {
        Mockito.when(billsRepository.getAllByDeleteDateIsNull(Mockito.any(Pageable.class)))
                .thenReturn(page);
        Mockito.when(billsMapper.toPageResponseDto(Mockito.any(Page.class), Mockito.anyString(), Mockito.any(Sort.Direction.class)))
                .thenReturn(billPageResponse);

        billsService.getAll(page.getNumber(), page.getSize(), billPageResponse.getSortBy(), Sort.Direction.ASC, true, null);

        Mockito.verify(billsRepository, Mockito.times(1))
                .getAllByDeleteDateIsNull(Mockito.any(Pageable.class));
        Mockito.verify(billsMapper, Mockito.times(1))
                .toPageResponseDto(Mockito.any(Page.class), Mockito.anyString(), Mockito.any(Sort.Direction.class));
    }

    @Test
    void getBillByIdTest() {
        Mockito.when(billsRepository.findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(bills));
        Mockito.when(billsMapper.toResponseDto(bills))
                .thenReturn(billResponseDto);

        billsService.getById(billExternalId);

        Mockito.verify(billsRepository, Mockito.times(1))
                .findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class));
        Mockito.verify(billsMapper, Mockito.times(1))
                .toResponseDto(Mockito.any(Bills.class));
    }

    @Test
    void getBillByIdNotFoundTest() {
        Mockito.when(billsRepository.findByExternalIdAndDeleteDateIsNull(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> billsService.getById(Mockito.any(UUID.class)))
                .isInstanceOf(EntityNotFoundException.class);

        Mockito.verify(billsMapper, Mockito.times(0))
                .toResponseDto(Mockito.any(Bills.class));
    }
}
