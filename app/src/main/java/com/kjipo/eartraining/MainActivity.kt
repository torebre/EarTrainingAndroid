package com.kjipo.eartraining


import android.arch.lifecycle.LifecycleRegistry
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    val lifecycleRegistry = LifecycleRegistry(this)

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var navigationController: NavigationController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            Toast.makeText(this@MainActivity, "No MIDI support", Toast.LENGTH_LONG).show()
        }

        setContentView(R.layout.main_act)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val earTrainingButton = findViewById<Button>(R.id.earTraining)
        earTrainingButton.setOnClickListener { navigationController.startScoreActivity() }
    }


    public override fun onStop() {
        super.onStop()
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return dispatchingAndroidInjector
    }
}
