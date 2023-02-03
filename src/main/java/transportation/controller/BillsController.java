package transportation.controller;

import bill.BillPageResponse;
import bill.BillPostDto;
import bill.BillPutDto;
import bill.BillResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import transportation.service.BillsService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("v1/bills")
@RequiredArgsConstructor
public class BillsController {
    private final BillsService billsService;

    @Operation(summary = "Добавить платеж")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = BillResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @PostMapping("add")
    public BillResponseDto add(@Valid @RequestBody BillPostDto billPostDto) {
        return billsService.save(billPostDto);
    }

    @Operation(summary = "Удалить платеж по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @DeleteMapping("{externalId}/delete")
    public String delete(@PathVariable UUID externalId) {
        return billsService.deleteById(externalId);
    }

    @Operation(summary = "Обновить данные о платеже")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = BillResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @PutMapping("{externalId}")
    public BillResponseDto update(@PathVariable UUID externalId, @Valid @RequestBody BillPutDto billPutDto) {
        return billsService.update(externalId, billPutDto);
    }

    @Operation(summary = "Востановить платеж по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = BillResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @PostMapping("{externalId}")
    public BillResponseDto reestablish(@PathVariable UUID externalId) {
        return billsService.reestablish(externalId);
    }

    @Operation(summary = "Получить список платежей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = BillPageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @GetMapping("all")
    public BillPageResponse getAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(defaultValue = "10") @Min(0) Integer sizeNumber,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam Sort.Direction direction,
            @RequestParam Boolean showPaidBills,
            @RequestParam(required = false) UUID userId) {
        return billsService.getAll(pageNumber, sizeNumber, sortBy, direction, showPaidBills, userId);
    }

    @Operation(summary = "Получить платеж по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = BillResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @GetMapping("{externalId}")
    public BillResponseDto getById(@PathVariable UUID externalId) {
        return billsService.getById(externalId);
    }

    @Operation(summary = "Удалить все платежи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @DeleteMapping("delete")
    public String deleteAll() {
        return billsService.delete();
    }
}
