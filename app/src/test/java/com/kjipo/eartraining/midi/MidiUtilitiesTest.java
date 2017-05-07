package com.kjipo.eartraining.midi;


import com.kjipo.eartraining.data.ClefType;
import com.kjipo.eartraining.data.ElementType;
import com.kjipo.eartraining.data.Note;
import com.kjipo.eartraining.data.Sequence;
import com.kjipo.eartraining.data.SequenceBuilder;
import com.kjipo.eartraining.eartrainer.EarTrainerImpl;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class MidiUtilitiesTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MidiUtilitiesTest.class);


    @Test
    public void transformToPlayerEventsTest() {
        Sequence sequence = SequenceBuilder.createSequence()
                .setClefType(ClefType.TREBLE)
                .setTempoInMillisecondsPerQuarterNote(1000)
                .setTimeSignatureDenominator(4)
                .setTimeSignatureNominator(4)
                .setDurationOfBar(4)
                .addNote(new Note(1, 60, 1, ElementType.QUARTERNOTE, 4))
                .build();

        List<PlayerEvent> playerEvents = MidiUtilities.transformToPlayerEvents(
                MidiUtilities.transformSequenceToMidiFormat(sequence));


        System.out.println("Player events: " +playerEvents);

        assertThat(playerEvents).isNotEmpty();

    }


    @Test
    public void sequenceTransformedAsExpected() {
        Sequence sequence = SequenceBuilder.createSequence()
                .setClefType(ClefType.TREBLE)
                .setTempoInMillisecondsPerQuarterNote(1000)
                .setTimeSignatureDenominator(4)
                .setTimeSignatureNominator(4)
                .setDurationOfBar(4)
                .addNote(new Note(1, 60, 1, ElementType.QUARTERNOTE, 4))
                .build();

        byte midiData[] = MidiUtilities.transformSequenceToMidiFormat(sequence);

        String hexadecimalValues[] = getBytesAsHexadecimal(midiData);
        for(int i = 0; i < hexadecimalValues.length; ++i) {
            System.out.print(i +": " +hexadecimalValues[i] +", ");
            if(i % 5 == 0) {
                System.out.println();
            }
        }


        assertThat(midiData[37]).isEqualTo((byte)0);
        assertThat(midiData[38]).isEqualTo(MidiMessages.NOTE_ON.getMessageAsByte());


        // 14 bytes

        // Header 4 bytes

        // 4 bytes with length information

        // 15 bytes

//        int deltaTime = 0;
//        PlayerEvent currentPlayerEvent = null;
//        long cumulativeTime = 0;
//
//
//        // TODO For now just jump down to the MIDI messages
//        for (int i = 36; i < midiData.length - 4; ++i) {
////            byte currentByte = midiData[i];
//
//            deltaTime = midiData[i];
//            if (deltaTime != 0) {
//                cumulativeTime += deltaTime;
//                currentPlayerEvent = new PlayerEvent(cumulativeTime);
//                playerEvents.add(currentPlayerEvent);
//            }
//
//            if (midiData[i] == MidiMessages.NOTE_ON.getMessageAsByte()) {
//                byte pitch = midiData[i++];
//                // TODO Use velocity
//                ++i;
//                currentPlayerEvent.addOnPitch(pitch);
//            } else if (midiData[i] == MidiMessages.NOTE_OFF.getMessageAsByte()) {
//                byte pitch = midiData[i++];
//                // TODO Use velocity
//                ++i;
//                currentPlayerEvent.addOffPitch(pitch);
//            }
//        }



    }



    @Test
    public void testTransformSequenceToMidiFormat() {
        EarTrainerImpl earTrainer = new EarTrainerImpl();

        for(int i = 0; i < 100; ++i) {
            Sequence sequence = earTrainer.generateNextSequence();
            try {
                MidiUtilities.transformSequenceToMidiFormat(sequence);
            }
            catch(RuntimeException e) {
                LOGGER.error("Sequence caused exception: {}", sequence);
                throw e;
            }
        }
    }





    private static String[] getBytesAsHexadecimal(byte bytes[]) {
//        byte[] bytes = {-1, 0, 1, 2, 3 };
//        StringBuilder sb = new StringBuilder();
        String result[] = new String[bytes.length];
        int counter = 0;
        for (byte b : bytes) {
//            sb.append(String.format("%02X ", b));
            result[counter++] = String.format("%02X ", b);

        }
//        return sb.toString();
        return result;
    }



}
