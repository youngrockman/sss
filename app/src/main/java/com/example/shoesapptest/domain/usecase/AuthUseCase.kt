package com.example.shoesapptest.domain.usecase

import com.example.shoesapptest.data.local.DataStore
import com.example.shoesapptest.data.remote.network.NetworkResponse
import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.data.remote.network.AuthRequest
import com.example.shoesapptest.data.remote.network.AuthResponse
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.data.remote.network.RegistrationRequest
import com.example.shoesapptest.data.remote.network.RegistrationResponse
import com.example.shoesapptest.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class AuthUseCase(private val dataStore: DataStore, private val authRepository: AuthRepository) {
    val token: Flow<String> = dataStore.tokenFlow

     fun registration(registrationRequest: RegistrationRequest): Flow<NetworkResponse<RegistrationResponse>> = flow {
        try {
            emit(NetworkResponse.Loading)
            if (!EmailValidator().validate(registrationRequest.email)) {
                emit(NetworkResponse.Error("Invalid email format"))
                return@flow
            }
            if (!PasswordValidator().validate(registrationRequest.password)) {
                emit(NetworkResponse.Error("Password must contain: 8+ chars, 1 uppercase, 1 digit, 1 special char"))
                return@flow
            }

            val result = authRepository.registration(registrationRequest)
            dataStore.setToken(result.token)
            emit(NetworkResponse.Success(result))
        } catch (e: Exception) {
            emit(NetworkResponse.Error(e.message ?: "Unknown Error"))

            println("Registration failed: ${e.message}")
        }
    }


     fun authorization(authorizationRequest: AuthRequest): Flow<NetworkResponse<AuthResponse>> = flow {
        try {
            emit(NetworkResponse.Loading)
            val result = authRepository.authorization(authorizationRequest)
            dataStore.setToken(result.token)
            emit(NetworkResponse.Success(result))
        } catch (e: Exception) {
            emit(NetworkResponse.Error(e.message ?: "Unknown Error"))
        }
    }

    suspend fun getSneakers(): NetworkResponseSneakers<List<SneakersResponse>> {
        return try {
            val result = authRepository.getSneakers()
            result
        } catch (e: Exception) {
            NetworkResponseSneakers.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun getSneakersByCategory(category: String): NetworkResponseSneakers<List<SneakersResponse>> {
        return authRepository.getSneakersByCategory(category)
    }

    suspend fun getPopularSneakers(): NetworkResponseSneakers<List<SneakersResponse>> {
        return authRepository.getPopularSneakers()
    }


}




