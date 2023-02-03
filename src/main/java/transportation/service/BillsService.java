package transportation.service;

import bill.BillPageResponse;
import bill.BillPostDto;
import bill.BillPutDto;
import bill.BillResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import transportation.exception.EntityNotFoundException;
import transportation.mapper.BillsMapper;
import transportation.model.Bills;
import transportation.repository.BillsRepository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BillsService {
    private final BillsRepository billsRepository;
    private final BillsMapper billsMapper;

    public BillResponseDto save(BillPostDto billPostDto) {
        Bills bills = Bills.builder()
                .externalId(UUID.randomUUID())
                .userId(billPostDto.getUserId())
                .amount(billPostDto.getAmount())
                .paymentDate(LocalDate.parse(billPostDto.getPaymentDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .wasPaid(false)
                .creationDate(ZonedDateTime.now())
                .lastUpdateDate(ZonedDateTime.now())
                .build();
        return billsMapper.toResponseDto(billsRepository.save(bills));
    }

    public String deleteById(UUID externalId) {
        billsRepository.delete(externalId);
        return "Платеж удален";
    }

    public BillResponseDto update(UUID externalId, BillPutDto billPutDto) {
        checkExistenceOrThrow(externalId);
        billsRepository.update(externalId, billPutDto.getAmount(), LocalDate.parse(billPutDto.getPaymentDate()), billPutDto.getWasPaid());
        return billsMapper.toResponseDto(billsRepository.findByExternalId(externalId).get());
    }

    public BillResponseDto reestablish(UUID externalId) {
        billsRepository.reestablish(externalId);
        Optional<Bills> bills = billsRepository.findByExternalIdAndDeleteDateIsNull(externalId);
        if (bills.isPresent()) {
            return billsMapper.toResponseDto(bills.get());
        }
        else throw new EntityNotFoundException("Bills for user with id = " + externalId + " not found");
    }

    public BillPageResponse getAll(Integer pageNumber, Integer pageSize, String sortBy, Sort.Direction direction, Boolean showPaidBills, UUID userId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, direction, sortBy);
        if (!showPaidBills && userId == null) {
            return billsMapper.toPageResponseDto(billsRepository.getAllByDeleteDateIsNullAndWasPaidFalse(pageable), sortBy, direction);
        }
        else if(showPaidBills && userId != null){
            return billsMapper.toPageResponseDto(billsRepository.getAllByDeleteDateIsNullAndUserId(pageable, userId), sortBy, direction);
        }
        else if(!showPaidBills && userId != null){
            return billsMapper.toPageResponseDto(billsRepository.getAllByDeleteDateIsNullAndWasPaidFalseAndUserId(pageable, userId), sortBy, direction);
        }
        else return billsMapper.toPageResponseDto(billsRepository.getAllByDeleteDateIsNull(pageable), sortBy, direction);
    }

    public BillResponseDto getById(UUID externalId) {
        Optional<Bills> bills = billsRepository.findByExternalIdAndDeleteDateIsNull(externalId);
        if (bills.isPresent()) {
            return billsMapper.toResponseDto(bills.get());
        }
        else throw new EntityNotFoundException("Bills for user with id = " + externalId + " not found");
    }

    public String delete() {
        billsRepository.deleteAll();
        return "Платежи удалены";
    }

    private void checkExistenceOrThrow(UUID externalId){
        Optional<Bills> bills = billsRepository.findByExternalIdAndDeleteDateIsNull(externalId);
        if (bills.isEmpty()) {
            throw new EntityNotFoundException("Bills for user with id = " + externalId + " not found");
        }
    }
}
