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

package com.wisekiddo.models

import androidx.room.Embedded
import androidx.room.Entity
import com.wisekiddo.stored.db.Constants

/**
 * Representation for a [RemoteModel] fetched from the API
 */
data class RemoteModel(

    val cell: String,
    val dob: Dob,
    val email: String,
    val gender: String,
    val id: Id,
    val location: Location,
    val login: Login,
    val name: Name,
    val nat: String,
    val phone: String,
    val picture: Picture,
    val registered: Registered
)

data class Info(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
)


data class Id(
    val name: String?=null,
    val value: String?=null
)

data class Dob(
    val age: Int?=null,
    val date: String?=null
)

data class Login(
    val md5: String?=null,
    val password: String?=null,
    val salt: String?=null,
    val sha1: String?=null,
    val sha256: String?=null,
    val username: String?=null,
    val uuid: String?=null
)

data class Registered(
    val age: Int?=null,
    val date: String?=null
)

data class Picture(
    val large: String?=null,
    val medium: String?=null,
    val thumbnail: String?=null
)

data class Name(
    val first: String?=null,
    val last: String?=null,
    val title: String?=null
)

data class Location(
    val city: String?=null,
    @Embedded val coordinates: Coordinates?=null,
    val postcode: Int?=null,
    val state: String?=null,
    val street: String?=null,
    @Embedded val timezone: Timezone?=null
)

data class Timezone(
    val description: String?=null,
    val offset: String?=null
)

data class Coordinates(
    val latitude: String?=null,
    val longitude: String?=null
)