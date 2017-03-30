
public class Sampling {

    public static void main (String[] argv)
    {
	int k = 1;  // For sin(2*PI*k*x)

	Function F1 = new Function ("sin-2*pi*x");
	for (double x=0; x<=1; x+=0.01) {             // Actual sin
	    double y = Math.sin (2*Math.PI*k*x);
	    F1.add (x, y);
	}

	Function F2 = new Function ("sin-samples");
	for (double x=0; x<=1; x+=0.1) {              // 10 equally spaced samples.
	    double y = Math.sin (2*Math.PI*k*x);
	    F2.add (x, y);
	}

	Function.show (F1, F2);

    }

}