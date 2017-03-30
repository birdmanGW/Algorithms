public class PlotSin {

    public static void main (String[] argv)
    {
        Function F = new Function ("sin");
	    Function G = new Function ("cos");
        
        for (double x=0; x<=20; x+=0.2) {
            F.add (x, Math.sin(x));
	        G.add (x, Math.cos(x));
        }

        Function.show (F, G);
    }
    
}
