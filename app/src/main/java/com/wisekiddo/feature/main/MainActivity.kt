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

package com.wisekiddo.feature.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.wisekiddo.R
import com.wisekiddo.application.mapper.PresentationStreamMapper
import com.wisekiddo.feature.base.BaseActivity
import com.wisekiddo.models.DataViewModel
import com.wisekiddo.presentation.MainDataViewModel
import com.wisekiddo.presentation.MainIntent
import com.wisekiddo.presentation.MainUIModel
import com.wisekiddo.utils.MapQuery
import com.wisekiddo.utils.RSAEncrypt
import com.wisekiddo.widgets.loaders.CurvesLoader
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var mapper: PresentationStreamMapper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mainDataViewModel: MainDataViewModel

    private val spinnerArray = arrayOf("Male", "Female")
    private val selectedQuery = DataViewModel(gender = "Male")

    private val compositeDisposable = CompositeDisposable()
    private val loadConversationsIntentPublisher = BehaviorSubject.create<MainIntent.LoadDataIntent>()
    private val refreshConversationsIntentPublisher = BehaviorSubject.create<MainIntent.RefreshDataIntent>()

    private lateinit var toolbar: Toolbar
    private lateinit var spinnerGender: Spinner
    private lateinit var progressBar: CurvesLoader
    private lateinit var textInputUserID: TextInputEditText
    private lateinit var textInputMultiplier: TextInputEditText
    private lateinit var buttonQuery: Button
    private lateinit var fab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var spinnerArrayAdapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        mainDataViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MainDataViewModel::class.java)
        mainDataViewModel.processIntents(intents())


        //-- prepare sample Encryption
        val strPlainText = "RSA Encrypt Text"
        Log.i("RSA_Encrypt_Text", strPlainText)
        val strEncryptedText = String(RSAEncrypt.encrypt(strPlainText))
        Log.i("Encrypted_Text", "\t$strEncryptedText")

        val strDecryptedText = String(RSAEncrypt.decrypt(strEncryptedText))
        Log.i("Decrypted_Text", "\t$strDecryptedText")
    }

    override fun renderView() {
        setContentView(R.layout.activity_main)
    }

    override fun initialize() {
        setupViews()
        setupListeners()
    }

    private fun setupViews() {

        toolbar = findViewById(R.id.toolbar)
        spinnerGender = findViewById(R.id.sp_gender)
        progressBar = findViewById(R.id.progress_bar)
        textInputUserID = findViewById(R.id.te_userId)
        textInputMultiplier = findViewById(R.id.te_multiplier)
        buttonQuery = findViewById(R.id.btn_query)
        fab = findViewById(R.id.fab)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        spinnerArrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArray
        )

        spinnerGender.adapter = spinnerArrayAdapter
        setSupportActionBar(toolbar)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    private fun setupListeners() {

        buttonQuery.setOnClickListener {
            selectedQuery.seed = textInputUserID.text.toString()
            selectedQuery.multiplier = textInputMultiplier.text.toString()

            MapQuery.setQueries("gender", selectedQuery.gender?.toLowerCase() ?: "")
            MapQuery.setQueries("seed", selectedQuery.seed?.toLowerCase() ?: "")

            compositeDisposable.add(mainDataViewModel.states(MapQuery.getQueries()).subscribe {
                render(it)
            })
            refreshConversationsIntentPublisher.onNext(MainIntent.RefreshDataIntent)

        }

        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedQuery.gender = "male"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedQuery.gender = parent?.getItemAtPosition(position).toString()
            }
        }

        fab.setOnClickListener {
            showSnackbar("Hello World!")
        }

    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun intents(): Observable<MainIntent> {
        return Observable.merge(
            initialIntent(), loadConversationsIntentPublisher,
            refreshConversationsIntentPublisher
        )
    }

    private fun initialIntent(): Observable<MainIntent.InitialIntent> {
        return Observable.just(MainIntent.InitialIntent)
    }

    override fun render(state: MainUIModel) {
        when {
            state.inProgress -> {
                progressBar.visibility = View.VISIBLE
            }
            state is MainUIModel.Failed -> {
                compositeDisposable.clear()
                progressBar.visibility = View.GONE
                showSnackbar("An issue occur on retrieving the data")
            }
            state is MainUIModel.Success -> {
                progressBar.visibility = View.GONE

                Log.i("LIST", state.dataList.toString())
            }
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {
                // Nav Gallery
            }
            R.id.nav_slideshow -> {
                // Navigation for slideshow
            }
            R.id.nav_tools -> {
                // Navigation for slideshow
            }
            R.id.nav_share -> {
                // Navigation for slideshow
            }
            R.id.nav_send -> {
                // Navigation for slideshow
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
