
public class TestDFT4 {

    public static void main (String[] argv)
    {
	int N = 64;
	int freq = 1;
	int C = 10;
	boolean isSine = true;
	Complex[] signal = SignalProcUtil.makeComplexSinusoid (N, freq, 10, isSine);

	// A second one of different frequency.
	freq = 2;
	Complex[] signal2 = SignalProcUtil.makeComplexSinusoid (N, freq, 10, isSine);

	// Add.
	for (int i=0; i<N; i++) {
	    signal[i] = signal[i].add (signal2[i]);
	}

	// Plot signal and then spectrum.
	SignalProcUtil.plotComplex ("Signal", signal);
	Complex[] spectrum = SignalProcUtil.DFT (signal, N);
	SignalProcUtil.plotComplex ("Spectrum", spectrum);
    }

}