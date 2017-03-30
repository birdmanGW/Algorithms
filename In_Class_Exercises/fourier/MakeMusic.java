
public class MakeMusic {

    public static void main (String[] argv)
    {
	int samplesPerSecond = 8000;

	// Example:
	byte[] samples = SignalProcUtil.makeMusic ("EFGABCDE", "EFGABCDE", samplesPerSecond);

	// To make your own music, edit the two strings above. Use any letter among
	// "A' through 'G' (in caps). Both strings have to be the same length.
	// Can you tell the difference between the two strings?
	
	// Now play the sound.
	SignalProcUtil.playSoundBytes (samples, samplesPerSecond);
    }

}