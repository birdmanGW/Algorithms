
public class TestDFT3 {

    public static void main (String[] argv)
    {
	int N = 64;
	int freq = 1;              // Try different frequencies.
	int C = 10;
	boolean isSine = true;     // Change to false for "cosine".

        // Build the signal.
	Complex[] signal = SignalProcUtil.makeComplexSinusoid (N, freq, 10, isSine);

	// Plot signal and then spectrum.
	SignalProcUtil.plotComplex ("Signal", signal);
	Complex[] spectrum = SignalProcUtil.DFT (signal, N);
	SignalProcUtil.plotComplex ("Spectrum", spectrum);
    }

}
