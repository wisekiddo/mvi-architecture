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
import androidx.appcompat.app.AppCompatActivity
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
import com.wisekiddo.application.base.BaseView
import com.wisekiddo.application.mapper.PresentationStreamMapper
import com.wisekiddo.models.DataViewModel
import com.wisekiddo.presentation.MainDataViewModel
import com.wisekiddo.presentation.MainIntent
import com.wisekiddo.presentation.MainUIModel
import com.wisekiddo.widgets.loaders.CurvesLoader
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    BaseView<MainIntent, MainUIModel> {

    @Inject
    lateinit var mapper: PresentationStreamMapper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainDataViewModel: MainDataViewModel
    private lateinit var spGender:Spinner
    private lateinit var tvUserId: TextInputEditText
    private lateinit var tvMultiplier: TextInputEditText
    private lateinit var btnQuery:Button

    private lateinit var progressBar: CurvesLoader

    private val selectedQuery = DataViewModel(gender = "Male")

    private val loadConversationsIntentPublisher =
        BehaviorSubject.create<MainIntent.LoadDataIntent>()
    private val refreshConversationsIntentPublisher =
        BehaviorSubject.create<MainIntent.RefreshDataIntent>()

    private val compositeDisposable = CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        setupViews()

        //-----
        AndroidInjection.inject(this)
        mainDataViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MainDataViewModel::class.java)

        mainDataViewModel.processIntents(intents())

        val option= HashMap<String, String>()
        option["gender"] = selectedQuery.gender?.toLowerCase()?:""
        option["seed"] = selectedQuery.seed?.toLowerCase()?:""
        //option["gender"] = "male"
        compositeDisposable.add(mainDataViewModel.states(option).subscribe {
            render(it)
        })
    }

    private fun setupViews() {
        val spinnerArray = arrayOf("Male", "Female")
        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray)

        spGender = findViewById(R.id.sp_gender)
        spGender.adapter = spinnerArrayAdapter
        spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedQuery.gender = parent?.getItemAtPosition(position).toString()
            }

        }


        tvUserId = findViewById(R.id.te_userId)
        tvMultiplier = findViewById(R.id.te_multiplier)

        btnQuery = findViewById(R.id.btn_query)
        btnQuery.setOnClickListener {
            selectedQuery.seed=tvUserId.text.toString()
            selectedQuery.multiplier=tvUserId.text.toString()

            val option= HashMap<String, String>()
            option["gender"] = selectedQuery.gender?.toLowerCase()?:""
            option["seed"] = selectedQuery.seed?.toLowerCase()?:""
            //option["gender"] = "male"
            compositeDisposable.add(mainDataViewModel.states(option).subscribe {
                render(it)
            })
        }

        progressBar= findViewById(R.id.progress_bar)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun intents(): Observable<MainIntent> {
        return Observable.merge(initialIntent(), loadConversationsIntentPublisher,
            refreshConversationsIntentPublisher)
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
                Snackbar.make(window.decorView.rootView, "An issue occur on retrieving the data", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            }
            state is MainUIModel.Success -> {
                progressBar.visibility = View.GONE

                Log.i("LIST",state.dataList.toString())
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
