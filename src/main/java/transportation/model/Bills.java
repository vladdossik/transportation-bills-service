package transportation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table (name = "bills")
public class Bills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID userId;
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID externalId;
    private ZonedDateTime creationDate;
    private ZonedDateTime lastUpdateDate;
    @NotNull(message = "amount is mandatory")
    @Digits(integer = 20, fraction = 20, message = "amount entered incorrectly")
    private Double amount;
    @FutureOrPresent(message = "issueDate entered incorrectly")
    private LocalDate paymentDate;
    private Boolean wasPaid;
    private ZonedDateTime deleteDate;
}
