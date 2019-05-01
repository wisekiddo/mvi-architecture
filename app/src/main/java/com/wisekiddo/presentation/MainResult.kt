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

package com.wisekiddo.presentation

import com.wisekiddo.models.DomainModel
import com.wisekiddo.application.base.BaseResult
import com.wisekiddo.enums.TaskStatus


sealed class MainResult : BaseResult {

    class LoadDataTask(val status: TaskStatus,
                       val dataList: List<DomainModel>? = null) :
            MainResult() {

        companion object {

            internal fun success(conversations: List<DomainModel>?): LoadDataTask {
                return LoadDataTask(
                    TaskStatus.SUCCESS,
                    conversations
                )
            }

            internal fun failure(): LoadDataTask {
                return LoadDataTask(TaskStatus.FAILURE, null)
            }

            internal fun inFlight(): LoadDataTask {
                return LoadDataTask(TaskStatus.IN_FLIGHT)
            }
        }
    }

}