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

package com.wisekiddo.application.module

import android.app.Application
import androidx.room.Room
import com.wisekiddo.repository.DataStored
import com.wisekiddo.stored.StoredImpl
import com.wisekiddo.stored.db.Constants
import com.wisekiddo.stored.db.ProjectDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Module that provides all dependencies from the cache package/layer.
 */
@Module
abstract class StoredModule {

    /**
     * This companion object annotated as a module is necessary in order to provide dependencies
     * statically in case the wrapping module is an abstract class (to use binding)
     */
    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideDataDatabase(application: Application): ProjectDatabase {
            return Room.databaseBuilder(
                    application.applicationContext,
                ProjectDatabase::class.java, Constants.DATABASE)
                    .build()
        }
    }

    @Binds
    abstract fun bindDataCache(storedImpl: StoredImpl): DataStored
}