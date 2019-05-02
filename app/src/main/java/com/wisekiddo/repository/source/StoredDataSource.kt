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
import com.wisekiddo.repository.DataStored
import com.wisekiddo.repository.DataSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Implementation of the [StoredDataSource] interface to provide a means of communicating
 * with the local data source
 */
open class StoredDataSource @Inject constructor(private val dataStored: DataStored) :
    DataSource {

    /**
     * Clear all DataList from the cache
     */
    override fun clearDataList(): Completable {
        return dataStored.clearDataList()
    }

    /**
     * Save a given [List] of [RepositoryModel] instances to the cache
     */
    override fun saveDatList(repositoryList: List<RepositoryModel>): Completable {
        return dataStored.saveDataList(repositoryList)
                .doOnComplete {
                    dataStored.setLastCacheTime(System.currentTimeMillis())
                }
    }

    /**
     * Retrieve a list of [RepositoryModel] instance from the cache
     */
    override fun getDataList(options:Map<String, String>): Flowable<List<RepositoryModel>> {
        return dataStored.getDataList()
    }

    /**
     * Retrieve a list of [RepositoryModel] instance from the cache
     */
    override fun isCached(): Single<Boolean> {
        return dataStored.isCached()
    }

}