
public class MakeMusic2 {

    public static void main (String[] argv)
    {
	int samplesPerSecond = 8000;

	// Example:
	byte[] samples = SignalProcUtil.makeMusic ("C", "C", samplesPerSecond);

	// Plot first 100 values.
	Function F = new Function ("F values");
	for (int i=0; i<100; i++) {
	    F.add (i, samples[i]);
	}
	F.show ();
    }

}