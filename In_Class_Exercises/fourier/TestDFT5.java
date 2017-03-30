
public class TestDFT5 {

    public static void main (String[] argv)
    {
	int N = 64;
	int freq = 1;
	int C = 10;
	boolean isSine = true;
	Complex[] signal = SignalProcUtil.makeComplexSinusoid (N, freq, 10, isSine);

	// A second one of different, and high, frequency.
	freq = N/4+1;
	Complex[] signal2 = SignalProcUtil.makeComplexSinusoid (N, freq, 10, isSine);

	// Add.
	for (int i=0; i<N; i++) {
	    signal[i] = signal[i].add (signal2[i]);
	}

	// Plot signal and then spectrum.
	SignalProcUtil.plotComplex ("Signal", signal);
	Complex[] spectrum = SignalProcUtil.DFT (signal, N);
	SignalProcUtil.plotComplex ("Spectrum", spectrum);

	// Now screen out high frequencies in the spectrum.
	for (int i=N/4; i<=3*N/4; i++) {
	    spectrum[i] = new Complex (0,0);
	}
	SignalProcUtil.plotComplex ("Altered spectrum", spectrum);
	Complex[] filteredSignal = SignalProcUtil.inverseDFT (spectrum, N);
	SignalProcUtil.plotComplex ("Filtered signal", filteredSignal);
	
    }

}