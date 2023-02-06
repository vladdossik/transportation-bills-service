package transportation.mapper;

import bill.BillPageResponse;
import bill.BillResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;
import transportation.AbstractInitialization;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BillMapperTest extends AbstractInitialization {

    @InjectMocks
    private BillsMapper billsMapper;

    @Test
    void billToResponseDtoTest() {
        BillResponseDto response = billsMapper.toResponseDto(bills);

        assertEquals(response.getUserId(), bills.getUserId());
        assertEquals(response.getAmount(), bills.getAmount());
        assertEquals(LocalDate.parse(response.getPaymentDate()), bills.getPaymentDate());
        assertEquals(response.getWasPaid(), bills.getWasPaid());
        assertEquals(response.getCreationDate(), bills.getCreationDate());
        assertEquals(response.getLastUpdateDate(), bills.getLastUpdateDate());
    }

    @Test
    void billPageToPageResponseTest() {
        BillPageResponse response = billsMapper.toPageResponseDto(page, "", Sort.Direction.ASC);

        assertEquals(response.getPageNumber(), page.getNumber());
        assertEquals(response.getPageSize(), page.getSize());
        assertEquals(response.getAmountPages(), page.getTotalPages());

        assertNotNull(response.getBills());
        assertEquals(1, response.getBills().size());
        assertEquals(response.getBills().get(0).getUserId(), bills.getUserId());
        assertEquals(response.getBills().get(0).getAmount(), bills.getAmount());
        assertEquals(LocalDate.parse(response.getBills().get(0).getPaymentDate()), bills.getPaymentDate());
        assertEquals(response.getBills().get(0).getWasPaid(), bills.getWasPaid());
        assertEquals(response.getBills().get(0).getCreationDate(), bills.getCreationDate());
        assertEquals(response.getBills().get(0).getLastUpdateDate(), bills.getLastUpdateDate());
    }
}
