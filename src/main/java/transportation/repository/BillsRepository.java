package transportation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import transportation.model.Bills;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillsRepository extends JpaRepository<Bills, Long> {
    Optional<Bills> findByExternalId(UUID externalId);
    Optional<Bills> findByExternalIdAndDeleteDateIsNull(UUID externalId);
    Page<Bills> getAllByDeleteDateIsNull(Pageable pageable);
    Page<Bills> getAllByDeleteDateIsNullAndWasPaidFalse(Pageable pageable);
    Page<Bills> getAllByDeleteDateIsNullAndWasPaidFalseAndUserId(Pageable pageable,UUID userId);
    Page<Bills> getAllByDeleteDateIsNullAndUserId(Pageable pageable,UUID userId);


    @Modifying
    @Transactional
    @Query("update Bills b set b.deleteDate = CURRENT_TIMESTAMP where b.externalId = :externalId")
    void delete(@Param("externalId") UUID externalId);
    @Modifying
    @Transactional
    @Query("update Bills b set b.amount = :amount, b.paymentDate = :paymentDate, b.wasPaid = :wasPaid where b.externalId = :externalId")
    void update(@Param("externalId") UUID externalId, @Param("amount") Double amount, @Param("paymentDate") LocalDate paymentDate, @Param("wasPaid")  boolean wasPaid);
    @Modifying
    @Transactional
    @Query("update Bills b set b.deleteDate = null where b.externalId = :externalId")
    void reestablish(@Param("externalId") UUID externalId);
    @Modifying
    @Transactional
    @Query("update Bills b set b.deleteDate = CURRENT_TIMESTAMP")
    void deleteAll();
}
