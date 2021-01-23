package customer.controller

import customer.serviceImplementation.CustomerImplementation
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.ws.rs.core.Request

@CrossOrigin(origins = ["*"])
@RequestMapping("/customer")
@RestController
@EnableAutoConfiguration
class CustomerController {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    private val customerImplementation : CustomerImplementation?= null

    //***************** List *************
    @RequestMapping(value = ["/getList"], method = [RequestMethod.GET])
    fun getList(): String {
        logger.info("CustomerController::getList")
        val userList = customerImplementation!!.getList()
        return userList.toString()
    }

    @RequestMapping(value = ["/createUser"], method = [RequestMethod.POST], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createUser( @RequestBody payload: Map<String, Any>): String {
         val data = JSONObject(payload)
        logger.info("CustomerController::createUser :: data :: $data ")

        val customerName = data.optString("customerName")
        val customerPhone = data.optString("customerPhone")
        val dateOfBirth = data.optString("dob")
        val gender = data.optString("gender")
        val accountType = data.optString("accountType")
        val response = customerImplementation!!.createUser(customerName, dateOfBirth, gender, customerPhone,accountType)
        return response.toString()
    }

}