
import java.util.*;
import java.io.*;

public class Debug {

  static PrintWriter debugWriter;
  static boolean isSet;
  static String defaultFileName = "debug.data";
  
  static {
    isSet = false;
  }


  public static void setOutputFile (boolean useFile, String dirName, String fileName)
  {
    // System.out.println ("Debug.setOutputFile: u=" + useFile + " d=" + dirName + " f=" + fileName);

    
    
    if (isSet)
      return ;
    
    if ( (!useFile) || (fileName == null) ) {
      debugWriter = new PrintWriter (System.out);
    }
    else if (dirName != null) {
      try {
        debugWriter = new PrintWriter (new FileWriter (dirName + "/" + fileName));
      }
      catch (IOException e) {
        System.out.println ("ERR: Debug.setOutputFile1: could not open debug file=" + fileName);
        System.exit (1);
      }
    }
    else {
      try {
        debugWriter = new PrintWriter (new FileWriter (fileName));
      }
      catch (IOException e) {
        System.out.println ("ERR: Debug.setOutputFile2: could not open debug file=" + fileName);
        System.exit (1);
      }
    }
    
    isSet = true;
    
  }
  
  public static boolean isSet ()
  {
    return isSet;
  }
  
  public static void print (String str)
  {
    if (!isSet())
      setOutputFile (true, null, defaultFileName);
    debugWriter.print (str);
  }
  
  public static void println (String str)
  {
    if (!isSet())
      setOutputFile (true, null, defaultFileName);
    debugWriter.println (str);
    debugWriter.flush();
  }

  public static void print (Object obj)
  {
    print (obj.toString());
  }
  
  public static void println (Object obj)
  {
    println (obj.toString());
  }
  
  
  public static void error (String str)
  {
    System.out.println ("ERROR: " + str);
    println (str);
  }
  

  public static void write (String str)
  {
    if (!isSet())
      setOutputFile (true, null, defaultFileName);
    debugWriter.print (str);
  }
  
  public static void writeln (String str)
  {
    if (!isSet())
      setOutputFile (true, null, defaultFileName);
    debugWriter.println (str);
    debugWriter.flush();
  }
  
  public static void close ()
  {
    if (!isSet())
      return;
    debugWriter.close();
  }
  
  public static PrintWriter getPrintWriter ()
  {
    if (!isSet())
      return null;
    return debugWriter;
  }
}
