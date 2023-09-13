package com.atwa.rekompose.network

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.response.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.serialization.*

object ApiClient {

    private const val baseUrl = "https://api.github.com/"

    suspend inline fun <reified T> invokeCall(
        path: String,
        args: HashMap<String, String>,
    ): Result<T> {
        return runCatching {
            client.get {
                apiUrl(path, args)
            }
        }
    }

    val client by lazy {
        return@lazy try {
            HttpClient {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                install(Logging) {
                    logger = NetworkHttpLogger
                    level = LogLevel.BODY
                    filter { request ->
                        request.url.host.contains("ktor.io")
                    }
                }
            }


        } catch (e: Exception) {
            throw RuntimeException("Error initialization: ${e.message}")
        }
    }

    @OptIn(KtorExperimentalAPI::class)
    fun HttpRequestBuilder.apiUrl(path: String, hashMapOf: HashMap<String, String>) {
        header(HttpHeaders.CacheControl, io.ktor.client.utils.CacheControl.MAX_AGE)
        url {
            takeFrom(baseUrl)
            encodedPath = path
            hashMapOf.entries.forEach {
                parameters.append(it.key, it.value)
            }
        }
    }
}

data class NetworkException(override val message: String) : Exception()


