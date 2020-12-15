package ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.remote

<<<<<<< HEAD
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.Api
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.Constants
=======
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
>>>>>>> origin/master
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.Item

object ItemApi {
    private const val URL = "http://192.168.0.137:3000/"

    interface Service {
<<<<<<< HEAD
        @GET("api/item")
        suspend fun find(): List<Item>

        @GET("api/item/{id}")
        suspend fun read(@Path("id") itemId: String): Item;

        @Headers("Content-Type: application/json")
        @POST("api/item")
        suspend fun create(@Body item: Item): Item

        @Headers("Content-Type: application/json")
        @PUT("api/item/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Item): Item

        @DELETE("/api/item/{id}")
        suspend fun delete(@Path("id") itemId: String): Response<Unit>
    }

    val service: Service = Api.retrofit.create(Service::class.java)


    object RemoteDataSource {
        val eventChannel = Channel<String>()

        init {
            val request = Request.Builder().url(URL).build()
            OkHttpClient().newWebSocket(request, MyWebSocketListener()).request()
        }

        private class MyWebSocketListener : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                Log.d("WebSocket", "onOpen")
                val token = Constants.instance()?.fetchValueString("token")
                val json =
                    "{\"type\":\"authorization\",\"payload\":{\"token\":\"$token\"}}"
                //"{\"type\":\"authorization\",\"payload\":{\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImIiLCJfaWQiOiJ2STFHTWt6QnphNWk4Mnp6IiwiaWF0IjoxNjA1NjI5MDU4LCJleHAiOjE2MDU4NDUwNTh9.GepIJPYh_qR-5nRNIULd--7cT5tdfhJhmzSQKTApVzA\"}}"
                Log.d("json", json)
                webSocket.send(json)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "onMessage$text")
                runBlocking { eventChannel.send(text) }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d("WebSocket", "onMessage bytes")
                output("Receiving bytes : " + bytes.hex())
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                Log.e("WebSocket", "onFailure", t)
                t.printStackTrace()
            }

            private fun output(txt: String) {
                Log.d("WebSocket", txt)
            }
        }
    }
/*    private val client: OkHttpClient = OkHttpClient.Builder().build()
=======
        @GET("/item")
        suspend fun find(): List<Item>

        @GET("/item/{id}")
        suspend fun read(@Path("id") itemId: String): Item;

        @Headers("Content-Type: application/json")
        @POST("/item")
        suspend fun create(@Body item: Item): Item

        @Headers("Content-Type: application/json")
        @PUT("/item/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Item): Item
    }

    private val client: OkHttpClient = OkHttpClient.Builder().build()
>>>>>>> origin/master

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
<<<<<<< HEAD
*/

=======

    val service: Service = retrofit.create(Service::class.java)
>>>>>>> origin/master
}