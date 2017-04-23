package com.kjipo.eartrainingandroid;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kjipo.eartrainingandroid.eartrainer.EarTrainer;
import com.kjipo.eartrainingandroid.eartrainer.EarTrainerUtilities;
import com.kjipo.eartrainingandroid.midi.MidiPlayer;
import com.kjipo.eartrainingandroid.midi.MidiUtilities;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String navigationDrawerItems[];
    @Inject
    EarTrainer earTrainer;
    @Inject
    MidiPlayer midiPlayer;

    private MediaPlayer mediaPlayer;

    private NoteViewFragment noteViewFragment;


    private CharSequence actionBarTitle = "";
    private CharSequence title;

    private final static int SCORE_FRAGMENT_POSITION = 0;
    private final static int NOTEVIEW_FRAGMENT_POSITION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteViewFragment = new NoteViewFragment();

        ((EarTrainingApplication) getApplication()).inject(this);


        setupSequenceGenerator();

        title = actionBarTitle = getTitle();
        drawerLayout = (DrawerLayout)findViewById(R.id.main_screen);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        navigationDrawerItems = getResources().getStringArray(R.array.options_array);

        drawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, navigationDrawerItems));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
//                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                // TODO
                getSupportActionBar().setTitle("Test"); // actionBarTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

    }

    private void setupSequenceGenerator() {
        // TODO Populate the sequence generator with previous history
        earTrainer.generateNextSequence();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_websearch, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
//        // Handle action buttons
//        switch(item.getItemId()) {
//            case R.id.action_websearch:
//                // create intent to perform web search for this planet
//                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
//                // catch event that there's no activity to handle intent
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
//                }
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }

        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        MidiManager m = (MidiManager)getApplicationContext().getSystemService(Context.MIDI_SERVICE);
        MidiDeviceInfo[] infos = m.getDevices();

        Log.i("Test", "MIDI devices2: " +infos.length);
        for(MidiDeviceInfo midiDeviceInfo : infos) {
            Log.i("Test", midiDeviceInfo.toString());
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        if(mediaPlayer != null) {
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


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        Fragment fragment = null;

        switch(position) {
            case NOTEVIEW_FRAGMENT_POSITION:
                fragment = noteViewFragment;
                break;

            case SCORE_FRAGMENT_POSITION:
                fragment = new ScoreFragment();
                break;

            default:
                // TODO

                break;
        }

//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        Log.i("Test", "Replacing with: " +fragment);

        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, fragment);

////        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        drawerList.setItemChecked(position, true);
        setTitle(navigationDrawerItems[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        actionBarTitle = title;
        getSupportActionBar().setTitle(title);
    }



    public void playSequence(View view) {
        try {


            System.out.println("Sequence to play: " +earTrainer.getCurrentSequence());

            System.out.println("Sequence as JSON: " +MidiUtilities.transformSequenceToJson(earTrainer.getCurrentSequence()));

            midiPlayer.playSequence(MidiUtilities.transformSequenceToMidiFormat(earTrainer.getCurrentSequence()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void generateSequence(View view) {
        noteViewFragment.loadNoteSequence(EarTrainerUtilities.transformToJson(earTrainer.generateNextSequence()));
    }




}
