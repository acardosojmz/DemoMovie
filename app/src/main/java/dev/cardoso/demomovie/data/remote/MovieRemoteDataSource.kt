package dev.cardoso.demomovie.data.remote

import dagger.Provides
import dagger.hilt.android.scopes.ViewModelScoped
import dev.cardoso.demomovie.model.MovieDesc
import dev.cardoso.demomovie.model.TrendingMovieResponse
import dev.cardoso.demomovie.network.services.MovieService
import dev.cardoso.demomovie.util.ErrorUtils
import dev.cardoso.demomovie.model.Result
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


/**
 * fetches data from remote source
 */

@ViewModelScoped
class MovieRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchTrendingMovies(): Result<TrendingMovieResponse> {
        val movieService = retrofit.create(MovieService::class.java);
        return getResponse(
            request = { movieService.getPopularMovies() },
            defaultErrorMessage = "Error fetching Movie list")

    }

    suspend fun fetchMovie(id: Int): Result<MovieDesc> {
        val movieService = retrofit.create(MovieService::class.java);
        return getResponse(
            request = { movieService.getMovie(id) },
            defaultErrorMessage = "Error fetching Movie Description")
    }

    private suspend fun <T> getResponse(request: suspend () -> Response<T>,
                                        defaultErrorMessage: String): Result<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage,
                    errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }
}