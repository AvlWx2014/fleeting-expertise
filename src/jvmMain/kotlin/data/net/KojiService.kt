package data.net

import data.net.xmlrpc.MethodCall
import data.net.xmlrpc.MethodResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// TODO: use OkHttp client directly, since the resource for these annotations
//  needs to be dynamic based on config.
interface KojiService {
    @POST("kojihub")
    fun post(@Body body: MethodCall): Call<MethodResponse>
}