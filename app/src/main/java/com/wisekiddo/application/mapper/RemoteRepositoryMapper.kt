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

package com.wisekiddo.application.mapper

import com.wisekiddo.models.RepositoryModel
import com.wisekiddo.models.RemoteModel
import javax.inject.Inject

/**
 * Map a [RemoteModel] to and from a [RepositoryModel] instance when data is moving between
 * this later and the DomainModel layer
 */
open class RemoteRepositoryMapper @Inject constructor() :
    RemoteMapper<RemoteModel, RepositoryModel> {

    /**
     * Map an instance of a [RemoteModel] to a [RepositoryModel] model
     */
    override fun mapFromRemote(type: RemoteModel): RepositoryModel {
        return RepositoryModel(type.id, type.name, type.title, type.avatar)
    }

}