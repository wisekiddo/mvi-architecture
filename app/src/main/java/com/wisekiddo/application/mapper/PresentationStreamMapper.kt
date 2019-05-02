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

import com.wisekiddo.models.DomainModel
import com.wisekiddo.models.MainViewModel
import javax.inject.Inject

/**
 * Map a [MainViewModel] to and from a [DomainModel] instance when data is moving between
 * this layer and the Domain layer
 */
open class PresentationStreamMapper @Inject constructor():
    PresentationMapper<MainViewModel, DomainModel> {

    /**
     * Map a [DomainModel] instance to a [MainViewModel] instance
     */
    override fun mapToView(type: DomainModel): MainViewModel {
        return MainViewModel(type.seed, type.name, type.gender, type.age, type.dob, type.email)
    }


}