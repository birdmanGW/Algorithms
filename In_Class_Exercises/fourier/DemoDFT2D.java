
import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

public class DemoDFT2D extends JFrame {

    // N x N grid or image.
    int N = 5;

    JTextField vField = new JTextField (6);

    // The signal and spectrum are now 2D images or 2D arrays of complex numbers.
    Complex[][] signal, spectrum;

    // We're going to display the real and imaginary parts separately.
    Clickable2DGrid signalRe, signalIm, spectrumRe, spectrumIm;

    // For selection and change of value.
    Clickable2DGrid currentGrid;
    int selecti = -1, selectj = -1;
        

    public DemoDFT2D ()
    {
	// Build the GUI.
        this.setSize (800, 700);
        this.setTitle ("FFT in 2D");
        Container cPane = this.getContentPane();
        cPane.add (makeBottomPanel(), BorderLayout.SOUTH);
        cPane.add (makeCenterPanel(), BorderLayout.CENTER);
        this.setVisible (true);
    }
    

    // Apply() scales the values to the range [-127, 127] so that we can treat the
    // values as greyscale intensities.

    void apply ()
    {
        apply ("Signal", signal, signalRe, signalIm);
        apply ("Spectrum", spectrum, spectrumRe, spectrumIm);
        signalRe.repaint ();
        signalIm.repaint ();
        spectrumRe.repaint ();
        spectrumIm.repaint ();
    }
    
