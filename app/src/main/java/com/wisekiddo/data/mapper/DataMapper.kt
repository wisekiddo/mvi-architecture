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

package com.wisekiddo.data.mapper

import com.wisekiddo.data.model.DataModel
import com.wisekiddo.domain.model.DomainModel

import javax.inject.Inject


/**
 * Map a [DataModel] to and from a [DomainModel] instance when data is moving between
 * this later and the Domain layer
 */
open class DataMapper @Inject constructor() : EntityMapper<DataModel, DomainModel> {

    /**
     * Map a [DataModel] instance to a [DomainModel] instance
     */
    override fun mapFromEntity(type: DataModel): DomainModel {
        return DomainModel(type.id, type.name, type.title, type.avatar)
    }

    /**
     * Map a [DomainModel] instance to a [DataModel] instance
     */
    override fun mapToEntity(type: DomainModel): DataModel {
        return DataModel(type.id, type.name, type.title, type.avatar)
    }


}