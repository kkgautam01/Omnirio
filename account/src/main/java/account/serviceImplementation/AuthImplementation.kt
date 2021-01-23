package account.serviceImplementation

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import account.service.AccountService
import io.jsonwebtoken.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import java.security.Key
import java.util.*

import account.config.Constant
import account.model.Account
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthImplementation {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    private val accountService : AccountService ? = null

    private val SECRET_KEY = Constant.SECRETKEY

    open fun authoriseSession(session: String): Boolean {
        logger.info("AuthImplementation::authoriseSession: session:$session")

        var isAuthorised = 0

        val token = try{
            JSONObject(decodeJWT(session))
        }catch(E : Exception)
        {
            JSONObject()
        }

        val userId = try{
            token.getString("id")
        } catch(E : Exception)
        {
            ""
        }

        val acc = getAccountDetailsById(userId)
        logger.info("AuthImplementation::authoriseSession: acc: $acc")
        return acc != null

    }

    open fun getAccountDetailsById(id: String) : Account?{
        logger.info("AuthImplementation::getAccountDetailsById: id:$id")

        return accountService!!.getAccountById(id)
    }

    open fun authenticateApiAccess(session: String, permissionType: String, permissionValue: String) : JSONObject {
        logger.info("AuthImplementation::authenticateApiAccess: session: $session, permissionType: $permissionType, permissionValue: $permissionValue")

        var response = JSONObject()
        response.put("status", false)
        response.put("code", 403)
        response.put("msg", "User is not authorised to access this service.")

        val token = try{
            JSONObject(decodeJWT(session))
        }catch(E : Exception)
        {
            JSONObject()
        }

        val userId = try{
            token.getString("id")
        } catch(E : Exception)
        {
            ""
        }

        val acc = getAccountDetailsById(userId)
        var flag = false

        if(acc != null) {
            logger.info("AuthImplementation::authenticateApiAccess: flag: $flag, permissionValue:$permissionValue:: uservalue: ${acc.account_type}, permissionType: $permissionType :: Usertype : ${acc.branch}")

            var type = permissionType
            var value = permissionValue
            if (type.equals("*")) {
                if (value.toInt() <= acc.account_type.toInt())
                    flag = true
            } else if (type.equals(acc.branch)) {
                if (value.toInt() <= acc.account_type.toInt())
                    flag = true
            }
        }



        if (!flag) {
            return response
        } else {
            response.put("status", true)
            response.put("code", 200)
            response.put("msg", "User is authorised to access this service.")
        }




        return response
    }

    open fun getApiDetails(api: String) : JSONObject {
        logger.info("AuthImplementation::getApiDetails: api: $api")

        var res = JSONObject()
        res.put("api","")
        res.put("type","")
        res.put("value","")

        val apiObj =arrayOf(
                arrayOf("/account/getList","*","1"),
                arrayOf("/account/createAccount","manager","2"),
                arrayOf("/account/updateAccount","manager","2"),
                arrayOf("/account/deleteAccount","manager","2")
        )

        for(a in apiObj){
            logger.info("AuthImplementation::getApiDetails: a : ${a[0]}")

            if(api.equals(a[0]))
            {
                res.put("api",a[0])
                res.put("type",a[1])
                res.put("value",a[2])
            }
        }

        logger.info("AuthImplementation::getApiDetails: res : $res")

        return res
    }

    open fun createJWT(id: String?, issuer: String?, subject: String?, ttlMillis: Long): String? {
        val signatureAlgorithm = SignatureAlgorithm.HS256
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY)
        val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)

        val builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey)

        if (ttlMillis >= 0) {
            val expMillis = nowMillis + ttlMillis
            val exp = Date(expMillis)
            builder.setExpiration(exp)
        }

        return builder.compact()
    }

    open fun decodeJWT(jwt: String?): Claims? {
        logger.info("AuthImplementation::decodeJWT:: jwt:$jwt")

        val resp = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).body

        logger.info("AuthImplementation::decodeJWT:: resp:$resp")

        return resp
    }
}