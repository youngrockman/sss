package com.example.shoesapptest.data.repository

import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.data.remote.network.auth.AuthRemoteSource
import com.example.shoesapptest.data.remote.network.AuthRequest
import com.example.shoesapptest.data.remote.network.AuthResponse
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.data.remote.network.RegistrationRequest
import com.example.shoesapptest.data.remote.network.RegistrationResponse

class AuthRepository(val authRemoteSource: AuthRemoteSource) {

    suspend fun registration(registrationRequest: RegistrationRequest): RegistrationResponse {
        return authRemoteSource.registration(registrationRequest)
    }

    suspend fun authorization(authorizationRequest: AuthRequest): AuthResponse {
        return authRemoteSource.authorization(authorizationRequest)
    }

    suspend fun getSneakers(): NetworkResponseSneakers<List<SneakersResponse>> {
        return try {
            val result = authRemoteSource.popular()
            NetworkResponseSneakers.Success(result)
        } catch (e: Exception) {
            NetworkResponseSneakers.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun getPopularSneakers(): NetworkResponseSneakers<List<SneakersResponse>> {
        return try {
            val result = authRemoteSource.getPopularSneakers()
            NetworkResponseSneakers.Success(result)
        } catch (e: Exception) {
            NetworkResponseSneakers.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun getSneakersByCategory(category: String): NetworkResponseSneakers<List<SneakersResponse>> {
        return try {
            val result = authRemoteSource.getSneakersByCategory(category)
            NetworkResponseSneakers.Success(result)
        } catch (e: Exception) {
            NetworkResponseSneakers.Error(e.message ?: "Unknown Error")
        }
    }
}







