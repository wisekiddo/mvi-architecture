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

import com.wisekiddo.application.mapper.RepositoryStreamMapper
import com.wisekiddo.models.RepositoryModel
import com.wisekiddo.repository.source.DataSourceFactory
import com.wisekiddo.models.DomainModel
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Provides an implementation of the [DataRepository] interface for communicating to and from
 * data sources
 */
class DataRepository @Inject constructor(private val factory: DataSourceFactory,
                                         private val repositoryStreamMapper: RepositoryStreamMapper
):
    Repository {

    override fun clearDataList(): Completable {
        return factory.retrieveCacheDataStore().clearDataList()
    }

    override fun saveDataList(dataList: List<DomainModel>): Completable {
        val dataModel = mutableListOf<RepositoryModel>()
        dataList.map { dataModel.add(repositoryStreamMapper.mapToEntity(it)) }
        return factory.retrieveCacheDataStore().saveDatList(dataModel)
    }

    override fun getDataList(options:Map<String, String>): Flowable<List<DomainModel>> {
        return factory.retrieveCacheDataStore().isCached()
                .flatMapPublisher {
                    factory.retrieveDataStore(it).getDataList(options)
                }
                .flatMap {
                    Flowable.just(it.map { repositoryStreamMapper.mapFromEntity(it) })
                }
                .flatMap {
                    saveDataList(it).toSingle { it }.toFlowable()
                }
    }

}