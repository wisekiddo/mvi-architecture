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
 * Interface defining methods for the data operations.
 * This is to be implemented by external data source layers, setting the requirements for the
 * operations that need to be implemented
 */
interface DataSource {
    fun clearDataList(): Completable
    fun saveDatList(repositoryList: List<RepositoryModel>): Completable
    fun getDataList(): Flowable<List<RepositoryModel>>
    fun isCached(): Single<Boolean>
}