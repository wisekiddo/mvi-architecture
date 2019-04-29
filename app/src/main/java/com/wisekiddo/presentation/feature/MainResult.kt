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

package com.wisekiddo.presentation.feature

import com.wisekiddo.domain.model.DomainModel
import com.wisekiddo.presentation.base.BaseResult
import com.wisekiddo.presentation.enums.TaskStatus


sealed class MainResult : BaseResult {

    class LoadBufferoosTask(val status: TaskStatus,
                            val dataList: List<DomainModel>? = null) :
            MainResult() {

        companion object {

            internal fun success(conversations: List<DomainModel>?): LoadBufferoosTask {
                return LoadBufferoosTask(TaskStatus.SUCCESS, conversations)
            }

            internal fun failure(): LoadBufferoosTask {
                return LoadBufferoosTask(TaskStatus.FAILURE, null)
            }

            internal fun inFlight(): LoadBufferoosTask {
                return LoadBufferoosTask(TaskStatus.IN_FLIGHT)
            }
        }
    }

}