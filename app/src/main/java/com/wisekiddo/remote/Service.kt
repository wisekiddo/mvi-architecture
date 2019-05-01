/*
 * Copyright 2019 Wisekiddo by Ronald Garcia Bernardo. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wisekiddo.remote

import com.wisekiddo.models.RemoteModel
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines the abstract methods used for interacting with the DomainModel API
 */
interface Service {

    @GET("/api/")
    fun getData(@Query("gender") gender:String): Flowable<DataResponse>

    class DataResponse {
        lateinit var result: List<RemoteModel>
    }

    //@GET("?gender=$gender")
    //fun getDataByGender( gender:String,  id:String): Flowable<DataResponse>

    //@Query("q") query: String,
    //@Query("sort") sort: String,
    //@Query("order") order: String
    //user: https://randomuser.me/api/?seed=0002
    //multiple user: https://randomuser.me/api/?results=10
}