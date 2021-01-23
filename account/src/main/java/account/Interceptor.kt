package acount

import account.serviceImplementation.AuthImplementation
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import org.slf4j.LoggerFactory;
import org.json.JSONObject
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

@Component
@EnableAutoConfiguration
class Interceptor : HandlerInterceptorAdapter() {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    private val authImplementation: AuthImplementation? = null

    @Throws(Exception::class)
    override fun preHandle(req: HttpServletRequest, res: HttpServletResponse?, handler: Any?): Boolean {
        logger.info("Interceptor::PreHandle::Request URL :: " + req.requestURL)
        logger.info("Interceptor::PreHandle::Request URL :: " + req.method)
        logger.info("Interceptor::PreHandle::getHeader :: " + req.getHeader("session"))

        if(req.method != "OPTIONS") {
            var obj = JSONObject()
            var device = "app"
            var session = try{
                req.getHeader("session")
            }catch ( e : Exception){
                ""
            }
            try {

                val isAuth = authImplementation!!.authoriseSession(session)

                logger.info("Interceptor::PreHandle::getHeader :: isAuth:$isAuth")

                return if (!isAuth) {
                    obj.put("code", 401)
                    obj.put("msg", "Unauthorised Access")
                    obj.put("status", false)

                    res!!.writer.write(obj.toString())
                    logger.info("Interceptor::PreHandle::UnAuthorised::$obj")
                    res.status = 401
                    res.setHeader("Access-Control-Allow-Origin", "*")
                    false
                } else {
                    val api  =req.requestURI
                    val getApiDetail = authImplementation!!.getApiDetails(api)

                    val authApi = authImplementation!!.authenticateApiAccess(session,getApiDetail.getString("type"),getApiDetail.getString("value"))
                    if(!authApi.getBoolean("status"))
                    {
                        logger.info("Interceptor::PreHandle::UnAuthorised::Error::$authApi")
                        res!!.writer.write(authApi.toString())
                        res.status = 403
                        res.setHeader("Access-Control-Allow-Origin", "*")
                        return false
                    }
                    true
                }

            } catch (e: Exception) {
                obj.put("code", 401)
                obj.put("msg", "Invalid Session")
                obj.put("status", false)
                logger.info("Interceptor::PreHandle::UnAuthorised::Error::$obj")
                res!!.writer.write(obj.toString())
                res.status = 401
                res.setHeader("Access-Control-Allow-Origin", "*")

                return false
            }
        }else{
            return true
        }

    }


    @Throws(Exception::class)
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse?, handler: Any?,
                            modelAndView: ModelAndView?) {
        logger.info("Interceptor::PostHandle:: request :: $request")
        logger.info("Interceptor::PostHandle:: response :: $response")
    }

    @Throws(Exception::class)
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse?, handler: Any?,
                                 exception: Exception?) {
        logger.info("Interceptor::AfterCompletion::")
        logger.info("Interceptor::AfterCompletion:: request :: $request")
        logger.info("Interceptor::AfterCompletion:: response :: $response")


    }
}