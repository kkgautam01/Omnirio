package customer.repository;

import customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query(value = "SELECT * FROM customer", nativeQuery = true)
    Iterable<Customer> getList();

    @Query(value = "SELECT * FROM customer WHERE user_id = :userId", nativeQuery = true)
    Customer getUserById(@Param("userId") String userId);

    @Modifying
    @Query(value = "INSERT INTO customer (user_id, user_name, date_of_birth, gender, phone_number) VALUES(:userId, :userName, :dateOfBirth, :gender, :phoneNumber) ", nativeQuery = true)
    void createUser(@Param("userId") String userId,@Param("userName") String userName,@Param("dateOfBirth") String dateOfBirth,@Param("gender") String gender,@Param("phoneNumber") String phoneNumber);




}

