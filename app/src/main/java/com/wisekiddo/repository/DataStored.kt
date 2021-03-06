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

package com.wisekiddo.repository

import com.wisekiddo.models.RepositoryModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


/**
 * Interface defining methods for the caching of data. This is to be implemented by the
 * cache layer, using this interface as a way of communicating.
 */
interface DataStored {

    /**
     * Clear all data from the cache.
     */
    fun clearDataList(): Completable

    /**
     * Save a given list of data to the cache.
     */
    fun saveDataList(repositoryList: List<RepositoryModel>): Completable

    /**
     * Retrieve a list of data, from the cache.
     */
    fun getDataList(): Flowable<List<RepositoryModel>>

    /**
     * Check whether there is a list of data stored in the cache.
     * @return true if the list is cached, otherwise false
     */
    fun isCached(): Single<Boolean>

    /**
     * Set a point in time at when the cache was last updated.
     * @param lastCache the point in time at when the cache was last updated
     */
    fun setLastCacheTime(lastCache: Long)

    /**
     * Check if the cache is expired.
     * @return true if the cache is expired, otherwise false
     */
    fun isExpired(): Boolean

}