package edu.gwu.algtest;

import edu.gwu.debug.Debug;
import edu.gwu.stat.RandomGenerator;
import edu.gwu.stat.RandomGeneratorException;
import edu.gwu.stat.RandomGeneratorFactory;
import edu.gwu.stat.RandomObjectGenerator;
import edu.gwu.util.PropertyExtractor;
import edu.gwu.util.UtilException;
import java.io.PrintStream;
import java.util.Enumeration;

class SearchTester implements GenericTester
{
  public static final String XLABEL = "Data size";
  public static final String YLABEL = "Time";
  public static final String TITLE = "Search Comparison";
  public static final int STRINGSIZE_MIN = 3;
  public static final int STRINGSIZE_AVG = 3;
  public static final char LOWCHAR = 'A';
  public static final char HIGHCHAR = 'Z';
  boolean testLarge = false;
  boolean eqSearch = true;
  boolean advSearch = false;
  boolean testDelete = false;
  boolean testSuccPred = false;
  boolean uniqueData = true;
  boolean testEnum = true;
  double insertFrac = 1.0D;
  double trueSearchFrac = 0.0D;
  double trueSearchCDF = 1.0D;
  double falseSearchFrac = 0.0D;
  double falseSearchCDF = 1.0D;
  double deleteFrac = 0.0D;
  double deleteCDF = 1.0D;
  int minStringSize = 3;
  int avgStringSize = 3;
  
  SearchAlgorithm alg;
  PropertyExtractor props;
  RandomObjectGenerator rog;
  RandomGenerator rand;
  String[] testData;
  int dataSize;
  int numOps;
  int extraDataSize;
  int totalDataSize;
  int problemSize1;
  int problemSize2;
  
  public SearchTester(PropertyExtractor paramPropertyExtractor)
  {
    try
    {
      this.props = paramPropertyExtractor;
      
      String str1 = paramPropertyExtractor.getStringProperty("searchTester.testLarge");
      if (str1 == null) {
        this.testLarge = false;
      } else if (str1.equalsIgnoreCase("false")) {
        this.testLarge = false;
      } else {
        this.testLarge = true;
      }
      String str2 = paramPropertyExtractor.getStringProperty("searchTester.equalitySearch");
      if (str2 == null) {
        this.eqSearch = true;
      } else if (str2.equalsIgnoreCase("false")) {
        this.eqSearch = false;
      } else {
        this.eqSearch = true;
      }
      String str3 = paramPropertyExtractor.getStringProperty("searchTester.testDelete");
      if (str3 == null) {
        this.testDelete = false;
      } else if (str3.equalsIgnoreCase("false")) {
        this.testDelete = false;
      } else {
        this.testDelete = true;
      }
      String str4 = paramPropertyExtractor.getStringProperty("searchTester.testSuccPred");
      if (str4 == null) {
        this.testSuccPred = false;
      } else if (str4.equalsIgnoreCase("false")) {
        this.testSuccPred = false;
      } else {
        this.testSuccPred = true;
      }
      String str5 = paramPropertyExtractor.getStringProperty("searchTester.uniqueData");
      if (str5 == null) {
        this.uniqueData = true;
      } else if (str5.equalsIgnoreCase("false")) {
        this.uniqueData = false;
      } else {
        this.uniqueData = true;
      }
      String str6 = paramPropertyExtractor.getStringProperty("searchTester.minStringSize");
      if (str6 != null)
        this.minStringSize = Integer.parseInt(str6.trim());
      String str7 = paramPropertyExtractor.getStringProperty("searchTester.avgStringSize");
      if (str7 != null) {
        this.avgStringSize = Integer.parseInt(str7.trim());
      }
      this.testEnum = paramPropertyExtractor.getBooleanProperty("searchTester.testEnum");
      
      this.rog = new RandomObjectGenerator();
      this.rog.setStringParameters(this.minStringSize, this.avgStringSize, 'A', 'Z');
      this.rand = RandomGeneratorFactory.getGenerator();
      setOperationMix(paramPropertyExtractor);
    }
    catch (UtilException localUtilException) {
      System.out.println("SearchTester: Properties exception e=" + localUtilException);
      localUtilException.printStackTrace();
      this.testDelete = (this.testLarge = 0);
      this.uniqueData = true;
    }
    catch (NumberFormatException localNumberFormatException) {
      System.out.println("SearchTester: format exception e=" + localNumberFormatException);
      localNumberFormatException.printStackTrace();
    }
    catch (RandomGeneratorException localRandomGeneratorException) {
      System.out.println("SearchTester: randomgenerator exception e=" + localRandomGeneratorException);
      localRandomGeneratorException.printStackTrace();
    }
    
    System.out.println("SearchTester: testDelete=" + this.testDelete + " testLarge=" + this.testLarge + " equality=" + this.eqSearch + " uniqueData=" + this.uniqueData);
  }
  


