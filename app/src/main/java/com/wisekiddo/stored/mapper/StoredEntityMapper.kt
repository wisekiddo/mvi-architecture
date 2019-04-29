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

package com.wisekiddo.stored.mapper

import com.wisekiddo.data.model.DataModel
import com.wisekiddo.stored.model.StoredModel
import javax.inject.Inject

/**
 * Map a [StoredModel] instance to and from a [DataModel] instance when data is moving between
 * this later and the DomainModel layer
 */
open class StoredEntityMapper @Inject constructor():
    EntityMapper<StoredModel, DataModel> {

    /**
     * Map a [DataModel] instance to a [StoredModel] instance
     */
    override fun mapToCached(type: DataModel): StoredModel {
        return StoredModel(type.id, type.name, type.title, type.avatar)
    }

    /**
     * Map a [StoredModel] instance to a [DataModel] instance
     */
    override fun mapFromCached(type: StoredModel): DataModel {
        return DataModel(type.id, type.name, type.title, type.avatar)
    }

}