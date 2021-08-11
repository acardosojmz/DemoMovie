package dev.cardoso.demomovie.ui.details

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cardoso.demomovie.data.MovieRepository
import dev.cardoso.demomovie.model.MovieDesc
import dev.cardoso.demomovie.model.Result
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


/**
 * ViewModel for Movie details screen
 */
@HiltViewModel
class DetailsViewModel   @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {
    private var _id = MutableLiveData<Int>()
    private val _movie: LiveData<Result<MovieDesc>> = _id.distinctUntilChanged().switchMap {
        liveData {
            movieRepository.fetchMovie(it).onStart {
                emit(Result.loading())
            }.collect {
                emit(it)
            }
        }
    }
    val movie = _movie

    fun getMovieDetail(id: Int) {
        _id.value = id
    }
}