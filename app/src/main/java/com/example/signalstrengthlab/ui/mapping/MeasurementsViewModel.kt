package com.example.signalstrengthlab.ui.mapping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signalstrengthlab.common.Resource
import com.example.signalstrengthlab.data.model.Measurement
import com.example.signalstrengthlab.data.repository.SignalStrengthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeasurementsViewModel @Inject constructor(
    private val repository: SignalStrengthRepository
) : ViewModel() {

    private val _measurements: MutableStateFlow<Resource<List<Measurement>>> =
        MutableStateFlow(Resource.Loading())

    val measurements: StateFlow<Resource<List<Measurement>>>
        get() = _measurements.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMeasurements(
                localDataSourceCall = { _measurements.emit(it) },
                remoteDataSourceCall = { _measurements.emit(it) }
            )
        }
    }
}