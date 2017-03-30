
public class MakeSound2 {

    public static void main (String[] argv)
    {
        
	double freq = 261.63;          // Middle C = 261.63Hz.
	int samplesPerSecond = 8000;   // For comparison: audio CD uses 44100 samples/second.
	int playTimeSeconds = 3;       

	// This makes the samples (digitized sound).
	byte[] samples = SignalProcUtil.makeSampledSound (freq, samplesPerSecond, playTimeSeconds);

	// INSERT YOUR CODE HERE: print the bytes in the array
	for (int i = 0; i < samples.length; i++)
		System.out.println("\t : " + samples[i] + "\n");

	// Now play the sound.
	SignalProcUtil.playSoundBytes (samples, samplesPerSecond);
    }

}