  void setOperationMix(PropertyExtractor paramPropertyExtractor)
    throws NumberFormatException, UtilException
  {
    int i = paramPropertyExtractor.getIntProperty("searchTester.insertPercent");
    int j = paramPropertyExtractor.getIntProperty("searchTester.trueSearchPercent");
    int k = paramPropertyExtractor.getIntProperty("searchTester.falseSearchPercent");
    int m = paramPropertyExtractor.getIntProperty("searchTester.deletePercent");
    int n = 0;
    if ((i < 0) || (j < 0) || (k < 0) || (m < 0))
    {
      System.out.println("ERROR: SearchTester: invalid operation mix percentages");
      i = 100;
      j = k = m = 0;
    }
    
    n = i + j + k + m;
    if (n != 100) {
      System.out.println("ERROR: SearchTester: operation mix percentages do not add up");
      i = 100;
      j = k = m = 0;
    }
    this.insertFrac = (i / 100.0D);
    this.trueSearchFrac = (j / 100.0D);
    this.falseSearchFrac = (k / 100.0D);
    if ((this.eqSearch) || (this.advSearch)) {
      this.deleteFrac = (1.0D - (this.insertFrac + this.trueSearchFrac + this.falseSearchFrac));
    } else {
      this.falseSearchFrac = (1.0D - (this.insertFrac + this.trueSearchFrac));
      this.deleteFrac = 0.0D;
    }
    
    if (this.deleteFrac < 0.0D)
      this.deleteFrac = 0.0D;
    this.trueSearchCDF = (this.insertFrac + this.trueSearchFrac);
    this.falseSearchCDF = (this.trueSearchCDF + this.falseSearchFrac);
    this.deleteCDF = 1.0D;
  }
  

  void error(String paramString)
    throws AlgorithmTestException
  {
    throw new AlgorithmTestException(paramString);
  }
  
  public void setAlgorithm(Algorithm paramAlgorithm)
    throws AlgorithmTestException
  {
    if (this.eqSearch) {
      if (!(paramAlgorithm instanceof EqualitySearchAlgorithm)) {
        error("SearchTester: Algorithm " + paramAlgorithm.getName() + " does not implement EqualitySearchAlgorithm");
      }
      this.alg = ((SearchAlgorithm)paramAlgorithm);
    }
    else {
      if (!(paramAlgorithm instanceof OrderedSearchAlgorithm)) {
        error("SearchTester: Algorithm " + paramAlgorithm.getName() + " does not implement OrderedSearchAlgorithm");
      }
      this.alg = ((SearchAlgorithm)paramAlgorithm);
    }
  }
  

  String arrayToString(String paramString, Object[] paramArrayOfObject)
  {
    String str = paramString + ": ";
    for (int i = 0; i < paramArrayOfObject.length; i++)
      str = str + "[" + paramArrayOfObject[i] + "]";
    str = str + "\n";
    return str;
  }
  
  KeyValuePair[] makeArrayCopy(KeyValuePair[] paramArrayOfKeyValuePair)
  {
    KeyValuePair[] arrayOfKeyValuePair = new KeyValuePair[paramArrayOfKeyValuePair.length];
    for (int i = 0; i < paramArrayOfKeyValuePair.length; i++) {}
    

    return arrayOfKeyValuePair;
  }
  
  String[] makeArrayCopy(String[] paramArrayOfString)
  {
    String[] arrayOfString = new String[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++) {
      arrayOfString[i] = new String(paramArrayOfString[i]);
    }
    return arrayOfString;
  }
  
  boolean contained(String paramString, String[] paramArrayOfString)
  {
    for (int i = 0; i < paramArrayOfString.length; i++)
      if (paramString.equals(paramArrayOfString[i]))
        return true;
    return false;
  }
  
  boolean checkEqual(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    String[] arrayOfString = makeArrayCopy(paramArrayOfString2);
    for (int i = 0; i < paramArrayOfString1.length; i++)
    {
      int j = 0;
      for (int k = 0; k < arrayOfString.length; k++) {
        if (paramArrayOfString1[i].equals(arrayOfString[k])) {
          j = 1;
          arrayOfString[k] = null;
          break;
        }
      }
      if (j == 0)
        return false;
    }
    return true;
  }
  

