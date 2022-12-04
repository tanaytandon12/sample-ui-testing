package com.example.sampleuitesting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

abstract class DemoViewModel : ViewModel() {
    abstract fun fetchInfo(id: Long): Flow<DemoDataStatus>

    class Factory() : ViewModelProvider.NewInstanceFactory() {
        companion object {
            var INSTANCE: DemoViewModel? = null
        }

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(DemoViewModel::class.java)) {
                (INSTANCE ?: DemoViewModelImpl()) as T
            } else super.create(modelClass)
        }
    }
}

class DemoViewModelImpl() : DemoViewModel() {
    override fun fetchInfo(id: Long): Flow<DemoDataStatus> {
        return flow {
            emit(DemoDataStatus.Loading)
            delay(2000L)
            val isSuccess = ((Math.random() * 100).toInt() % 2) == 0
            emit(
                if (isSuccess) DemoDataStatus.Success(
                    DemoData("sample title", "sample description")
                ) else DemoDataStatus.Error("there was a failure")
            )
        }
    }

}

data class DemoData(val title: String, val description: String)

sealed class DemoDataStatus {
    object Loading : DemoDataStatus()
    data class Success(val data: DemoData) : DemoDataStatus()
    data class Error(val msg: String) : DemoDataStatus()
}