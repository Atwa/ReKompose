package com.atwa.rekompose.core.network

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.takeFrom
import io.ktor.util.KtorExperimentalAPI

class ApiClient(private val baseUrl: String) {

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


