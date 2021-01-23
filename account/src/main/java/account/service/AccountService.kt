package account.service

import account.model.Account
import account.repository.AccountRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
@Component
class AccountService (private val accountRepository: AccountRepository){

    open fun getAccountById(customerId : String) : Account?{
        return accountRepository.getAccountById(customerId)
    }

    open fun getAccountList() : Iterable<Account?>{
        return accountRepository.getList()
    }

    open fun create(accountId : String, accountType : String, openDate : String, customerId : String, customerName : String, branch : String, minor_indicator : String) {
        return accountRepository.createAccount(accountId, accountType, openDate, customerId, customerName, branch, minor_indicator)

    }
}