  void testEqCorrectness()
    throws Exception
  {
    Debug.println("SearchTester.testEqualityCorrectness(): algorithm=" + this.alg.getName());
    System.out.println("SearchTester.testEqualityCorrectness: algorithm=" + this.alg.getName());
    
    EqualitySearchAlgorithm localEqualitySearchAlgorithm = (EqualitySearchAlgorithm)this.alg;
    

    int i = 1;
    String[] arrayOfString1 = new String[i];
    for (int j = 0; j < i; j++)
      arrayOfString1[j] = ("" + j);
    String[] arrayOfString2 = makeArrayCopy(arrayOfString1);
    localEqualitySearchAlgorithm.initialize(i);
    if (localEqualitySearchAlgorithm.getCurrentSize() != 0)
      failed("Test1: 1-element: current size should be zero without insertions");
    for (int k = 0; k < i; k++)
      localEqualitySearchAlgorithm.insert(arrayOfString1[k], arrayOfString1[k]);
    String[] arrayOfString3 = new String[i];
    for (int m = 0; m < i; m++) {
      arrayOfString3[m] = ((String)localEqualitySearchAlgorithm.search(arrayOfString2[m]));
      if (arrayOfString3[m] == null)
        failed("Test1: 1-element: search for inserted element=" + arrayOfString2[m] + " failed");
      if (!contained(arrayOfString3[m], arrayOfString2)) {
        failed("Test1: 1-element: element=" + arrayOfString3[m] + " returned, not in original");
      }
    }
    if (this.testEnum) {
      m = 0;
      Enumeration localEnumeration3 = localEqualitySearchAlgorithm.getKeys();
      while (localEnumeration3.hasMoreElements()) {
        arrayOfString3[m] = ((String)localEnumeration3.nextElement());
        m++;
      }
      if (!checkEqual(arrayOfString1, arrayOfString3))
        failed("Test1: 1-element: enumeration did not return correct keys");
      localEnumeration3 = localEqualitySearchAlgorithm.getValues();
      m = 0;
      while (localEnumeration3.hasMoreElements()) {
        arrayOfString3[m] = ((String)localEnumeration3.nextElement());
        m++;
      }
      if (!checkEqual(arrayOfString1, arrayOfString3))
        failed("Test1: 1-element: enumeration did not return correct values");
    }
    passed("Test1: 1-element test");
    


    i = 2;
    arrayOfString1 = new String[i];
    for (m = 0; m < i; m++)
      arrayOfString1[m] = ("" + m);
    arrayOfString2 = makeArrayCopy(arrayOfString1);
    localEqualitySearchAlgorithm.initialize(i);
    for (m = 0; m < i; m++)
      localEqualitySearchAlgorithm.insert(arrayOfString1[m], arrayOfString1[m]);
    arrayOfString3 = new String[i];
    for (m = 0; m < i; m++) {
      arrayOfString3[m] = ((String)localEqualitySearchAlgorithm.search(arrayOfString2[m]));
      if (arrayOfString3[m] == null)
        failed("Test2: 2-element: search for inserted element=" + arrayOfString2[m] + " failed");
      if (!contained(arrayOfString3[m], arrayOfString2)) {
        failed("Test2: 2-element: element=" + arrayOfString3[m] + " returned, not in original");
      }
    }
    if (this.testEnum) {
      Enumeration localEnumeration1 = localEqualitySearchAlgorithm.getKeys();
      i2 = 0;
      while (localEnumeration1.hasMoreElements()) {
        arrayOfString3[i2] = ((String)localEnumeration1.nextElement());
        
        i2++;
      }
      if (i2 != i)
        failed("Test2: 2-element: enumeration did not return correct values");
      if (!checkEqual(arrayOfString1, arrayOfString3))
        failed("Test2: 2-element: enumeration did not return correct keys");
      localEnumeration1 = localEqualitySearchAlgorithm.getValues();
      i2 = 0;
      while (localEnumeration1.hasMoreElements()) {
        arrayOfString3[i2] = ((String)localEnumeration1.nextElement());
        i2++;
      }
      if (i2 != i)
        failed("Test2: 2-element: enumeration did not return correct values");
      if (!checkEqual(arrayOfString1, arrayOfString3))
        failed("Test2: 2-element: enumeration did not return correct values");
    }
    passed("Test2: 2-element test");
    


    i = 10;
    arrayOfString1 = new String[i];
    for (int n = 0; n < i; n++)
      arrayOfString1[n] = ("" + n);
    arrayOfString2 = makeArrayCopy(arrayOfString1);
    localEqualitySearchAlgorithm.initialize(i);
    for (n = 0; n < i; n++)
      localEqualitySearchAlgorithm.insert(arrayOfString1[n], arrayOfString1[n]);
    arrayOfString3 = new String[i];
    for (n = 0; n < i; n++) {
      arrayOfString3[n] = ((String)localEqualitySearchAlgorithm.search(arrayOfString2[n]));
      if (arrayOfString3[n] == null)
        failed("Test3: 10-element: search for inserted element=" + arrayOfString2[n] + " failed");
      if (!contained(arrayOfString3[n], arrayOfString2))
        failed("Test3: 10-element: element=" + arrayOfString3[n] + " returned, not in original");
    }
    if (this.testEnum) {
      Enumeration localEnumeration2 = localEqualitySearchAlgorithm.getKeys();
      i2 = 0;
      while (localEnumeration2.hasMoreElements()) {
        arrayOfString3[i2] = ((String)localEnumeration2.nextElement());
        i2++;
      }
      if (i2 != i)
        failed("Test3: 10-element: enumeration did not return correct values");
      if (!checkEqual(arrayOfString1, arrayOfString3))
        failed("Test3: 10-element: enumeration did not return correct keys");
      localEnumeration2 = localEqualitySearchAlgorithm.getValues();
      i2 = 0;
      while (localEnumeration2.hasMoreElements()) {
        arrayOfString3[i2] = ((String)localEnumeration2.nextElement());
        i2++;
      }
      if (i2 != i)
        failed("Test3: 10-element: enumeration did not return correct values");
      if (!checkEqual(arrayOfString1, arrayOfString3))
        failed("Test3: 10-element: enumeration did not return correct values");
    }
    passed("Test3: 10-element test");
    

    int i1 = 10;
    int i5; for (int i2 = 0; i2 < i1; i2++) {
      i = 10;
      arrayOfString1 = this.rog.makeRandomUniqueStrings(i);
      arrayOfString2 = makeArrayCopy(arrayOfString1);
      localEqualitySearchAlgorithm.initialize(i);
      for (int i3 = 0; i3 < i; i3++)
        localEqualitySearchAlgorithm.insert(arrayOfString1[i3], arrayOfString1[i3]);
      arrayOfString3 = new String[i];
      for (i3 = 0; i3 < i; i3++) {
        arrayOfString3[i3] = ((String)localEqualitySearchAlgorithm.search(arrayOfString2[i3]));
        if (arrayOfString3[i3] == null)
          failed("Test4: #" + i2 + " of " + i1 + " 10-random-element: : search for inserted element=" + arrayOfString2[i3] + " failed");
        if (!contained(arrayOfString3[i3], arrayOfString2))
          failed("Test4: #" + i2 + " of " + i1 + " 10-random-element: : element=" + arrayOfString3[i3] + " returned, not in original");
      }
      if (this.testEnum) {
        Enumeration localEnumeration4 = localEqualitySearchAlgorithm.getKeys();
        i5 = 0;
        while (localEnumeration4.hasMoreElements()) {
          arrayOfString3[i5] = ((String)localEnumeration4.nextElement());
          i5++;
        }
        if (i5 != i)
          failed("Test4: #" + i2 + " of " + i1 + " 10-random-element: : enumeration did not return correct values");
        if (!checkEqual(arrayOfString1, arrayOfString3))
          failed("Test4: #" + i2 + " of " + i1 + " 10-random-element: : enumeration did not return correct keys");
        localEnumeration4 = localEqualitySearchAlgorithm.getValues();
        i5 = 0;
        while (localEnumeration4.hasMoreElements()) {
          arrayOfString3[i5] = ((String)localEnumeration4.nextElement());
          i5++;
        }
        if (i5 != i)
          failed("Test4: #" + i2 + " of " + i1 + " 10-random-element: : enumeration did not return correct values");
        if (!checkEqual(arrayOfString1, arrayOfString3))
          failed("Test4: #" + i2 + " of " + i1 + " 10-random-element: : enumeration did not return correct values");
      }
    }
    passed("Test4: " + i1 + " tests of 10-random-elements");
    

    if (this.testLarge) {
      i1 = 10;
      for (i2 = 0; i2 < i1; i2++) {
        i = 1000;
        arrayOfString1 = this.rog.makeRandomUniqueStrings(i);
        arrayOfString2 = makeArrayCopy(arrayOfString1);
        localEqualitySearchAlgorithm.initialize(i);
        for (int i4 = 0; i4 < i; i4++)
          localEqualitySearchAlgorithm.insert(arrayOfString1[i4], arrayOfString1[i4]);
        arrayOfString3 = new String[i];
        for (i4 = 0; i4 < i; i4++) {
          arrayOfString3[i4] = ((String)localEqualitySearchAlgorithm.search(arrayOfString2[i4]));
          if (arrayOfString3[i4] == null)
            failed("Test5: #" + i2 + " of " + i1 + " 10-random-element: : search for inserted element=" + arrayOfString2[i4] + " failed");
          if (!contained(arrayOfString3[i4], arrayOfString2))
            failed("Test5: #" + i2 + " of " + i1 + " 10-random-element: : element=" + arrayOfString3[i4] + " returned, not in original");
        }
        if (this.testEnum) {
          Enumeration localEnumeration5 = localEqualitySearchAlgorithm.getKeys();
          i5 = 0;
          while (localEnumeration5.hasMoreElements()) {
            arrayOfString3[i5] = ((String)localEnumeration5.nextElement());
            i5++;
          }
          if (i5 != i)
            failed("Test5: #" + i2 + " of " + i1 + " 10-random-element: : enumeration did not return correct values");
          if (!checkEqual(arrayOfString1, arrayOfString3))
            failed("Test5: #" + i2 + " of " + i1 + " 10-random-element: : enumeration did not return correct keys");
          localEnumeration5 = localEqualitySearchAlgorithm.getValues();
          i5 = 0;
          while (localEnumeration5.hasMoreElements()) {
            arrayOfString3[i5] = ((String)localEnumeration5.nextElement());
            i5++;
          }
          if (i5 != i)
            failed("Test5: #" + i2 + " of " + i1 + " 10-random-element: : enumeration did not return correct values");
          if (!checkEqual(arrayOfString1, arrayOfString3))
            failed("Test5: #" + i2 + " of " + i1 + " 10-random-element: : enumeration did not return correct values");
        }
      }
      passed("Test5: " + i1 + " tests of 1000-random-elements");
    }
  }
  


