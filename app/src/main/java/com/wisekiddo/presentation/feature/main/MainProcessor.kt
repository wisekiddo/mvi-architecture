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

package com.wisekiddo.presentation.feature.main

import com.wisekiddo.streams.GetDataList
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class MainProcessor @Inject constructor(private val getDataList: GetDataList) {

    private val conversationsProcessor: ObservableTransformer<
            MainAction.LoadData, MainResult> = ObservableTransformer{
                it.switchMap {
                    getDataList.execute()
                            .map {
                                MainResult.LoadDataTask.success(
                                    it
                                )
                            }
                            .onErrorReturn {
                                MainResult.LoadDataTask.failure()
                            }
                            .toObservable()
                            .startWith(MainResult.LoadDataTask.inFlight())
                }
            }

    var actionProcessor: ObservableTransformer<MainAction, MainResult>

    init {
        this.actionProcessor = ObservableTransformer { item ->
            item.publish {
                it.ofType(MainAction.LoadData::class.java)
                        .compose(conversationsProcessor)
                        .mergeWith(it.filter { it !is MainAction.LoadData }
                            .flatMap {
                                    Observable.error<MainResult>(
                                            IllegalArgumentException("Unknown Action type"))
                                })
            }
        }
    }

}