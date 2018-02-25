package com.kjipo.eartraining.midi;


public class SequenceTransformationTest {
    public static final int MILLISECONDS_PER_QUARTER_NOTE = 1000;


    /*
    @Test
    public void encodeSequenceTest() throws IOException {
        Sequence sequence = createTestSequence();
        byte midiData[] = MidiUtilities.transformSequenceToMidiFormat(sequence);

        byte tempo[] = MidiTrackBuilder.splitLengthIntoBytes(sequence.getTempoInMillisecondsPerQuarterNote());

        int counter = 0;
        for(byte b : midiData) {
            System.out.print(b +" ");
            ++counter;
            if(counter % 10 == 0) {
                System.out.println();
            }

        }


        // TODO Check why the start is at 22

        Assert.assertEquals(Arrays.copyOfRange(midiData, 22, 29), new byte[]{
                // Tempo
                0x00, (byte) 0xFF, 0x51, 0x03, tempo[1], tempo[2], tempo[3]});

    }


    private static Sequence createTestSequence() {
        EventSeries eventSeries = EventSeriesFactoryProvider.getEventSeriesFactory().createEmptySeries();
        Map<Event, Note> eventSeqMapping = new HashMap<Event, Note>();
        ElementType[] validElements = new ElementType[] {ElementType.QUARTERNOTE, ElementType.HALFNOTE};
        List<Note> notes = new ArrayList<Note>();
        int cumulativeDurationWithinBar = 0;
        int id = 0;
        Random randomizer = new Random();

        for (int i = 0; i < 10; ++i) {
            ElementType type = validElements[randomizer.nextInt(validElements.length)];
            int pitch = 60 + randomizer.nextInt(11);
            int duration = -1;
            switch (type) {
                case QUARTERNOTE:
                    duration = 1;
                    break;

                case HALFNOTE:
                    duration = 2;
                    break;
            }
            Event event = eventSeries.addEvent(i, duration);
            Note note = new Note(id++, pitch, cumulativeDurationWithinBar, type, duration);
            cumulativeDurationWithinBar += duration;
            notes.add(note);

            if(cumulativeDurationWithinBar % 4 == 0) {
                notes.add(new Note(id++, -1, 0, ElementType.BAR_LINE, duration));
                cumulativeDurationWithinBar = 0;
            }

            eventSeqMapping.put(event, note);
        }

        // TODO The duration of bar parameter should be calculated
        return new Sequence(ClefType.TREBLE, 4, 4, notes, 4, MILLISECONDS_PER_QUARTER_NOTE);
    }
    */


}
