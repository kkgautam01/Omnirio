package account.controller

import account.serviceImplementation.AccountImplementation
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"])
@RequestMapping("/account")
@RestController
@EnableAutoConfiguration

class AccountController {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    private val accountImplementation : AccountImplementation?= null

    @RequestMapping(value = ["/getList"], method = [RequestMethod.GET])
    fun getList(@RequestHeader(value = "session") session: String): String {

        val response = accountImplementation!!.getList()
        return response.toString()
    }

    @RequestMapping(value = ["/createAccount"], method = [RequestMethod.POST])
    fun createUser(@RequestHeader(value = "session") session: String, @RequestBody payload: Map<String, Any>): String {
        val data = JSONObject(payload)
        val customerName = data.optString("customerName")
        val customerPhone = data.optString("customerPhone")
        val dateOfBirth = data.optString("dateOfBirth")
        val accountType = data.optString("accountType")
        val gender = data.optString("gender")
        val response = accountImplementation!!.create(customerPhone,customerName, dateOfBirth,accountType, gender)

        return response.toString()
    }

}