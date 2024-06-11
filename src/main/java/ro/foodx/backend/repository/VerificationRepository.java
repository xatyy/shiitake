package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.foodx.backend.model.user.Verification;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface VerificationRepository extends JpaRepository<Verification, UUID> {
    Verification findOneByUser_Id(UUID id);

    Verification findOneById(UUID id);


    //@Query("select u from Verification u where timestamp < u.timestamp")
    List<Verification> findByTimestampLessThan(Long timestamp);
}
