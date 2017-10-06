package com.kjipo.eartraining;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kjipo.eartraining.adapter.TransitionListenerAdapter;
import com.kjipo.eartraining.eartrainer.EarTrainer;
import com.kjipo.eartraining.eartrainer.EarTrainerUtilities;
import com.kjipo.eartraining.helper.TransitionHelper;
import com.kjipo.eartraining.midi.MidiPlayerInterface;
import com.kjipo.eartraining.score.ScoreActivity;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btnScore;

    @Inject
    EarTrainer earTrainer;
    @Inject
    MidiPlayerInterface midiPlayer;

    private NoteViewFragment noteViewFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((EarTrainingApplication) getApplication()).getMainComponent().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            Toast.makeText(MainActivity.this, "No MIDI support", Toast.LENGTH_LONG).show();
        }

        noteViewFragment = new NoteViewFragment();
        setupSequenceGenerator();

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

        btnScore = (Button) findViewById(R.id.btnScore);
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.getWindow().getSharedElementExitTransition().addListener(
                        new TransitionListenerAdapter() {
                            @Override
                            public void onTransitionEnd(Transition transition) {
                                MainActivity.this.finish();
                            }
                        });

                final Pair[] pairs = TransitionHelper.createSafeTransitionParticipants(MainActivity.this, true,
                        new Pair<>(view, MainActivity.this.getString(R.string.transition_score)));
                @SuppressWarnings("unchecked")
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(MainActivity.this, pairs);
                ScoreActivity.start(MainActivity.this, activityOptions);
            }
        });

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

        midiPlayer.setup(getApplicationContext());

    }


    @Override
    public void onStop() {
        super.onStop();

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


//    @Override
//    public void setTitle(CharSequence title) {
//        actionBarTitle = title;
//        getSupportActionBar().setTitle(title);
//    }


    public void playSequence(View view) {
        // TODO

    }

    public void generateSequence(View view) {
        noteViewFragment.loadNoteSequence(EarTrainerUtilities.transformToJson(earTrainer.generateNextSequence()));
    }


}
