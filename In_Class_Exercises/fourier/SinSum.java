
public class SinSum {

    public static void main (String[] argv)
    {
	Function F1 = new Function ("sin-x");
	Function F2 = new Function ("sin-2x");
	Function Fsum = new Function ("sin-sum");
	for (double x=0; x<=1; x+=0.01) {
	    double y1 = Math.sin (2*Math.PI*x);
	    double y2 = Math.sin (2*Math.PI*2*x);
	    F1.add (x, y1);
	    F2.add (x, y2);
	    Fsum.add (x, y1+y2);
	}
	Function.show (F1, F2, Fsum);
    }

}