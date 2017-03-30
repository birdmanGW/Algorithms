
public class TestSound {

    public static void main (String[] argv)
    {
        
	double freq = 261.63;          // Middle C = 261.63Hz.
	int samplesPerSecond = 8000;   // For comparison: audio CD uses 44100 samples/second.
	int playTimeSeconds = 5;       

	// This makes the samples (digitized sound).
	byte[] samples = SignalProcUtil.makeSampledSound (freq, samplesPerSecond, playTimeSeconds);

	// Now play the sound.
	SignalProcUtil.playSoundBytes (samples, samplesPerSecond);
    }

}