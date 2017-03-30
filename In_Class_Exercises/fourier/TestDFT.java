
public class TestDFT {

    public static void main (String[] argv)
    {
	// Create a blank (zero) spectrum.
	int N = 8;
	Complex[] spectrum = new Complex [N];
	for (int k=0; k<N; k++) {
	    spectrum[k] = new Complex (0,0);
	}

	// Set only one frequency to have non-zero content.
	double C = 10;
	int k = 6;
	spectrum[k] = new Complex (C, 0);

	// Plot spectrum and then signal.
	SignalProcUtil.plotComplex ("Spectrum", spectrum);
	Complex[] signal = SignalProcUtil.inverseDFT (spectrum, N);
	SignalProcUtil.plotComplex ("Signal", signal);
    }

}