  void testCompCorrectness()
    throws Exception
  {
    Debug.println("SearchTester.testCompCorrectness(): algorithm=" + this.alg.getName());
    System.out.println("SearchTester.testCompCorrectness: algorithm=" + this.alg.getName());
    
    OrderedSearchAlgorithm localOrderedSearchAlgorithm = (OrderedSearchAlgorithm)this.alg;
    

    int i = 1;
    String[] arrayOfString1 = new String[i];
    for (int j = 0; j < i; j++)
      arrayOfString1[j] = ("" + j);
    String[] arrayOfString2 = makeArrayCopy(arrayOfString1);
    localOrderedSearchAlgorithm.initialize(i);
    if (localOrderedSearchAlgorithm.getCurrentSize() != 0)
      failed("Test1: 1-element: current size should be zero without insertions");
    for (int k = 0; k < i; k++)
      localOrderedSearchAlgorithm.insert(arrayOfString1[k], arrayOfString1[k]);
    String[] arrayOfString3 = new String[i];
    for (int m = 0; m < i; m++) {
      ComparableKeyValuePair localComparableKeyValuePair1 = localOrderedSearchAlgorithm.search(arrayOfString2[m]);
      
      arrayOfString3[m] = ((String)localComparableKeyValuePair1.value);
      if (arrayOfString3[m] == null)
        failed("Test1: 1-element: search for inserted element=" + arrayOfString2[m] + " failed");
      if (!contained(arrayOfString3[m], arrayOfString2)) {
        failed("Test1: 1-element: element=" + arrayOfString3[m] + " returned, not in original");
      }
    }
    if (this.testEnum) {
      Enumeration localEnumeration1 = localOrderedSearchAlgorithm.getKeys();
      int i1 = 0;
      while (localEnumeration1.hasMoreElements()) {
        arrayOfString3[i1] = ((String)localEnumeration1.nextElement());
        i1++;
      }
      if (!checkEqual(arrayOfString1, arrayOfString3))
        failed("Test1: 1-element: enumeration did not return correct keys");
      localEnumeration1 = localOrderedSearchAlgorithm.getValues();
      i1 = 0;
      while (localEnumeration1.hasMoreElements()) {
        arrayOfString3[i1] = ((String)localEnumeration1.nextElement());
        i1++;
      }
      if (!checkEqual(arrayOfString1, arrayOfString3)) {
        failed("Test1: 1-element: enumeration did not return correct values");
      }
    }
    passed("Test1: 1-element test");
    


    i = 2;
    arrayOfString1 = new String[i];
    for (int n = 0; n < i; n++)
      arrayOfString1[n] = ("1" + n);
    arrayOfString2 = makeArrayCopy(arrayOfString1);
    localOrderedSearchAlgorithm.initialize(i);
    for (n = 0; n < i; n++)
      localOrderedSearchAlgorithm.insert(arrayOfString1[n], arrayOfString1[n]);
    arrayOfString3 = new String[i];
    for (n = 0; n < i; n++) {
      ComparableKeyValuePair localComparableKeyValuePair2 = localOrderedSearchAlgorithm.search(arrayOfString2[n]);
      arrayOfString3[n] = ((String)localComparableKeyValuePair2.value);
      if (arrayOfString3[n] == null)
        failed("Test2: 2-element: search for inserted element=" + arrayOfString2[n] + " failed");
      if (!contained(arrayOfString3[n], arrayOfString2)) {
        failed("Test2: 2-element: element=" + arrayOfString3[n] + " returned, not in original");
      }
    }
    if (this.testEnum) {
      localObject1 = localOrderedSearchAlgorithm.getKeys();
      int i2 = 0;
      while (((Enumeration)localObject1).hasMoreElements()) {
        arrayOfString3[i2] = ((String)((Enumeration)localObject1).nextElement());
        
        i2++;
      }
      if (i2 != i)
        failed("Test2: 2-element: enumeration did not return correct values");
      if (!checkEqual(arrayOfString1, arrayOfString3))
        failed("Test2: 2-element: enumeration did not return correct keys");
      localObject1 = localOrderedSearchAlgorithm.getValues();
      i2 = 0;
      while (((Enumeration)localObject1).hasMoreElements()) {
        arrayOfString3[i2] = ((String)((Enumeration)localObject1).nextElement());
        i2++;
      }
      if (i2 != i)
        failed("Test2: 2-element: enumeration did not return correct values");
      if (!checkEqual(arrayOfString1, arrayOfString3)) {
        failed("Test2: 2-element: enumeration did not return correct values");
      }
    }
    
    Object localObject1 = localOrderedSearchAlgorithm.minimum();
    ComparableKeyValuePair localComparableKeyValuePair3 = localOrderedSearchAlgorithm.maximum();
    if (((ComparableKeyValuePair)localObject1).key.compareTo(arrayOfString2[0]) != 0)
      failed("Test2: 2-element minimum incorrect");
    if (localComparableKeyValuePair3.key.compareTo(arrayOfString2[(i - 1)]) != 0)
      failed("Test2: 2-element maximum incorrect");
    ComparableKeyValuePair localComparableKeyValuePair4 = null;
    passed("Test2: 2-element test");
    

    i = 10;
    arrayOfString1 = new String[i];
    for (int i3 = 0; i3 < i; i3++)
      arrayOfString1[i3] = ("1" + i3);
    arrayOfString2 = makeArrayCopy(arrayOfString1);
    localOrderedSearchAlgorithm.initialize(i);
    for (i3 = 0; i3 < i; i3++)
      localOrderedSearchAlgorithm.insert(arrayOfString1[i3], arrayOfString1[i3]);
    arrayOfString3 = new String[i];
    for (i3 = 0; i3 < i; i3++) {
      localComparableKeyValuePair4 = localOrderedSearchAlgorithm.search(arrayOfString2[i3]);
      arrayOfString3[i3] = ((String)localComparableKeyValuePair4.value);
      if (arrayOfString3[i3] == null)
        failed("Test3: 10-element: search for inserted element=" + arrayOfString2[i3] + " failed");
      if (!contained(arrayOfString3[i3], arrayOfString2)) {
        failed("Test3: 10-element: element=" + arrayOfString3[i3] + " returned, not in original");
      }
    }
    if (this.testEnum) {
      Enumeration localEnumeration2 = localOrderedSearchAlgorithm.getKeys();
      int i5 = 0;
      while (localEnumeration2.hasMoreElements()) {
        arrayOfString3[i5] = ((String)localEnumeration2.nextElement());
        
        i5++;
      }
      if (i5 != i)
        failed("Test3: 10-element: enumeration did not return correct values");
      if (!checkEqual(arrayOfString1, arrayOfString3))
        failed("Test3: 10-element: enumeration did not return correct keys");
      localEnumeration2 = localOrderedSearchAlgorithm.getValues();
      i5 = 0;
      while (localEnumeration2.hasMoreElements()) {
        arrayOfString3[i5] = ((String)localEnumeration2.nextElement());
        i5++;
      }
      if (i5 != i)
        failed("Test3: 10-element: enumeration did not return correct values");
      if (!checkEqual(arrayOfString1, arrayOfString3)) {
        failed("Test3: 10-element: enumeration did not return correct values");
      }
    }
    
    localObject1 = localOrderedSearchAlgorithm.minimum();
    localComparableKeyValuePair3 = localOrderedSearchAlgorithm.maximum();
    if (((ComparableKeyValuePair)localObject1).key.compareTo(arrayOfString2[0]) != 0)
      failed("Test3: 10-element minimum incorrect");
    if (localComparableKeyValuePair3.key.compareTo(arrayOfString2[(i - 1)]) != 0)
      failed("Test3: 10-element maximum incorrect");
    passed("Test3: 10-element test");
    

    int i4 = 50;
    ArrayOrderedSearch localArrayOrderedSearch = new ArrayOrderedSearch();
    for (int i6 = 0; i6 < i4; i6++) {
      i = 10;
      arrayOfString1 = new String[i];
      arrayOfString1 = this.rog.makeRandomUniqueStrings(i);
      arrayOfString2 = makeArrayCopy(arrayOfString1);
      localOrderedSearchAlgorithm.initialize(i);
      localArrayOrderedSearch.initialize(i);
      for (int i8 = 0; i8 < i; i8++) {
        localOrderedSearchAlgorithm.insert(arrayOfString1[i8], arrayOfString1[i8]);
        localArrayOrderedSearch.insert(arrayOfString1[i8], arrayOfString1[i8]);
      }
      arrayOfString3 = new String[i];
      for (i8 = 0; i8 < i; i8++) {
        localComparableKeyValuePair4 = localOrderedSearchAlgorithm.search(arrayOfString2[i8]);
        arrayOfString3[i8] = ((String)localComparableKeyValuePair4.value);
        if (arrayOfString3[i8] == null)
          failed("Test4: #" + i6 + " of " + i4 + " random 10-element: search for inserted element=" + arrayOfString2[i8] + " failed");
        if (!contained(arrayOfString3[i8], arrayOfString2)) {
          failed("Test4: #" + i6 + " of " + i4 + " random 10-element: element=" + arrayOfString3[i8] + " returned, not in original");
        }
      }
      if (this.testEnum) {
        localObject2 = localOrderedSearchAlgorithm.getKeys();
        int i11 = 0;
        while (((Enumeration)localObject2).hasMoreElements()) {
          arrayOfString3[i11] = ((String)((Enumeration)localObject2).nextElement());
          
          i11++;
        }
        if (i11 != i)
          failed("Test4: #" + i6 + " of " + i4 + " random 10-element: enumeration did not return correct values");
        if (!checkEqual(arrayOfString1, arrayOfString3))
          failed("Test4: #" + i6 + " of " + i4 + " random 10-element: enumeration did not return correct keys");
        localObject2 = localOrderedSearchAlgorithm.getValues();
        i11 = 0;
        while (((Enumeration)localObject2).hasMoreElements()) {
          arrayOfString3[i11] = ((String)((Enumeration)localObject2).nextElement());
          i11++;
        }
        if (i11 != i)
          failed("Test4: #" + i6 + " of " + i4 + " random 10-element: enumeration did not return correct values");
        if (!checkEqual(arrayOfString1, arrayOfString3)) {
          failed("Test4: #" + i6 + " of " + i4 + " random 10-element: enumeration did not return correct values");
        }
      }
      
      localObject1 = localOrderedSearchAlgorithm.minimum();
      localComparableKeyValuePair3 = localOrderedSearchAlgorithm.maximum();
      Object localObject2 = localArrayOrderedSearch.minimum();
      ComparableKeyValuePair localComparableKeyValuePair5 = localArrayOrderedSearch.maximum();
      
      if (((ComparableKeyValuePair)localObject1).key.compareTo(((ComparableKeyValuePair)localObject2).key) != 0)
        failed("Test4: #" + i6 + " of " + i4 + " random 10-element minimum incorrect");
      if (localComparableKeyValuePair3.key.compareTo(localComparableKeyValuePair5.key) != 0)
        failed("Test4: #" + i6 + " of " + i4 + " random 10-element maximum incorrect");
    }
    passed("Test4: " + i4 + " random 10-element tests");
    Object localObject3;
    Object localObject4;
    if (this.testLarge) {
      i4 = 10;
      for (i6 = 0; i6 < i4; i6++) {
        i = 1000;
        arrayOfString1 = new String[i];
        arrayOfString1 = this.rog.makeRandomUniqueStrings(i);
        arrayOfString2 = makeArrayCopy(arrayOfString1);
        localOrderedSearchAlgorithm.initialize(i);
        localArrayOrderedSearch.initialize(i);
        for (int i9 = 0; i9 < i; i9++) {
          localOrderedSearchAlgorithm.insert(arrayOfString1[i9], arrayOfString1[i9]);
          localArrayOrderedSearch.insert(arrayOfString1[i9], arrayOfString1[i9]);
        }
        arrayOfString3 = new String[i];
        for (i9 = 0; i9 < i; i9++) {
          localComparableKeyValuePair4 = localOrderedSearchAlgorithm.search(arrayOfString2[i9]);
          arrayOfString3[i9] = ((String)localComparableKeyValuePair4.value);
          if (arrayOfString3[i9] == null)
            failed("Test5: #" + i6 + " of " + i4 + " random 1000-element: search for inserted element=" + arrayOfString2[i9] + " failed");
          if (!contained(arrayOfString3[i9], arrayOfString2)) {
            failed("Test5: #" + i6 + " of " + i4 + " random 1000-element: element=" + arrayOfString3[i9] + " returned, not in original");
          }
        }
        if (this.testEnum) {
          localObject3 = localOrderedSearchAlgorithm.getKeys();
          int i12 = 0;
          while (((Enumeration)localObject3).hasMoreElements()) {
            arrayOfString3[i12] = ((String)((Enumeration)localObject3).nextElement());
            
            i12++;
          }
          if (i12 != i)
            failed("Test5: #" + i6 + " of " + i4 + " random 1000-element: enumeration did not return correct values");
          if (!checkEqual(arrayOfString1, arrayOfString3))
            failed("Test5: #" + i6 + " of " + i4 + " random 1000-element: enumeration did not return correct keys");
          localObject3 = localOrderedSearchAlgorithm.getValues();
          i12 = 0;
          while (((Enumeration)localObject3).hasMoreElements()) {
            arrayOfString3[i12] = ((String)((Enumeration)localObject3).nextElement());
            i12++;
          }
          if (i12 != i)
            failed("Test5: #" + i6 + " of " + i4 + " random 1000-element: enumeration did not return correct values");
          if (!checkEqual(arrayOfString1, arrayOfString3)) {
            failed("Test5: #" + i6 + " of " + i4 + " random 1000-element: enumeration did not return correct values");
          }
        }
        
        localObject1 = localOrderedSearchAlgorithm.minimum();
        localComparableKeyValuePair3 = localOrderedSearchAlgorithm.maximum();
        localObject3 = localArrayOrderedSearch.minimum();
        localObject4 = localArrayOrderedSearch.maximum();
        
        if (((ComparableKeyValuePair)localObject1).key.compareTo(((ComparableKeyValuePair)localObject3).key) != 0)
          failed("Test5: #" + i6 + " of " + i4 + " random 1000-element minimum incorrect");
        if (localComparableKeyValuePair3.key.compareTo(((ComparableKeyValuePair)localObject4).key) != 0)
          failed("Test5: #" + i6 + " of " + i4 + " random 1000-element maximum incorrect");
      }
      passed("Test5: " + i4 + " random 1000-element tests");
    }
    
    if (this.testDelete) {
      i = 10;
      arrayOfString1 = new String[i];
      for (i6 = 0; i6 < i; i6++)
        arrayOfString1[i6] = ("1" + i6);
      arrayOfString2 = makeArrayCopy(arrayOfString1);
      localOrderedSearchAlgorithm.initialize(i);
      for (i6 = 0; i6 < i; i6++) {
        localOrderedSearchAlgorithm.insert(arrayOfString1[i6], arrayOfString1[i6]);
      }
      String str1 = (String)localOrderedSearchAlgorithm.delete(arrayOfString2[0]);
      localObject3 = (String)localOrderedSearchAlgorithm.delete(arrayOfString2[2]);
      localObject4 = (String)localOrderedSearchAlgorithm.delete(arrayOfString2[9]);
      
      if (!str1.equals(arrayOfString2[0]))
        failed("Test 6: deletion");
      if (!((String)localObject3).equals(arrayOfString2[2]))
        failed("Test 6: deletion");
      if (!((String)localObject4).equals(arrayOfString2[9])) {
        failed("Test 6: deletion");
      }
      if (localOrderedSearchAlgorithm.search(arrayOfString2[0]) != null)
        failed("Test 6: deletion");
      if (localOrderedSearchAlgorithm.search(arrayOfString2[2]) != null)
        failed("Test 6: deletion");
      if (localOrderedSearchAlgorithm.search(arrayOfString2[9]) != null) {
        failed("Test 6: deletion");
      }
      for (int i14 = 0; i14 < i; i14++) {
        if ((i14 != 0) && (i14 != 2) && (i14 != 9)) {
          localComparableKeyValuePair4 = localOrderedSearchAlgorithm.search(arrayOfString2[i14]);
          String str4 = (String)localComparableKeyValuePair4.value;
          if (!str4.equals(arrayOfString2[i14]))
            failed("Test 6: deletion");
        }
      }
      passed("Test6: deletion");
    }
    
    if (this.testSuccPred) {
      i = 10;
      arrayOfString1 = new String[i];
      for (int i7 = 0; i7 < i; i7++)
        arrayOfString1[i7] = ("1" + i7);
      arrayOfString2 = makeArrayCopy(arrayOfString1);
      localOrderedSearchAlgorithm.initialize(i);
      for (i7 = 0; i7 < i; i7++) {
        localOrderedSearchAlgorithm.insert(arrayOfString1[i7], arrayOfString1[i7]);
      }
      String str2 = (String)localOrderedSearchAlgorithm.successor(arrayOfString2[9]);
      if (str2 != null)
        failed("Test 7: successor");
      for (int i10 = 0; i10 < 9; i10++) {
        str2 = (String)localOrderedSearchAlgorithm.successor(arrayOfString2[i10]);
        if (!str2.equals(arrayOfString2[(i10 + 1)])) {
          failed("Test 7: successor");
        }
      }
      String str3 = (String)localOrderedSearchAlgorithm.predecessor(arrayOfString2[0]);
      if (str3 != null)
        failed("Test 7: predecessor");
      for (int i13 = 1; i13 < 10; i13++) {
        str3 = (String)localOrderedSearchAlgorithm.predecessor(arrayOfString2[i13]);
        if (!str3.equals(arrayOfString2[(i13 - 1)]))
          failed("Test 7: predecessor");
      }
      passed("Test7: successor, predecessor");
    }
  }
  

