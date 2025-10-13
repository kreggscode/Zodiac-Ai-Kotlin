package com.kreggscode.zodiacfinder.data.api

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// API Response Models
data class PollinationTextResponse(
    @SerializedName("choices")
    val choices: List<Choice>
)

data class Choice(
    @SerializedName("message")
    val message: Message
)

data class Message(
    @SerializedName("content")
    val content: String,
    @SerializedName("role")
    val role: String = "assistant"
)

// API Request Models
data class PollinationRequest(
    @SerializedName("model")
    val model: String = "openai",
    @SerializedName("messages")
    val messages: List<MessageRequest>,
    @SerializedName("temperature")
    val temperature: Float = 1.0f,
    @SerializedName("seed")
    val seed: Int? = null
)

data class MessageRequest(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String
)

interface PollinationAIService {
    
    @POST("openai")
    suspend fun generateText(
        @Body request: PollinationRequest
    ): PollinationTextResponse
    
    @GET("{prompt}")
    suspend fun generateTextSimple(
        @Path("prompt", encoded = true) prompt: String,
        @Query("model") model: String = "openai",
        @Query("temperature") temperature: Float = 1.0f,
        @Query("seed") seed: Int? = null
    ): String
    
    companion object {
        private const val BASE_URL = "https://text.pollinations.ai/"
        
        fun create(): PollinationAIService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            return retrofit.create(PollinationAIService::class.java)
        }
    }
}
