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

package com.wisekiddo.stored.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wisekiddo.stored.dao.StoredDao
import com.wisekiddo.models.StoredModel
import javax.inject.Inject

@Database(entities = [StoredModel::class], version = 1,  exportSchema = false)
abstract class ProjectDatabase  : RoomDatabase() {

    abstract fun cachedDao(): StoredDao

    private var INSTANCE: ProjectDatabase? = null

    private val sLock = Any()

    fun getInstance(context: Context): ProjectDatabase {
        if (INSTANCE == null) {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            ProjectDatabase::class.java, Constants.DATABASE)
                            .build()
                }
                return INSTANCE!!
            }
        }
        return INSTANCE!!
    }

}