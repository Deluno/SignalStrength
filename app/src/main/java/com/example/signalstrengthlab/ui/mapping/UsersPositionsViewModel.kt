package com.example.signalstrengthlab.ui.mapping

import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signalstrengthlab.common.Resource
import com.example.signalstrengthlab.data.model.Measurement
import com.example.signalstrengthlab.data.model.User
import com.example.signalstrengthlab.data.repository.SignalStrengthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

@HiltViewModel
class UsersPositionsViewModel @Inject constructor(
    private val repository: SignalStrengthRepository
) : ViewModel() {

    private val _users: MutableStateFlow<Resource<List<User>>> =
        MutableStateFlow(Resource.Loading())
    val users: StateFlow<Resource<List<User>>>
        get() = _users.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUsers(
                localDataSourceCall = { _users.emit(it) },
                remoteDataSourceCall = { _users.emit(it) }
            )
        }
    }

    fun getUsersPositions(measuredLocation: List<Measurement>): Flow<Map<User, IntOffset?>> = flow {
        val positions = _users.map { resource ->
            if (resource is Resource.Success) {
                resource.data!!.associateWith { calculateUserPosition(it, measuredLocation) }
            } else emptyMap()
        }
        emitAll(positions)
    }

    private fun calculateUserPosition(user: User, measuredLocation: List<Measurement>): IntOffset? {
        val distances = measuredLocation.map { measurement ->
            val userStrengths = user.signals.map { it.strength }
            val measurementStrength = measurement.signals.map { it.strength }
            val distance = getEuclideanDistance(userStrengths, measurementStrength)
            Pair(IntOffset(measurement.x, measurement.y), distance)
        }
        return distances.minByOrNull { it.second }?.first
    }

    private fun getEuclideanDistance(vector1: List<Int>, vector2: List<Int>): Double {
        var sum = 0.0
        (vector1 zip vector2).forEach { pair ->
            sum += (pair.second - pair.first).toDouble().pow(2)
        }
        return sqrt(sum)
    }
}