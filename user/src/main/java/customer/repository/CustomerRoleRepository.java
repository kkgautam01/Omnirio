package customer.repository;

import customer.model.CustomerRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRoleRepository extends JpaRepository<CustomerRole, String> {

    @Query(value = "SELECT * FROM customer_role", nativeQuery = true)
    Iterable<CustomerRole> getList();

    @Query(value = "SELECT * FROM customer_role WHERE role_code = :code", nativeQuery = true)
    CustomerRole getCustomerRoleById(@Param("code") String code);

}

