package transportation;

import bill.BillPageResponse;
import bill.BillPostDto;
import bill.BillPutDto;
import bill.BillResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import transportation.model.Bills;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public abstract class AbstractInitialization {

    public static Bills bills;
    protected static UUID billExternalId = UUID.randomUUID();
    protected static UUID userId = UUID.randomUUID();
    protected static ZonedDateTime creationDate = ZonedDateTime.now();
    protected static ZonedDateTime lastUpdateDate = creationDate;
    protected static BillResponseDto billResponseDto;
    protected static Page<Bills> page;
    protected static BillPostDto billPostDto;
    protected static BillPutDto billPutDto;
    protected static BillPageResponse billPageResponse;

    @BeforeAll
    public static void init() {
        bills = new Bills();
        bills.setId(1L);
        bills.setExternalId(billExternalId);
        bills.setUserId(userId);
        bills.setCreationDate(creationDate);
        bills.setLastUpdateDate(lastUpdateDate);
        bills.setAmount(300000.0);
        bills.setPaymentDate(LocalDate.parse("2025-01-01"));
        bills.setWasPaid(false);

        billResponseDto = new BillResponseDto();
        billResponseDto.setUserId(bills.getUserId());
        billResponseDto.setAmount(bills.getAmount());
        billResponseDto.setPaymentDate(bills.getPaymentDate().toString());
        billResponseDto.setWasPaid(bills.getWasPaid());
        billResponseDto.setCreationDate(bills.getCreationDate());
        billResponseDto.setLastUpdateDate(bills.getLastUpdateDate());

        billPostDto = new BillPostDto();
        billPostDto.setUserId(bills.getUserId());
        billPostDto.setAmount(bills.getAmount());
        billPostDto.setPaymentDate(bills.getPaymentDate().toString());

        billPutDto = new BillPutDto();
        billPutDto.setAmount(bills.getAmount());
        billPutDto.setPaymentDate(bills.getPaymentDate().toString());
        billPutDto.setWasPaid(bills.getWasPaid());

        billPageResponse = new BillPageResponse();
        billPageResponse.setBills(List.of(billResponseDto));
        billPageResponse.setAmountPages(1);
        billPageResponse.setPageNumber(1);
        billPageResponse.setPageSize(1);
        billPageResponse.setSortBy("sort");
        billPageResponse.setDirection(Sort.Direction.ASC.name());


        page = new PageImpl<>(List.of(bills),
                PageRequest.of(1, 1, Sort.Direction.ASC, "sort"), 1);
    }
}
