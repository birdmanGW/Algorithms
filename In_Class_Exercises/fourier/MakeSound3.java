
public class MakeSound3 {

    public static void main (String[] argv)
    {
        
	double freq = 261.63;          // Middle C = 261.63Hz.
	int samplesPerSecond = 8000;   // For comparison: audio CD uses 44100 samples/second.
	int playTimeSeconds = 3;       

	// This makes the samples (digitized sound).
	byte[] samples = SignalProcUtil.makeSampledSound (freq, samplesPerSecond, playTimeSeconds);

	// Plot first 100 values.
	Function F = new Function ("F values");
	for (int i=0; i<100; i++) {
	    F.add (i, samples[i]);
	}
	F.show ();
    }

}