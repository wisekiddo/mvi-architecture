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

package com.wisekiddo.domain.interactor

import com.wisekiddo.domain.executor.PostExecutionThread
import com.wisekiddo.domain.executor.ThreadExecutor
import com.wisekiddo.domain.model.DomainModel
import com.wisekiddo.domain.repository.DomainRepository
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Use case used for retrieving a [List] of [DomainModel] instances from the [DomainRepository]
 */
open class GetDataList @Inject constructor(val domainRepository: DomainRepository,
                                            threadExecutor: ThreadExecutor,
                                            postExecutionThread: PostExecutionThread
):
        FlowableUseCase<List<DomainModel>, Void?>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseObservable(params: Void?): Flowable<List<DomainModel>> {
        return domainRepository.getDataList()
    }

}