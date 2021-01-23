package account.repository;

import account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query(value = "SELECT * FROM account", nativeQuery = true)
    Iterable<Account> getList();

    @Query(value = "SELECT *  FROM account WHERE customer_id = :customerId", nativeQuery = true)
    Account getAccountById(@Param("customerId") String customerId);

    @Modifying
    @Query(value = "INSERT INTO account (account_id, account_type, open_date, customer_id, customer_name, branch,minor_indicator ) VALUES(:accountId, :accountType, :openDate, :customerId, :customerName, :branch, :minor_indicator) ", nativeQuery = true)
    void createAccount(@Param("accountId") String accountId,@Param("accountType") String accountType,@Param("openDate") String openDate,@Param("customerId") String customerId,@Param("customerName") String customerName, @Param("branch") String branch ,@Param("minor_indicator") String minor_indicator);

}