  public TestResult testCorrectness()
    throws AlgorithmTestException
  {
    try
    {
      if (this.eqSearch) {
        testEqCorrectness();
      } else {
        testCompCorrectness();
      }
    } catch (Exception localException) {
      System.out.println("Exception caught in testing correctness:" + localException);
      localException.printStackTrace();
      System.out.println("Continuing...");
    }
    
    return null;
  }
  
  void failed(String paramString, Object[] paramArrayOfObject)
    throws AlgorithmTestException
  {
    Debug.println("SearchTester: FAILED: " + paramString);
    Debug.println(arrayToString(": array", paramArrayOfObject));
    System.out.println("SearchTester: FAILED: " + paramString);
    throw new AlgorithmTestException(paramString);
  }
  
  void failed(String paramString)
    throws AlgorithmTestException
  {
    Debug.println("SearchTester: FAILED: " + paramString);
    System.out.println("SearchTester: FAILED: " + paramString);
    throw new AlgorithmTestException(paramString);
  }
  

  void passed(String paramString)
  {
    Debug.println(" => passed: " + paramString);
    System.out.println(" => passed: " + paramString);
  }
  
  public void resetSeed()
  {
    this.rog.resetSeed();
  }
  

  void generateEqPerformanceData(int paramInt1, int paramInt2)
    throws RandomGeneratorException
  {
    EqualitySearchAlgorithm localEqualitySearchAlgorithm = (EqualitySearchAlgorithm)this.alg;
    this.dataSize = paramInt1;
    this.numOps = paramInt2;
    this.extraDataSize = paramInt2;
    this.totalDataSize = (paramInt1 + this.extraDataSize);
    this.testData = this.rog.makeRandomUniqueStrings(this.totalDataSize);
    
    localEqualitySearchAlgorithm.initialize(this.totalDataSize);
    for (int i = 0; i < paramInt1; i++) {
      localEqualitySearchAlgorithm.insert(this.testData[i], this.testData[i]);
    }
  }
  
