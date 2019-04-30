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

import androidx.lifecycle.ViewModel
import com.wisekiddo.base.BaseIntent
import com.wisekiddo.base.BaseViewModel
import com.wisekiddo.enums.TaskStatus
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

open class MainDataViewModel @Inject internal constructor(
    private val mainProcessor: MainProcessor,
    private val mainMapper: MainMapper
)
    : ViewModel(), BaseViewModel<MainIntent, MainUIModel> {

    private var intentsSubject: PublishSubject<MainIntent> = PublishSubject.create()
    private val intentFilter: ObservableTransformer<MainIntent, MainIntent> =
            ObservableTransformer<MainIntent, MainIntent> {
                it.publish {
                    Observable.merge(it.ofType(MainIntent.InitialIntent::class.java).take(1),
                            it.filter({ intent -> intent !is MainIntent.InitialIntent }))
                }
            }
    private val reducer: BiFunction<MainUIModel, MainResult, MainUIModel> =
            BiFunction<MainUIModel, MainResult, MainUIModel> { _, result ->
                when (result) {
                    is MainResult.LoadBufferoosTask -> {
                        when {
                            result.status == TaskStatus.SUCCESS -> MainUIModel.Success(
                                result.dataList?.map { mainMapper.mapToView(it) })
                            result.status == TaskStatus.FAILURE -> MainUIModel.Failed
                            result.status == TaskStatus.IN_FLIGHT -> MainUIModel.InProgress
                            else -> MainUIModel.Idle()
                        }
                    }
                }
            }
    private val statesSubject: Observable<MainUIModel> = compose()

    override fun processIntents(intents: Observable<MainIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<MainUIModel> {
        return statesSubject
    }

    private fun compose(): Observable<MainUIModel> {
        return intentsSubject
                .compose(intentFilter)
                .map { this.actionFromIntent(it) }
                .compose(mainProcessor.actionProcessor)
                .scan<MainUIModel>(MainUIModel.Idle(), reducer)
                .replay(1)
                .autoConnect(0)
    }

    private fun actionFromIntent(intent: BaseIntent): MainAction {
        return when (intent) {
            is MainIntent.LoadDataIntent -> MainAction.LoadData
            is MainIntent.RefreshDataIntent -> MainAction.LoadData
            is MainIntent.InitialIntent -> MainAction.LoadData
            else -> throw UnsupportedOperationException(
                    "Oops, that looks like an unknown intent: " + intent)
        }
    }

}