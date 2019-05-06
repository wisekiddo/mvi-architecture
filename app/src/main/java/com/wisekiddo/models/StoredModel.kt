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
import androidx.room.PrimaryKey
import com.wisekiddo.stored.db.Constants

/**
 * Model used solely for the caching of a data
 */
@Entity(tableName = Constants.TABLE_NAME)

data class StoredModel(
    @PrimaryKey(autoGenerate = true) val seed: Int,
    val cell: String,
    @Embedded(prefix = "dob") val dob: Dob,
    val email: String,
    val gender: String,
    @Embedded(prefix = "id") val id: Id,
    @Embedded(prefix = "location") val location: Location,
    @Embedded(prefix = "login") val login: Login,
    @Embedded(prefix = "name") val name: Name,
    val nat: String,
    val phone: String,
    @Embedded(prefix = "picture") val picture: Picture,
    @Embedded(prefix = "registered") val registered: Registered
)
