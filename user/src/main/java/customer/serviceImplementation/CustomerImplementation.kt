package customer.serviceImplementation

import customer.service.CustomerService
import org.json.JSONObject
import org.springframework.stereotype.Service

import org.json.JSONArray

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import customer.config.Constant

@Service
class CustomerImplementation {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    private val customerService: CustomerService? = null


    open fun getList(): JSONObject {
        logger.info("CustomerImplementation :: getList ")
        var response = JSONObject()

        response.put("status", false)
        response.put("msg", "Could not fetch list, try again")
        response.put("data", "")

        try{
            val list = customerService!!.getCustomerList()
            var userArray = JSONArray()
            if(list.count() > 0){
                for(l in list){
                   val user = createUserObj(l!!.user_id , l!!.user_name, l!!.date_of_birth, l!!.gender, l!!.phone_number,"")
                    userArray.put(user)
                }
            }
            response.put("status", true)
            response.put("msg", "User list")
            response.put("data", userArray)
        }catch(e : Exception){
            logger.error("Error:: CustomerImplementation :: getList : $e")
        }

        return response
    }

    open fun createUser(userName: String, dateOfBirth: String, gender: String, phoneNumber: String, roleCode : String): JSONObject {
        logger.info("CustomerImplementation :: createUSer : userName : $userName, dob :: $dateOfBirth, gender : $gender, phone : $phoneNumber ")
        var response = JSONObject()

        response.put("status", false)
        response.put("msg", "Could not create User, try again")
        response.put("data", "")

        if(gender.isNullOrEmpty() || (!gender.equals(Constant.MALE) && !gender.equals(Constant.FEMALE))){
            response.put("msg", "Gender value is invalid.")
            return response
        }

        try{
            val role = getroleById(roleCode)
            if(role.getBoolean("status")) {
                val userId = createUserId(userName, phoneNumber)
                val roleData = role.getJSONObject("data")
                val getUser = customerService!!.getCustomerById(userId)
                if(getUser == null) {
                    customerService!!.createUser(userId, userName, dateOfBirth, gender, phoneNumber)
                    val userObj = createUserObj(userId, userName, dateOfBirth, gender, phoneNumber, roleData.optString("roleName"))
                    response.put("status", true)
                    response.put("msg", "User created")
                    response.put("data", userObj)
                }else{
                    response.put("msg", "User already exists with same name and phone")

                }
            }else{
                response.put("msg", role.getString("msg"))
            }
        }catch(e : Exception){
            logger.error("Error:: CustomerImplementation :: createUSer : $e")
        }
        return response
    }


    open fun createUserObj(userId : String, userName: String, dateOfBirth: String, gender: String, phoneNumber: String, roleName: String) : JSONObject{
        val obj = JSONObject()
        obj.put("userId", userId)
        obj.put("userName", userName)
        obj.put("dateOfBirth", dateOfBirth)
        obj.put("gender",gender)
        obj.put("phoneNumber", phoneNumber)
        obj.put("branch", roleName)

        return obj
    }

    open fun createUserRoleObj(roleId : String, roleName: String, roleCode: String) : JSONObject{
        val obj = JSONObject()
        obj.put("roleId", roleId)
        obj.put("roleName", roleName)
        obj.put("roleCode", roleCode)

        return obj
    }

    open fun createUserId(userName: String, phoneNumber: String) : String{
        return "$userName-$phoneNumber"
    }

    open fun getroleById(id : String): JSONObject{
        logger.info("CustomerImplementation :: getroleById : id : $id")
        var response = JSONObject()

        response.put("status", false)
        response.put("msg", "Could not get role, try again")
        response.put("data", "")
        try{
             val role = customerService!!.getCustomerRoleByCode(id)
            if(role != null) {
                val userObj = createUserRoleObj(role.role_id,role.role_name,role.role_code)
                response.put("status", true)
                response.put("msg", "got role")
                response.put("data", userObj)
            }
        }catch(e : Exception){
            logger.error("Error:: CustomerImplementation :: createUSer : $e")
        }
        return response
    }
}