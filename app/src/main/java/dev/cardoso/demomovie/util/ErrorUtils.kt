package dev.cardoso.demomovie.util

import dev.cardoso.demomovie.model.Error
import retrofit2.Retrofit
import okio.IOException
import retrofit2.Response

/**
 * parses error response body
 */
object ErrorUtils {

    fun parseError(response: Response<*>, retrofit: Retrofit): Error? {
        val converter = retrofit.responseBodyConverter<Error>(Error::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            Error()
        }
    }
}