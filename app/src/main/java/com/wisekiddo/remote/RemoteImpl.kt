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

package com.wisekiddo.remote

import com.wisekiddo.data.model.DataModel
import com.wisekiddo.data.repository.DataRemote
import io.reactivex.Flowable
import com.wisekiddo.remote.mapper.RemoteEntityMapper
import javax.inject.Inject

/**
 * Remote implementation for retrieving Data instances. This class implements the
 * [DataRemote] from the DomainModel layer as it is that layers responsibility for defining the
 * operations in which data store implementation layers can carry out.
 */
class RemoteImpl @Inject constructor(
    private val service: Service,
    private val remoteEntityMapper: RemoteEntityMapper
) : DataRemote {

    /**
     * Retrieve a list of [RemoteEntityMapper] instances from the [Service].
     */
    override fun getDataList(): Flowable<List<DataModel>> {
        return service.getData()
            .map { it.result }
            .map { itemList ->
                val entities = mutableListOf<DataModel>()
                itemList.forEach { entities.add(remoteEntityMapper.mapFromRemote(it)) }
                entities
            }
    }

}