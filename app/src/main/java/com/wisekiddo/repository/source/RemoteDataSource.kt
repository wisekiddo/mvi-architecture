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

package com.wisekiddo.repository.source

import com.wisekiddo.models.RepositoryModel
import com.wisekiddo.repository.DataRemote
import com.wisekiddo.repository.DataSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Implementation of the [RemoteDataSource] interface to provide a means of communicating
 * with the remote data source
 */
open class RemoteDataSource @Inject constructor(private val dataRemote: DataRemote) :
    DataSource {

    override fun clearDataList(): Completable {
        throw UnsupportedOperationException()
    }

    override fun saveDatList(repositoryList: List<RepositoryModel>): Completable {
        throw UnsupportedOperationException()
    }

    /**
     * Retrieve a list of [RepositoryModel] instances from the API
     */
    override fun getDataList(): Flowable<List<RepositoryModel>> {
        return dataRemote.getDataList()
    }

    override fun isCached(): Single<Boolean> {
        throw UnsupportedOperationException()
    }

}