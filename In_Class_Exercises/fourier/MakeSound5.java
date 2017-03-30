
public class MakeSound5 {

    public static void main (String[] argv)
    {
	int samplesPerSecond = 8000;   // For comparison: audio CD uses 44100 samples/second.
	int playTimeSeconds = 3;       

	// Create two sets of sounds: second is a harmonic of first (lower C and middle C on piano).
	byte[] samples1 = SignalProcUtil.makeSampledSound (130.81, samplesPerSecond, playTimeSeconds);
	byte[] samples2 = SignalProcUtil.makeSampledSound (261.63, samplesPerSecond, playTimeSeconds);

        // Add them.
	byte[] samples = SignalProcUtil.addSounds (samples1, samples2);

	// Play.
	SignalProcUtil.playSoundBytes (samples, samplesPerSecond);

	// Plot first 100 values of all three.
	Function F1 = new Function ("F1 values");
	Function F2 = new Function ("F3 values");
	Function F3 = new Function ("F3 values");
	for (int i=0; i<100; i++) {
	    F1.add (i, samples1[i]);
	    F2.add (i, samples2[i]);
	    F3.add (i, samples[i]);
	}
	Function.show (F1, F2, F3);
    }

}
