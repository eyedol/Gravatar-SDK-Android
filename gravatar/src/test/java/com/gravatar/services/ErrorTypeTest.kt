package com.gravatar.services

import com.gravatar.HttpResponseCode.HTTP_CLIENT_TIMEOUT
import com.gravatar.HttpResponseCode.HTTP_NOT_FOUND
import com.gravatar.HttpResponseCode.HTTP_TOO_MANY_REQUESTS
import com.gravatar.HttpResponseCode.SERVER_ERRORS
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorTypeTest {
    @Test
    fun `given an http code when converting to error type then correct type is returned`() {
        // Given
        val httpCodeToErrorTypeRelation = mutableListOf(
            HTTP_CLIENT_TIMEOUT to ErrorType.TIMEOUT,
            HTTP_NOT_FOUND to ErrorType.NOT_FOUND,
            HTTP_TOO_MANY_REQUESTS to ErrorType.RATE_LIMIT_EXCEEDED,
            600 to ErrorType.UNKNOWN,
        ).apply {
            SERVER_ERRORS.forEach { code ->
                add(code to ErrorType.SERVER)
            }
        }
        httpCodeToErrorTypeRelation.forEach { (code, expectedErrorType) ->
            // When
            val errorType = errorTypeFromHttpCode(code)
            // Then
            assert(errorType == expectedErrorType)
        }
    }

    @Test
    fun `given an exception when converting to error type then correct type is returned`() {
        // Given
        val exceptionToErrorTypeRelation = mutableListOf(
            SocketTimeoutException() to ErrorType.TIMEOUT,
            UnknownHostException() to ErrorType.NETWORK,
            Exception() to ErrorType.UNKNOWN,
        )
        exceptionToErrorTypeRelation.forEach { (exception, expectedErrorType) ->
            // When
            val errorType = exception.errorType()
            // Then
            assert(errorType == expectedErrorType)
        }
    }
}