  void generateOrdPerformanceData(int paramInt1, int paramInt2)
    throws RandomGeneratorException
  {
    OrderedSearchAlgorithm localOrderedSearchAlgorithm = (OrderedSearchAlgorithm)this.alg;
    this.dataSize = paramInt1;
    this.numOps = paramInt2;
    this.extraDataSize = paramInt2;
    this.totalDataSize = (paramInt1 + this.extraDataSize);
    this.testData = this.rog.makeRandomUniqueStrings(this.totalDataSize);
    
    localOrderedSearchAlgorithm.initialize(this.totalDataSize);
    for (int i = 0; i < paramInt1; i++) {
      localOrderedSearchAlgorithm.insert(this.testData[i], this.testData[i]);
    }
  }
  
  public void generatePerformanceData(int paramInt)
    throws AlgorithmTestException
  {
    System.out.println("ERROR: 1-parameter data generation not supported in searching");
    System.exit(0);
  }
  

  public void generatePerformanceData(int paramInt1, int paramInt2)
    throws AlgorithmTestException
  {
    this.problemSize1 = paramInt1;
    this.problemSize2 = paramInt2;
    try
    {
      if (this.eqSearch) {
        generateEqPerformanceData(paramInt1, paramInt2);
      } else {
        generateOrdPerformanceData(paramInt1, paramInt2);
      }
    } catch (RandomGeneratorException localRandomGeneratorException) {
      throw new AlgorithmTestException("RandomGeneratorException: ex=" + localRandomGeneratorException);
    }
  }
  
