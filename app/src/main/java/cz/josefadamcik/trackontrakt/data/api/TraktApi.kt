/*
 Copyright 2017 Josef Adamcik <josef.adamcik@gmail.com>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
package cz.josefadamcik.trackontrakt.data.api

import cz.josefadamcik.trackontrakt.data.api.model.HistoryItem
import cz.josefadamcik.trackontrakt.data.api.model.OauthTokenRequest
import cz.josefadamcik.trackontrakt.data.api.model.OauthTokenResponse
import cz.josefadamcik.trackontrakt.data.api.model.Settings
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

/**
 * Trakt API interface for retrofit.
 *
 * http://docs.trakt.apiary.io/
 *
 * Contains only calls need for the application.
 */
interface TraktApi {

    @POST("/oauth/token")
    fun oauthToken(@Body data: OauthTokenRequest): Single<Response<OauthTokenResponse>>

    @GET("/users/settings")
    fun userSettings(@Header("Authorization") authorization: String): Single<Settings>


    @GET("/users/me/history")
    fun myHistory(@Header("Authorization") authorization: String,
                  @Query("page") page: Int,
                  @Query("limit") limit: Int): Single<List<HistoryItem>>

}