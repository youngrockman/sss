package com.example.shoesapptest.data.remote.network.auth

import com.example.shoesapptest.data.remote.network.AuthRequest
import com.example.shoesapptest.data.remote.network.AuthResponse
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.data.remote.network.RegistrationRequest
import com.example.shoesapptest.data.remote.network.RegistrationResponse

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthRemoteSource {
    @POST("/registration")
    suspend fun  registration(@Body registrationRequest: RegistrationRequest): RegistrationResponse

    @POST("/authorization")
    suspend fun authorization(@Body authorizationRequest: AuthRequest): AuthResponse

    @GET("/allSneakers")
    suspend fun popular(): List<SneakersResponse>

    @GET("/sneakers/popular")
    suspend fun getPopularSneakers(): List<SneakersResponse>

    @GET("/sneakers/{category}")
    suspend fun getSneakersByCategory(@Path("category") category: String): List<SneakersResponse>

}


