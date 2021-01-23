package account.serviceImplementation

import account.service.AccountService
import org.json.JSONObject
import org.springframework.stereotype.Service

import org.json.JSONArray

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import account.config.Constant
import account.model.Account
import java.net.http.HttpClient
import java.time.LocalDate
import java.util.concurrent.TimeUnit

import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import java.util.Locale
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.GregorianCalendar
import org.apache.http.HttpEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.client.postForEntity
import org.springframework.util.MultiValueMap
import javax.validation.constraints.Null
import java.util.HashMap





@Service
class AccountImplementation {


    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    private val accountService: AccountService? = null

    @Autowired
    private val restTemplate : RestTemplate ?= null

    open fun getList(): JSONObject {
        logger.info("AccountImplementation :: getList ")
        var response = JSONObject()

        response.put("status", false)
        response.put("msg", "Could not fetch list, try again")
        response.put("data", "")

        try{
            val list = accountService!!.getAccountList()
            var userArray = JSONArray()
            if(list.count() > 0){
                for(l in list){
                    val user = createAccountObj(l!!.account_id , l!!.account_type, l!!.branch, l!!.customer_id, l!!.customer_name, l!!.minor_indicator, l!!.open_date)
                    userArray.put(user)
                }
            }
            response.put("status", true)
            response.put("msg", "Account list")
            response.put("data", userArray)
        }catch(e : Exception){
            logger.error("Error:: AccountImplementation :: getList : $e")
        }

        return response
    }

    open fun createAccountObj(accountId  :String , accountType : String , branch : String , customerId : String, customerName : String, minorIndicator : String, openDate : String) : JSONObject{
        val obj = JSONObject()

        obj.put("accountId", accountId)
        obj.put("accountType", accountType)
        obj.put("branch", branch)
        obj.put("customerId", customerId)
        obj.put("customerName", customerName)
        obj.put("minorIndicator", minorIndicator)
        obj.put("openDate", openDate)

        return obj
    }

    open fun create(customerPhone : String,customerName : String, dateOfBirth : String,accountType : String, gender : String) : JSONObject{
        logger.info("AccountImplementation :: getList ")
        var response = JSONObject()

        response.put("status", false)
        response.put("msg", "Could not create account, try again")
        response.put("data", "")

        if(customerPhone.isNullOrEmpty() || (customerPhone.length < 10 && customerPhone.length > 15)){
            response.put("msg", "Invalid phonenumber")
            return response
        }

        if(customerName.isNullOrEmpty()){
            response.put("msg", "Customer Name is empty")
            return response
        }

        if(accountType.isNullOrEmpty()){
            response.put("msg", "accountType is empty")
            return response
        }

        if(gender.isNullOrEmpty()){
            response.put("msg", "gender is empty")
            return response
        }

        val dob = try{
            SimpleDateFormat("yyyy-MM-DD").parse(dateOfBirth)
        }catch(e : Exception){
            Date()
        }

        val dateDiff = try{
            dateDiff(dob)
        }catch(e : Exception){
            0
        }

        logger.info("AccountImplementation :: dateDiff :: $dateDiff ")


        if(dateDiff < 18){
            response.put("msg", "Under age. Not authorised to register")
            return response
        }
        try{

            val postData: MutableMap<String, String> = HashMap()
            postData["customerName"] = customerName
            postData["customerPhone"] = customerPhone
            postData["dob"] = dateOfBirth.toString()
            postData["gender"] = gender
            postData["accountType"] = accountType


            val url = "http://customer/customer/createUser"

            var header = JSONObject()
            header.put("content-type","application/json")


            val apiResponse = try{
                JSONObject(restTemplate!!.postForObject(url, postData, String::class.java, postData))
            }catch(e : Exception){
                logger.error("Error: CreateAccount :: $e")
                JSONObject()
            }

            logger.info("AccountImplementation :: apiResponse :: $apiResponse ")


            if(apiResponse.has("status") && apiResponse.getBoolean("status")) {
                val data = apiResponse.getJSONObject("data")
                val customerId = data.getString("userId")
                val branch = data.getString("branch")
                val minor_indicator = "no"
                val date = Date()
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
                val openDate = formatter.format(date)

                val accountId = createAccountId(customerId)
                accountService!!.create(accountId, accountType, openDate.toString(), customerId, customerName, branch, minor_indicator)

                data.put("accountId", accountId)
                data.put("accountType", accountType)
                data.put("openDate", openDate)
                data.put("minor_indicator", minor_indicator)
                response.put("status", true)
                response.put("msg", "created account")
                response.put("data", data)


            }else{
                response.put("msg", apiResponse.getString("msg"))
            }
        }catch(e : Exception){
            logger.error("Error: CreateAccount :: $e")
        }

        return response
    }

    open fun createAccountId(customerId: String) : String{
        return "acc-$customerId"
    }

    open fun dateDiff(dob : Date) : Long{
         val calendar: Calendar = GregorianCalendar()
        calendar.time = dob
        val year = calendar[Calendar.YEAR]

        val month = calendar[Calendar.MONTH] + 1
        val day = calendar[Calendar.DAY_OF_MONTH]
        val start = LocalDate.of(year,month,day)
        val stop = LocalDate.now(ZoneId.of("America/Montreal"))
        val years = ChronoUnit.YEARS.between(start, stop)

        return years
     }

}