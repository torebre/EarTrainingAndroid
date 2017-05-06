package com.kjipo.eartrainingandroid;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.kjipo.eartrainingandroid.eartrainer.EarTrainer;
import com.kjipo.eartrainingandroid.eartrainer.EarTrainerUtilities;
import com.kjipo.eartrainingandroid.midi.MidiPlayerInterface;
import com.kjipo.eartrainingandroid.midi.MidiUtilities;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Inject
    EarTrainer earTrainer;
    @Inject
    MidiPlayerInterface midiPlayer;

    private MediaPlayer mediaPlayer;

    private NoteViewFragment noteViewFragment;


    private CharSequence actionBarTitle = "";
    private CharSequence title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((EarTrainingApplication) getApplication()).getMainComponent().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        noteViewFragment = new NoteViewFragment();
        setupSequenceGenerator();

        title = actionBarTitle = getTitle();

        setContentView(R.layout.main_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void setupSequenceGenerator() {
        // TODO Populate the sequence generator with previous history
        earTrainer.generateNextSequence();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_websearch, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onStart() {
        super.onStart();

        MidiManager m = (MidiManager) getApplicationContext().getSystemService(Context.MIDI_SERVICE);
        MidiDeviceInfo[] infos = m.getDevices();

        Log.i("Test", "MIDI devices2: " + infos.length);
        for (MidiDeviceInfo midiDeviceInfo : infos) {
            Log.i("Test", midiDeviceInfo.toString());
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // TODO Just here to check when this event is triggered

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d("debug", "Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d("debug", "Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d("debug", "Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d("debug", "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d("debug", "Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void setTitle(CharSequence title) {
        actionBarTitle = title;
        getSupportActionBar().setTitle(title);
    }


    public void playSequence(View view) {
        try {


            System.out.println("Sequence to play: " + earTrainer.getCurrentSequence());

            System.out.println("Sequence as JSON: " + MidiUtilities.transformSequenceToJson(earTrainer.getCurrentSequence()));

            midiPlayer.playSequence(MidiUtilities.transformSequenceToMidiFormat(earTrainer.getCurrentSequence()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void generateSequence(View view) {
        noteViewFragment.loadNoteSequence(EarTrainerUtilities.transformToJson(earTrainer.generateNextSequence()));
    }


}
