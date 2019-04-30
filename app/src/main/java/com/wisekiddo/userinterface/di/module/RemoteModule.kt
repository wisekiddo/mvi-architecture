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

package com.wisekiddo.userinterface.di.module

import com.wisekiddo.BuildConfig
import com.wisekiddo.data.repository.DataRemote
import com.wisekiddo.remote.RemoteImpl
import com.wisekiddo.remote.Service
import com.wisekiddo.remote.ServiceFactory
import dagger.Binds
import dagger.Module
import dagger.Provides


/**
 * Module that provides all dependencies from the repository package/layer.
 */
@Module
abstract class RemoteModule {

    /**
     * This companion object annotated as a module is necessary in order to provide dependencies
     * statically in case the wrapping module is an abstract class (to use binding)
     */
    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideService(): Service {
            return ServiceFactory.makeService(BuildConfig.DEBUG)
        }
    }

    @Binds
    abstract fun bindRemote(bufferooRemoteImpl: RemoteImpl): DataRemote
}