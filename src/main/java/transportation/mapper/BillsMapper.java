package transportation.mapper;

import bill.BillPageResponse;
import bill.BillResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import transportation.model.Bills;
import java.util.stream.Collectors;

@Component
public class BillsMapper {
    public BillResponseDto toResponseDto(Bills bills) {
        BillResponseDto billsResponseDto = new BillResponseDto();
        billsResponseDto.setUserId(bills.getUserId());
        billsResponseDto.setAmount(bills.getAmount());
        billsResponseDto.setPaymentDate(bills.getPaymentDate().toString());
        billsResponseDto.setWasPaid(bills.getWasPaid());
        billsResponseDto.setCreationDate(bills.getCreationDate());
        billsResponseDto.setLastUpdateDate(bills.getLastUpdateDate());
        return billsResponseDto;
    }

    public BillPageResponse toPageResponseDto(Page<Bills> page, String sortBy, Sort.Direction direction) {
        BillPageResponse pageResponse = new BillPageResponse();
        pageResponse.setBills(page.getContent().stream().map(this::toResponseDto).collect(Collectors.toList()));
        pageResponse.setAmountPages(page.getTotalPages());
        pageResponse.setPageNumber(page.getNumber());
        pageResponse.setPageSize(page.getSize());
        pageResponse.setSortBy(sortBy);
        pageResponse.setDirection(direction.name());
        return pageResponse;
    }
}
