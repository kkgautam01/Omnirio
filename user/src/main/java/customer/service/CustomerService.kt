package customer.service

import customer.model.CustomerRole;
import customer.model.Customer;
import customer.repository.CustomerRoleRepository;
import customer.repository.CustomerRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
open class CustomerService(private val customerRepository: CustomerRepository, private val customerRoleRepository: CustomerRoleRepository) {

    // customer
    open fun getCustomerList(): Iterable<Customer?> {
        return customerRepository.getList()
    }

    open fun getCustomerById(userId : String): Customer? {
        return customerRepository.getUserById(userId)
    }

    open fun createUser(userId : String, userName: String, dateOfBirth: String, gender: String, phoneNumber : String) {
        customerRepository.createUser(userId, userName, dateOfBirth, gender, phoneNumber )
    }

    // customer Role
    open fun getCustomerRoleList(): Iterable<CustomerRole?> {
        return customerRoleRepository.getList()
    }

    open fun getCustomerRoleByCode(code : String): CustomerRole? {
        return customerRoleRepository.getCustomerRoleById(code)
    }


}
