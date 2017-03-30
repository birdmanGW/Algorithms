
public class TestDFT6 {

    public static void main (String[] argv)
    {
	// 8192 = 2^13 samples per second. 
	int samplesPerSecond = 8192;
	byte[] samples = SignalProcUtil.makeMusic ("CCCC", "CCGG", samplesPerSecond);

	// Window size of N=1024
	int N = 2048;

	int numWindows = samples.length / N;
	System.out.println ("Number of windows: " + numWindows);

	byte[] filteredBytes = new byte [samples.length];

        // Process each window separately.
        for (int w=0; w<numWindows; w++) {
	    // Make space for the complex signal and fill it with N samples in window.
            Complex[] signal = new Complex [N];
            for (int i=0; i<N; i++) {
                signal[i] = new Complex ((double)samples[w*N+i], 0);
            }

	    // Get DFT of signal.
	    Complex[] spectrum = SignalProcUtil.FFT (signal, N);

	    // Filter out frequencies in N/4 to 3N/4 range.
/*	    for (int i=N/4; i<3*N/4; i++) {
		spectrum[i].re = spectrum[i].im = 0;
	    }*/

	    // Convert back to signal domain.
	    Complex[] filteredSignal = SignalProcUtil.inverseFFT (spectrum, N);

            // Copy to filtered bytes.
            byte[] filteredWindow = SignalProcUtil.convertComplexSignalToBytes (filteredSignal);
            for (int i=0; i<N; i++) {
                filteredBytes[w*N+i] = filteredWindow[i];
            }
	    System.out.println ("Processed window w= " + w);
        }

        SignalProcUtil.playSoundBytes (filteredBytes, samplesPerSecond);
	
    }

}