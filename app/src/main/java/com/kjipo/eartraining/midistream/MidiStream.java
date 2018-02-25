package com.kjipo.eartraining.midistream;

import android.content.Context;

import com.kjipo.eartraining.midi.MidiMessages;
import com.kjipo.eartraining.midi.MidiPlayerInterface;

import java.io.IOException;


public class MidiStream implements MidiPlayerInterface {
    private Synthesizer synthesizer;


    @Override
    public void noteOn(int pitch) {
        byte command[] = new byte[3];
        command[0] = (byte)MidiMessages.NOTE_ON.message;
        command[1] = (byte)pitch;
        command[2] = 127;
        try {
            // TODO Not necessary to wait here? Put on a different thread
            synthesizer.send(command, 0, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void noteOff(int pitch) {
        byte command[] = new byte[3];
        command[0] = (byte)MidiMessages.NOTE_OFF.message;
        command[1] = (byte)pitch;
        command[2] = 127;
        try {
            // TODO Not necessary to wait here? Put on a different thread
            synthesizer.send(command, 0, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setup(Context applicationContext) {
        synthesizer = new Synthesizer();


//        MidiManager midiManager = (MidiManager) applicationContext.getSystemService(MIDI_SERVICE);
//        MidiDeviceInfo[] infos = midiManager.getDevices();
//
//        Log.i("Test", "MIDI devices2: " + infos.length);
//
//        MidiDeviceInfo midiDeviceInfo = null;
//        for (MidiDeviceInfo info: infos) {
//            Bundle properties = info.getProperties();
//            if("Mobileer".equals(properties.get(MidiDeviceInfo.PROPERTY_MANUFACTURER))
//                && "SynthExample".equals(properties.get(MidiDeviceInfo.PROPERTY_PRODUCT))) {
//                midiDeviceInfo = info;
//                break;
//            }
//        }
//
//        if(midiDeviceInfo == null) {
//            Log.e("Midi", "MIDI device not found");
//            return;
//        }


//        CountDownLatch latch = new CountDownLatch(1);

//        midiManager.openDevice(midiDeviceInfo, new MidiManager.OnDeviceOpenedListener() {
//            @Override
//            public void onDeviceOpened(MidiDevice device) {
//                Log.i("Midi", "Opened device: " +device);
//
//                midiReceiver = device.openInputPort(0);
//
//                midiDeviceReference.set(device);
//                MainActivity.this.playMidi(device);
//            }
//        }, new Handler(Looper.getMainLooper()));






    }

    @Override
    public void start() {
        synthesizer.start();
    }

    @Override
    public void stop() {
        synthesizer.stop();
    }
}