  void runEqPerformanceTest()
  {
    EqualitySearchAlgorithm localEqualitySearchAlgorithm = (EqualitySearchAlgorithm)this.alg;
    for (int i = 0; i < this.numOps; i++) {
      double d = this.rand.uniform();
      int j; if (d < this.insertFrac)
      {
        j = (int)this.rand.uniform(this.dataSize, this.totalDataSize - 1);
        localEqualitySearchAlgorithm.insert(this.testData[j], this.testData[j]);
      } else { Object localObject;
        if (d < this.trueSearchCDF)
        {
          j = (int)this.rand.uniform(0L, this.dataSize - 1);
          localObject = localEqualitySearchAlgorithm.search(this.testData[j]);
        }
        else if (d < this.falseSearchCDF)
        {
          j = (int)this.rand.uniform(this.dataSize, this.totalDataSize - 1);
          localObject = localEqualitySearchAlgorithm.search(this.testData[j]);
        }
        else
        {
          j = (int)this.rand.uniform(0L, this.dataSize - 1);
          localObject = localEqualitySearchAlgorithm.delete(this.testData[j]);
        }
      }
    }
  }
  
  void runOrdPerformanceTest() {
    OrderedSearchAlgorithm localOrderedSearchAlgorithm = (OrderedSearchAlgorithm)this.alg;
    for (int i = 0; i < this.numOps; i++) {
      double d = this.rand.uniform();
      int j; if (d < this.insertFrac)
      {
        j = (int)this.rand.uniform(this.dataSize, this.totalDataSize - 1);
        localOrderedSearchAlgorithm.insert(this.testData[j], this.testData[j]);
      } else { ComparableKeyValuePair localComparableKeyValuePair;
        if (d < this.trueSearchCDF)
        {
          j = (int)this.rand.uniform(0L, this.dataSize - 1);
          localComparableKeyValuePair = localOrderedSearchAlgorithm.search(this.testData[j]);
        }
        else
        {
          j = (int)this.rand.uniform(this.dataSize, this.totalDataSize - 1);
          localComparableKeyValuePair = localOrderedSearchAlgorithm.search(this.testData[j]);
        }
      }
    }
  }
  
  public TestResult runPerformanceTest()
    throws AlgorithmTestException
  {
    if (this.eqSearch) {
      runEqPerformanceTest();
    } else
      runOrdPerformanceTest();
    return null;
  }
  
  public String getXaxisLabel()
  {
    return "Data size";
  }
  
  public String getYaxisLabel()
  {
    return "Time";
  }
  
  public String getTitle()
  {
    return "Search Comparison";
  }
  


  public boolean ifMetric()
  {
    return false;
  }
  
  public boolean ifSpecialTest()
  {
    return false;
  }
  

  public TestResult runSpecialTest()
    throws AlgorithmTestException
  {
    return null;
  }
  

  Algorithm loadAlgorithm(String paramString)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    Class localClass = Class.forName(paramString);
    return (Algorithm)localClass.newInstance();
  }
  
  public static void main(String[] paramArrayOfString)
  {
    try
    {
      SearchTester localSearchTester = new SearchTester(null);


    }
    catch (Exception localException)
    {


      localException.printStackTrace();
    }
  }
}

