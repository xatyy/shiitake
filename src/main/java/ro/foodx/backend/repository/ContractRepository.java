package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foodx.backend.model.store.Contract;
import java.util.UUID;

public interface ContractRepository extends JpaRepository<Contract, UUID> {
}
