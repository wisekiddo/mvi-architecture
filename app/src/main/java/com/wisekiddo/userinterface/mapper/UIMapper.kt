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

package com.wisekiddo.userinterface.mapper

import com.wisekiddo.presentation.feature.MainView
import com.wisekiddo.userinterface.model.DataViewModel

import javax.inject.Inject

/**
 * Map a [MainView] to and from a [DataViewModel] instance when data is moving between
 * this layer and the Domain layer
 */
open class UIMapper @Inject constructor(): Mapper<DataViewModel, MainView> {

    /**
     * Map a [MainView] instance to a [DataViewModel] instance
     */
    override fun mapToViewModel(type: MainView): DataViewModel {
        return DataViewModel(type.name, type.title, type.avatar)
    }

}