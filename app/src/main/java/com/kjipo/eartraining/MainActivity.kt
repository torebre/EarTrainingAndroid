package com.kjipo.eartraining


import android.arch.lifecycle.LifecycleRegistry
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Toast
import kotlinx.android.synthetic.main.main_act.*


class MainActivity : AppCompatActivity() {
    private val lifecycleRegistry = LifecycleRegistry(this)
    private var navigationController: NavigationController = NavigationController(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            Toast.makeText(this@MainActivity, "No MIDI support", Toast.LENGTH_LONG).show()
        }

        setContentView(R.layout.main_act)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        earTraining.setOnClickListener { navigationController.startScoreActivity() }
    }


    public override fun onStop() {
        super.onStop()
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

}