    void apply (String msg, Complex[][] data, Clickable2DGrid realGrid, Clickable2DGrid imGrid)
    {
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                double re = data[i][j].re;
                double im = data[i][j].im;
                if (re < -127) {
                    re = -127;
                }
                if (re > 127) {
                    re = 127;
                }
                if (im < -127) {
                    im = -127;
                }
                if (im > 127) {
                    im = 127;
                }
                realGrid.grid[i][j] = (int) re;
                imGrid.grid[i][j] = (int) im;
            }
        }

    }
    

    void change ()
    {
        try {
            double v = Double.parseDouble (vField.getText());
            if (currentGrid == signalRe) {
                signal[selecti][selectj].re = v;
            }
            else if (currentGrid == signalIm) {
                signal[selecti][selectj].im = v;
            }
            else if (currentGrid == spectrumRe) {
                spectrum[selecti][selectj].re = v;
            }
            else if (currentGrid == spectrumIm) {
                spectrum[selecti][selectj].im = v;
            }
            apply ();
        }
        catch (NumberFormatException e) {
            System.out.println (e);
        }
    }


    void setTextField ()
    {
        double v = 0;
        if (currentGrid == signalRe) {
            v = signal[selecti][selectj].re;
        }
        else if (currentGrid == signalIm) {
            v = signal[selecti][selectj].im;
        }
        else if (currentGrid == spectrumRe) {
            v = spectrum[selecti][selectj].re;
        }
        else if (currentGrid == spectrumIm) {
            v = spectrum[selecti][selectj].im;
        }
        vField.setText ("" + v);
    }

    void showValues ()
    {
	signalRe.showValuesToggle ();
	signalIm.showValuesToggle ();
	spectrumRe.showValuesToggle ();
	spectrumIm.showValuesToggle ();
	apply ();
    }


    void clear ()
    {
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
		signal[i][j].re = signal[i][j].im = 0;
		spectrum[i][j].re = spectrum[i][j].im = 0;
	    }
	}
	apply ();
    }



    ////////////////////////////////////////////////////////////////////////////
    // DFT and inverse.

    void DFT ()
    {
	spectrum = new Complex [N][N];
	for (int u=0; u<N; u++) {
	    for (int v=0; v<N; v++) {

		Complex sum = new Complex (0,0);

		for (int x=0; x<N; x++) {
		    for (int y=0; y<N; y++) {
			double cosTerm = Math.cos (2*Math.PI*(u*x+v*y) / (double)N);
			double sinTerm = -Math.sin (2*Math.PI*(u*x+v*y) / (double)N);
			Complex cTerm = new Complex (cosTerm, sinTerm);
			Complex prod = signal[x][y].mult (cTerm);
			sum = sum.add (prod);
		    }
		}

		Complex term = sum.mult (1.0/(N));
		spectrum[u][v] = term;
	    }
	}

	apply ();
    }
    

    void inverseDFT () 
    {
	signal = new Complex [N][N];

	for (int x=0; x<N; x++) {
	    for (int y=0; y<N; y++) {

		Complex sum = new Complex (0,0);

		for (int u=0; u<N; u++) {
		    for (int v=0; v<N; v++) {
			double cosTerm = Math.cos (2*Math.PI*(u*x+v*y) / (double)N);
			double sinTerm = Math.sin (2*Math.PI*(u*x+v*y) / (double)N);
			Complex cTerm = new Complex (cosTerm, sinTerm);
			Complex prod = spectrum[u][v].mult (cTerm);
			sum = sum.add (prod);
		    }
		}

		Complex term = sum.mult (1.0/(N));
		signal[x][y] = term;
	    }
	}

	apply ();
    }
    

    ////////////////////////////////////////////////////////////////////////////
    // GUI construction.

    JPanel makeBottomPanel ()
    {
        JPanel panel = new JPanel ();
        
        panel.add (new JLabel ("Value: "));
        panel.add (vField);

	JButton changeB = new JButton ("Change");
	changeB.addActionListener (
	   new ActionListener () {
		   public void actionPerformed (ActionEvent a)
		   {
		       change ();
		   }
           }
        );
	panel.add (changeB);

        panel.add (new JLabel ("       "));
        

	JButton invB = new JButton ("invDFT");
	invB.addActionListener (
	   new ActionListener () {
		   public void actionPerformed (ActionEvent a)
		   {
		       inverseDFT ();
		   }
           }
        );
	panel.add (invB);

	JButton dftB = new JButton ("DFT");
	dftB.addActionListener (
	   new ActionListener () {
		   public void actionPerformed (ActionEvent a)
		   {
		       DFT ();
		   }
           }
        );
	panel.add (dftB);


	JButton showB = new JButton ("ShowValues");
	showB.addActionListener (
	   new ActionListener () {
		   public void actionPerformed (ActionEvent a)
		   {
		       showValues ();
		   }
           }
        );
	panel.add (showB);

        panel.add (new JLabel ("  "));
	JButton clearB = new JButton ("Clear");
	clearB.addActionListener (
	   new ActionListener () {
		   public void actionPerformed (ActionEvent a)
		   {
		       clear ();
		   }
           }
        );
	panel.add (clearB);

        panel.add (new JLabel ("  "));
	JButton quitB = new JButton ("Quit");
	quitB.addActionListener (
	   new ActionListener () {
		   public void actionPerformed (ActionEvent a)
		   {
		       System.exit(0);
		   }
           }
        );
	panel.add (quitB);
        
        return panel;
    }
    

    JPanel makeCenterPanel ()
    {
        JPanel panel = new JPanel ();
        panel.setLayout (new GridLayout (2,2));
        //panel.setLayout (new BorderLayout());
        
        signal = new Complex [N][N];
        spectrum = new Complex [N][N];
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                signal[i][j] = new Complex (0,0);
                spectrum[i][j] = new Complex (0,0);
            }
        }

        signalRe = new Clickable2DGrid (this, N, N);
        signalRe.setBorder (BorderFactory.createLineBorder (Color.BLACK));
        signalIm = new Clickable2DGrid (this, N, N);
        signalIm.setBorder (BorderFactory.createLineBorder (Color.BLACK));
        spectrumRe = new Clickable2DGrid (this, N, N);
        spectrumRe.setBorder (BorderFactory.createLineBorder (Color.BLACK));
        spectrumIm = new Clickable2DGrid (this, N, N);
        spectrumIm.setBorder (BorderFactory.createLineBorder (Color.BLACK));

        panel.add (signalRe);
        panel.add (signalIm);
        panel.add (spectrumRe);
        panel.add (spectrumIm);
        return panel;
    }
    


    public static void main (String[] argv)
    {
        new DemoDFT2D ();
    }
    

}

