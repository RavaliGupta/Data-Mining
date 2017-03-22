package project2;

import java.awt.LinearGradientPaint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import ca.pfv.spmf.algorithms.frequentpatterns.zart.AlgoZart;
import ca.pfv.spmf.algorithms.frequentpatterns.zart.TFTableFrequent;
import ca.pfv.spmf.algorithms.frequentpatterns.zart.TZTableClosed;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.InterquartileRange;
import weka.filters.unsupervised.attribute.Remove;

 class ObjFunctionInfo {
    double objectivefunc;
    ArrayList<String> closedpattern1 = new ArrayList<>();
    ArrayList<String>  closedpattern2= new ArrayList<>();
    ArrayList<Double> jaccardsimilarity = new ArrayList<>();
}

public class MainProgram {
	
	static ArrayList <ObjFunctionInfo> infolist = new ArrayList<> ();
	static ArrayList<String> closedemerginpattern = new ArrayList<>();
	static ArrayList<String> closedpattern = new ArrayList<>();
	static ArrayList<Double> growthrate= new ArrayList<>();
	static ArrayList<Double> support1 = new ArrayList<>();
	static ArrayList<Double> support0= new ArrayList<>();
	static HashMap<String, String> vectorrep= new HashMap<>();
	 static HashMap<Integer, Double> hash = new HashMap<>();
     static HashMap<Integer, Double> maxvalue = new HashMap<>();
     static HashMap<Integer, Double> minvalue = new HashMap<>();
     static ArrayList<Integer> classcol=new ArrayList<>();
     static ArrayList lines = new ArrayList(); 
     static String[] fields;
     static String[][] strings;
     static ArrayList lines1 = new ArrayList(); 
     
     static String[] strings1;
     static Double[] num_in_array;

public static void main(String args[]) throws Exception
{
	
	System.out.println("Enter the Data File: ");
	Scanner scanner = new Scanner(System.in);
	String datafilemain = scanner.nextLine();
	System.out.println("Enter the value of minSup: ");
	double minSup = scanner.nextDouble();
	System.out.println("Enter the value of minGrowthRate: ");
	double minGrowthRate = scanner.nextDouble();
	
	/////coverting csv to arff
	/////// please uncomment this block if using different data file
	
	 /*CSVLoader loader = new CSVLoader();
	    loader.setSource(new File(System.getProperty("user.dir") + "//src//project2//bank_market_1.csv"));
	    Instances data = loader.getDataSet();
	 
	    // save ARFF
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File(System.getProperty("user.dir") + "//src//project2//bank_market2.arff"));
	   // saver.setDestination(new File(System.getProperty("user.dir") + "//src//project2//bank_market2.arff"));
	    saver.writeBatch();*/
	    
	    /////loading arff file in the in stances
	    BufferedReader reader = new BufferedReader(
                new FileReader(System.getProperty("user.dir") + "//src//project2//bank_market2.arff"));
          Instances datafile = new Instances(reader);
           reader.close();
           datafile.setClassIndex(datafile.numAttributes() - 1);
	    
	    
	
	//Discretize_map(datafilemain); //data discretization 
	FPClosed(minSup);  
	BitVectorRep();   //task2
	ClosedEmergingPatterns(minGrowthRate); //task3
	ClosedJaccardPatterns();  //task4
	ClosedPatterns(minSup); //task1
          // NaiveBaseClassifier(); //task5
DifferentDataMethods(datafile); //task6
}


/////////// start of task 5//////////////
public static void NaiveBaseClassifier() throws Exception
{
	
	boolean temp =false;
	double errorrate=0.0;
	int classvar =0;
	ArrayList<Double> errorlist=new ArrayList<>();
	ArrayList<Integer> classcol=new ArrayList<>();
	 ArrayList<String> closedpattern = new ArrayList<>();
	 
	 for (String classdata : Files.readAllLines(Paths.get(System.getProperty("user.dir") + "//src//project2//BANK_MARKET.txt"))) {	
		 for (String partdata : classdata.split(",")) {
			  classvar=Integer.valueOf(partdata);
			  classcol.add(classvar);
			  break;
		 }
	 }
	 for (String lineclosed : Files.readAllLines(Paths.get("C://MS-Summer//Data Mining//DataMining Workspace//Project2//src//fpgrowth//outputclosedpatternsonly.txt"))) {
		 ArrayList<String> matchpattern=new ArrayList<>();
	 int closedcount=0;
	 closedpattern.add(lineclosed);
	 for (String linedata : Files.readAllLines(Paths.get("C://MS-Summer//Data Mining//DataMining Workspace//Project2//src//fpgrowth//DiscretizedD.txt"))) {
		
	 	   for (String partclosed : lineclosed.split(" ")) {
		   Integer closed=Integer.valueOf(partclosed);
		   for (String partdata : linedata.split(" ")) {
			     Integer data =Integer.valueOf(partdata);
			   if(closed==data)
			   {
				   temp=true;
				   break;
			   }
			   else
			   {
				   temp=false;
				   
			   }
		   }	        	    	
	   }
	   if(temp==true){
		  matchpattern.add(linedata);
		  closedcount++;
	   }			  		   
   }

 FastVector fvWekaAttributes = new FastVector(21);
 FastVector fvClassVal = new FastVector(2);
 fvClassVal.addElement("1");
 fvClassVal.addElement("0");
 Attribute ClassAttribute = new Attribute("ClassAttribute",fvClassVal);
 fvWekaAttributes.addElement(ClassAttribute);
 String AttributeVar="";
 for(int i=1;i<21;i++)
 {
	 AttributeVar = "Attribute"+i;
FastVector fvNominalVal = new FastVector(5);
fvWekaAttributes.addElement( new Attribute(AttributeVar));
 }



// Declare the class attribute along with its values

Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, closedcount);
isTrainingSet.setClassIndex(0);
int total_num_cols=21;
System.out.println("closed couint:::"+closedcount);
for(int i=0;i<(closedcount);i++)
{
Instance iExample = new DenseInstance(total_num_cols);
for(int j=0;j<21;j++)
{
if(j==0)
{
iExample.setValue((Attribute)fvWekaAttributes.elementAt(j), classcol.get(i));
}
else
{
	int p = 1;			
for(String matchdata:matchpattern.get(i).split(" ")){		
	iExample.setValue((Attribute)fvWekaAttributes.elementAt(p),Integer.parseInt(matchdata));
	p++;
}
}
}
isTrainingSet.add(iExample);

}

NaiveBayes cModel = new NaiveBayes();
cModel.buildClassifier(isTrainingSet);
	 
Evaluation eTest = new Evaluation(isTrainingSet);
eTest.evaluateModel(cModel, isTrainingSet);
errorrate=eTest.errorRate();
errorlist.add(errorrate);
System.out.println("errorlist is:::"+errorrate);
String strSummary = eTest.toSummaryString();
//System.out.println(strSummary);

double[][] cmMatrix = eTest.confusionMatrix();
	 }
//////////////// calculating objective function////////////////////
int i,j=0;
double errorratecalc=0.0;
int n=3;
int k=2;
int l;
int once=1;
ArrayList<ArrayList<Integer>> permutations = new ArrayList<>();
ArrayList<Integer> comboval= new ArrayList<>();
ArrayList<String> closepattern1= new ArrayList<>();
ArrayList<String> closepattern2= new ArrayList<>();
HashMap<Integer,Double> obfunction=new HashMap<>();
double similarity=0.0;
ArrayList<Double> jacsimilarity= new ArrayList<>();

double objfunc=0.0;
String obj_print="";
String jaccard_print="";
String file_print="";
int highk=0;
for(i=0;i<errorlist.size();i++)
{
	 for(j=0;j<i+1;j++){
		 errorratecalc+=(errorlist.get(j));
			
     }
	 errorratecalc=errorratecalc/j;
	 ObjFunctionInfo objinfo = new ObjFunctionInfo();
	 ///////////calling permutations method to get combinations!!!
	 if(j==2 || j>2){
		 
	 permutations= combine(j, k);
	 for(l=0;l<permutations.size();l++)
	 {
		 for(j=0;j<(permutations.get(l)).size();j++)
		 {
		     comboval.add((permutations.get(l)).get(j));
		 }
		 similarity= findjaccardSimilarity(closedpattern.get((comboval.get(0))-1),closedpattern.get((comboval.get(1))-1));
		 closepattern1.add(closedpattern.get((comboval.get(0))-1));
		 closepattern2.add(closedpattern.get((comboval.get(1))-1));		
		 jacsimilarity.add(similarity);
		 similarity+=similarity;
		 comboval.clear();	      
	        }
		
	 }
	 similarity=similarity/(permutations.size());
	 objfunc=errorratecalc*(1-similarity);	
	 objinfo.closedpattern1.addAll(closepattern1);
	 objinfo.closedpattern2.addAll(closepattern2); 
	 objinfo.jaccardsimilarity.addAll(jacsimilarity);
	 objinfo.objectivefunc = objfunc;		   
	 infolist.add(objinfo);
	 if(i==2 || i>2)
	 {
    obfunction.put(i, objfunc);
	 }
	 errorratecalc=0.0;
	 similarity=0.0;
	 
	 }

	
//}

Double maxValueInMap=(Collections.max(obfunction.values()));  // This will return max value in the Hashmap
  for (Entry<Integer, Double> entry : obfunction.entrySet()) {  // Itrate through hashmap
      if (entry.getValue()==maxValueInMap) {
      	highk=entry.getKey();
          System.out.println("Key value of the highest hasg value is:::"+highk);     // Print the key with max value
      }
  }
  
  //Collections.sort(1,(List<ObjFunctionInfo>) infolist);
  
  if(once==1)
  {
  obj_print+="#Objective Function:"+obfunction.get(highk)+"\n";
  writeToPSFileTask5(obj_print);
  once=0;
  }
  System.out.println("highest objective function is::::"+infolist.get(highk).objectivefunc);
  for(int x=0;x<infolist.get(highk).closedpattern1.size();x++){
  	jaccard_print="Closed Pattern1:"+"  "+infolist.get(highk).closedpattern1.get(x)+"  "+"Closed Pattern2:"+" "+infolist.get(highk).closedpattern2.get(x)+"  "+
  	"Jaccard Similarity:"+"  "+infolist.get(highk).jaccardsimilarity.get(x)+"  "+"\n";
      writeToJaccardFileTask5(jaccard_print);
      jaccard_print="";
   }
 
  for(int m=0;m<highk;m++){
  file_print+="Closed Pattern1:"+closedpattern.get(m)+"  "+"#GrowthRate: "+" "+errorlist.get(m)+"\n";
	 writeToPSFileTask5(file_print);  /// uncomment it please dont delete
	 file_print="";
  }
printMap(obfunction);	 
}


// }		

public static void writeToPSFileTask5(String desc_write){
    try{
      FileWriter fw = new FileWriter(System.getProperty("user.dir") + "//src//project2//PSkEPsTask5.csv",true);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(desc_write);
      bw.close();
    }
    catch (Exception e) {
    	e.printStackTrace();
	}
}

public static void writeToJaccardFileTask5(String desc_write){
    try{
      FileWriter fw = new FileWriter(System.getProperty("user.dir") + "//src//project2//PSkEPJaccardTask5.csv",true);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(desc_write);
      bw.close();
    }
    catch (Exception e) {
    	e.printStackTrace();
	}
}




//////// end of task 5////////////////


////////////// start of task 6///////////////
public static void DifferentDataMethods(Instances datafile) throws Exception
{
	LinearRegression linearreg=new LinearRegression();
	linearreg.setOutputAdditionalStats(true);
	linearreg.buildClassifier(datafile);
	BufferedWriter output=new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "//src//project2//Linear_Regression.csv"));
	output.write(linearreg.toString());
	output.close();
	
	InterquartileRange outlier = new InterquartileRange();
	outlier.setInputFormat(datafile);
	Instances outlierDetected=Filter.useFilter(datafile, outlier);
	BufferedWriter outputcluster =new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "//src//project2//Clustering.csv"));
	outputcluster.write(outlierDetected.toString());
	outputcluster.close();
	
	Instances datanoclass=null;
	Remove filter_remove=new Remove();
	filter_remove.setAttributeIndices(""+ (datafile.classIndex()+1));
	filter_remove.setInputFormat(datafile);
	datanoclass=Filter.useFilter(datafile, filter_remove);
	
	
	SimpleKMeans kmeans= new SimpleKMeans();	
	kmeans.setSeed(10);
	kmeans.setNumClusters(10);
	kmeans.buildClusterer(datanoclass);
	BufferedWriter outputkmeans =new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "//src//project2//Kmeans.csv"));
	outputkmeans.write(kmeans.toString());
	outputkmeans.close();
	
}

//////////////////end of task 6///////////////////////////

//////////////start of fourth task ///////////////////////

public static void ClosedJaccardPatterns()
{
	System.out.println("Start of task 4  !!!!!!!");
	System.out.println("size of closedemergingpattern is:::"+closedemerginpattern.size());
	ArrayList<ArrayList<Integer>> permutations = new ArrayList<>();
	ArrayList<Integer> comboval= new ArrayList<>();
	ArrayList<Double> jacsimilarity= new ArrayList<>();
	ArrayList<String> closepattern1= new ArrayList<>();
	ArrayList<String> closepattern2= new ArrayList<>();
	HashMap<Integer,Double> obfunction=new HashMap<>();
	int i,j=0;
	int highk=0;
	double similarity=0;
	String file_print="";
	String obj_print="";
	String jaccard_print="";
	double avggrowthrate=0.0;
	boolean temp=false;
	double objfunc=0.0;
	int n=3;
	int k=2;
	int l;
	int once=1;
	 for(i=0;i<closedemerginpattern.size();i++)
	 {
		 for(j=0;j<i+1;j++){
		 avggrowthrate+=(growthrate.get(j));
				
	      }
		 avggrowthrate=avggrowthrate/j;
		 ObjFunctionInfo objinfo = new ObjFunctionInfo();
		 ///////////calling permutations method to get combinations!!!
		 if(j==2 || j>2){
			 
		 permutations= combine(j, k);
		 for(l=0;l<permutations.size();l++)
		 {
			 for(j=0;j<(permutations.get(l)).size();j++)
			 {
			     comboval.add((permutations.get(l)).get(j));
			 }
			 similarity= findjaccardSimilarity(closedemerginpattern.get((comboval.get(0))-1),closedemerginpattern.get((comboval.get(1))-1));
			 closepattern1.add(closedemerginpattern.get((comboval.get(0))-1));
			 closepattern2.add(closedemerginpattern.get((comboval.get(1))-1));		
			 jacsimilarity.add(similarity);
			 similarity+=similarity;
			 comboval.clear();	      
		        }
			
		 }
		 similarity=similarity/(permutations.size());
		 objfunc=avggrowthrate*(1-similarity);	
		 objinfo.closedpattern1.addAll(closepattern1);
		 objinfo.closedpattern2.addAll(closepattern2); 
		 objinfo.jaccardsimilarity.addAll(jacsimilarity);
		 objinfo.objectivefunc = objfunc;		   
		 infolist.add(objinfo);
		 if(i==2 || i>2)
		 {
	     obfunction.put(i, objfunc);
		 }
		 avggrowthrate=0.0;
		 similarity=0.0;
		 
		 }
	
		
	 //}
	 
	 Double maxValueInMap=(Collections.max(obfunction.values()));  // This will return max value in the Hashmap
        for (Entry<Integer, Double> entry : obfunction.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==maxValueInMap) {
            	highk=entry.getKey();
                System.out.println("Key value of the highest hasg value is:::"+highk);     // Print the key with max value
            }
        }
        
        //Collections.sort(1,(List<ObjFunctionInfo>) infolist);
        
        if(once==1)
        {
        obj_print+="#Objective Function:"+obfunction.get(highk)+"\n";
        writeToPSFile(obj_print);
        once=0;
        }
        System.out.println("highest objective function is::::"+infolist.get(highk).objectivefunc);
        for(int x=0;x<infolist.get(highk).closedpattern1.size();x++){
        	jaccard_print="Closed Pattern1:"+"  "+infolist.get(highk).closedpattern1.get(x)+"  "+"Closed Pattern2:"+" "+infolist.get(highk).closedpattern2.get(x)+"  "+
        	"Jaccard Similarity:"+"  "+infolist.get(highk).jaccardsimilarity.get(x)+"  "+"\n";
            writeToJaccardFile(jaccard_print);
            jaccard_print="";
         }
       
        for(int m=0;m<highk;m++){
        file_print+="Closed Pattern1:"+closedemerginpattern.get(m)+"  "+"#GrowthRate: "+" "+growthrate.get(m)+"  "+"Support for class0:"+" "+
        support0.get(m)+"  "+"Support for class1:"+"  "+support1.get(m)+"\n";
		 writeToPSFile(file_print);  /// uncomment it please dont delete
		 file_print="";
        }
	 printMap(obfunction);	 
	
	 
}

public static double findjaccardSimilarity(String pattern1,String pattern2)
{
	boolean temp=false;
	 String value="";
	 double similarity=0;
	   int intersection=0;
	   int union=0;
	 List<BitSet> list = new ArrayList<BitSet>();
	 String bitvector1=vectorrep.get(pattern1);
	 String bitvector2=vectorrep.get(pattern2);
	// System.out.println("bitvector1:::"+bitvector1+"bitvector2::::"+bitvector2);
	 list.add(createBitSet(bitvector1));
	 list.add(createBitSet(bitvector2));
	 (list.get(0)).and(list.get(1));
	   intersection=(list.get(0)).cardinality();
	   (list.get(0)).or((list.get(1)));
	   union=(list.get(0)).cardinality();
	   similarity=((double)intersection)/((double)union);
	  /* System.out.println("Intersection:::"+intersection);
	   System.out.println("Union:::"+union);
	   System.out.println("Jaccard Similarity:::"+similarity);*/
	   return similarity;
	
}

public static void printMap(Map mp) {
Iterator it = mp.entrySet().iterator();

while (it.hasNext()) {
    Map.Entry pair = (Map.Entry)it.next();
    System.out.println(pair.getKey() + " = " + pair.getValue());
    it.remove(); // avoids a ConcurrentModificationException
}


}

public static ArrayList<ArrayList<Integer>> combine(int n, int k) {
	ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
 
	if (n <= 0 || n < k)
		return result;
 
	ArrayList<Integer> item = new ArrayList<Integer>();
	dfs(n, k, 1, item, result); // because it need to begin from 1
 
	return result;
}
 
private static void dfs(int n, int k, int start, ArrayList<Integer> item,
		ArrayList<ArrayList<Integer>> res) {
	if (item.size() == k) {
		res.add(new ArrayList<Integer>(item));
		return;
	}
 
	for (int i = start; i <= n; i++) {
		item.add(i);
		dfs(n, k, i + 1, item, res);
		item.remove(item.size() - 1);
	}
}

public static void writeToPSFile(String desc_write){
    try{
      FileWriter fw = new FileWriter(System.getProperty("user.dir") + "//src//project2//PSkEPs.csv",true);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(desc_write);
      bw.close();
    }
    catch (Exception e) {
    	e.printStackTrace();
	}
}

public static void writeToJaccardFile(String desc_write){
    try{
      FileWriter fw = new FileWriter(System.getProperty("user.dir") + "//src//project2//PSkEPJaccard.csv",true);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(desc_write);
      bw.close();
    }
    catch (Exception e) {
    	e.printStackTrace();
	}
}



//////////////////end of task four/////////////////


////////////Start of third task///////////////////

public static void ClosedEmergingPatterns(double minGrowthRate) throws NumberFormatException, IOException
{
	System.out.println("Start of Task 3   !!!!!");
	
	int classvar =0;
	int class1;
	int class0;
	int totalclass0=0;
	int totalclass1=0;
	int count1=0;
	int count0=0;
	int pattern1=0;
	int pattern0=0;
	double supp1=0f;
	double supp0=0f;
	double growthrate0=0.0;
	double growthrate1=0.0;
	double maxgrowthrate;
	double exp_growthrate=minGrowthRate;
	 int classvarcount=0;
	
	
	String closedemerpattern="";
	String closedonly="";
	String growth="";
	   boolean temp=false;
	   for (String classdata : Files.readAllLines(Paths.get(System.getProperty("user.dir") + "//src//project2//BANK_MARKET.txt"))) {	
			 for (String partdata : classdata.split(",")) {
				  classvar=Integer.valueOf(partdata);
				  classcol.add(classvar);
				  break;
			 }
		 }
	   System.out.println("class col size"+classcol.size());
 for (String lineclosed : Files.readAllLines(Paths.get(System.getProperty("user.dir") + "//src//project2//outputclosedpatternsonly.txt"))) {
	 for(String linedata : Files.readAllLines(Paths.get(System.getProperty("user.dir") + "//src//project2//DiscretizedD.txt")))
	 {		
	
	 for (String partclosed : lineclosed.split(" ")) {
		 
		 Integer closed=Integer.valueOf(partclosed);
		   for (String parttest : linedata.split(" ")) {
			   Integer data =Integer.valueOf(parttest);
			   if(closed==data)
			   {
				   temp=true;
				   break;
			   }
			   else
			   {
				   temp=false;				   
			   }
		   }
		  
  	    	
	   }
	   if(temp==true){
		  if((classcol.get(classvarcount))==1)
		   {
			   count1++;
			   pattern1++;
			   classvarcount++;
		   }
		   else
		   {
			   count0++;
			   pattern0++;
			   classvarcount++;
		   }
	   }
	   else
	   {
		   if((classcol.get(classvarcount))==1)
		   {
			   totalclass1++;
			   classvarcount++;
		   }
		   else
		   {
			   totalclass0++;
			   classvarcount++;
		   }
	   }
   }

	 classvarcount=0;
	 class1=totalclass1+count1;
	 class0=totalclass0+count0;
	 supp1=((double)pattern1)/((double)class1);
	 supp0=((double)pattern0)/((double)class0);
	 growthrate1=supp1/supp0;
	 growthrate0=supp0/supp1;
	 maxgrowthrate=Math.max(growthrate1,growthrate0);
	 if((exp_growthrate<maxgrowthrate) || (exp_growthrate==maxgrowthrate))
	 {
		 closedemerpattern+=lineclosed+"  "+"#GrowthRate:"+"   "+maxgrowthrate+"   "+"#Supp for class1:"+growthrate1+"   "+"#Supp for class0:"+growthrate0;
		// closedonly+=lineclosed;
	    //  growth+=maxgrowthrate;
		 closedemerginpattern.add(lineclosed);
		 growthrate.add(maxgrowthrate);
		 support1.add(growthrate1);
		 support0.add(growthrate0);	
	 }
	 else
	 {
		 closedemerpattern.trim();
	 }
	 closedemerpattern+="\n";
	 writeToClosedEmerging(closedemerpattern);	 // please uncomment this later dont delete
	 closedemerpattern="";
 }
 
}

/*public static void setClosedEmerging(ArrayList<String> closedemergingpattern) {
	this.closedemergingpattern=closedemergingpattern;
}*/

 public static void writeToClosedEmerging(String desc_write){
	    try{
	      FileWriter fw = new FileWriter(System.getProperty("user.dir") + "//src//project2//closedemergingpattern.csv",true);
	      BufferedWriter bw = new BufferedWriter(fw);
	      bw.write(desc_write);
	      bw.close();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
		}
	}

/////////////////end of thrid task///////////////////////


//////////Start of second task ///////////////////////////////////
public static void BitVectorRep() throws IOException
{
	System.out.println("Inside the bit vector rep");
	
	   String desc="";
	   boolean temp=false;
	   String pattern1;
	   String pattern2;
	   ArrayList closedpattern=new ArrayList();
	   for (String lineclosed : Files.readAllLines(Paths.get(System.getProperty("user.dir") + "//src//project2//outputclosedpatternsonly.txt"))) {
	       for (String linedata : Files.readAllLines(Paths.get(System.getProperty("user.dir") + "//src//project2//DiscretizedD.txt"))) {
			   for (String partclosed : lineclosed.split(" ")) {
			   Integer closed=Integer.valueOf(partclosed);
			   for (String partdata : linedata.split(" ")) {
				   Integer data =Integer.valueOf(partdata);
				   if(closed==data)
				   {
					   temp=true;
					   break;
				   }
				   else
				   {
					   temp=false;					   
				   }
			   }  
      	    	
		   }
		   if(temp==true){
			   desc+="1"+",";
		   }
		   else
		   {
			   desc+="0"+",";
		   }
	   
	   }
		   
	   desc+="\n";
	   vectorrep.put(lineclosed, desc);
	   closedpattern.add(lineclosed);
	  write_ToBitSet(desc); 
	   desc="";
	    }
  for (String closedbits : Files.readAllLines(Paths.get(System.getProperty("user.dir") + "//src//project2//bitsetrep.txt"))) {
	    	
	    	closedpattern.add(closedbits);
	    }
	   pattern1=(String) closedpattern.get(23);
	   System.out.println("pattern1:::"+pattern1);
	   pattern2=(String) closedpattern.get(41);
	   System.out.println("pattern2:::"+pattern2);
	 jaccardSimilarity(pattern1,pattern2);
		
}

public static void jaccardSimilarity(String pattern1,String pattern2)
{
	   String patt1="";
	   String patt2="";
	   double similarity=0;
	   int intersection=0;
	   int union=0;
	   BitSet bitset1 = new BitSet();
	   List<BitSet> list = new ArrayList<BitSet>();
	   for(String partdata : pattern1.split(",") )
	   {
		   patt1+=partdata;
	   }
			 list.add(createBitSet(patt1));
			 for(String partdata : pattern2.split(",") )
			   {
				   patt2+=partdata;
			   }
			 list.add(createBitSet(patt2));
	   (list.get(0)).and(list.get(1));
	   intersection=(list.get(0)).cardinality();
	   (list.get(0)).or((list.get(1)));
	   union=(list.get(0)).cardinality();
	   similarity=((double)intersection)/((double)union);
	   System.out.println("Intersection:::"+intersection);
	   System.out.println("Union:::"+union);
	   System.out.println("Jaccard Similarity:::"+similarity);
	   
}
  
public static BitSet createBitSet(String bits)
{
    int len = bits.length();
    BitSet bs = new BitSet(len);
    for (int i = 0; i < len; i++)
    {
 	   bs.set(i, bits.charAt(len - (i + 1)) == '1');
    }
    return bs;
}

public static void write_ToBitSet(String desc_write){
    try{
      FileWriter fw = new FileWriter(System.getProperty("user.dir") + "//src//project2//bitsetrep.csv",true);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(desc_write);
      bw.close();
    }
    catch (Exception e) {
    	e.printStackTrace();
	}
}

///////////// end of second task ////////////////////


///////////// start of task1 //////////////////////////////////////////////
public static void FPClosed(double minSup) throws FileNotFoundException, IOException
{
	String input = System.getProperty("user.dir") + "//src//project2//DiscretizedD.txt"; 
	double minsup = minSup; 
	AlgoFPClose algo = new AlgoFPClose();
	String outputfile=System.getProperty("user.dir") + "//src//project2//outputclosedpatterns.csv";
	Itemsets patterns = algo.runAlgorithm(input, outputfile, minsup);  
	algo.printStats();
}
/////////////// Strat of discretizing the data /////////////////////

public static void ClosedPatterns(double minSup) throws IOException
{
	String input = System.getProperty("user.dir") + "//src//project2//DiscretizedD.txt"; 
	String output = System.getProperty("user.dir") + "//src//project2//ClosedAndMG.csv";  
	
	// Load a binary context
	TransactionDatabase context = new TransactionDatabase();
	context.loadFile(input);

	// Apply the Zart algorithm
	double minsup = minSup;
	AlgoZart zart = new AlgoZart();
	TZTableClosed results = zart.runAlgorithm(context, minsup);
	TFTableFrequent frequents = zart.getTableFrequent();
	zart.printStatistics();
	zart.saveResultsToFile(output);
}

public static void Discretize_map(String datafile) throws FileNotFoundException, IOException
{
	for (String line : Files.readAllLines(Paths.get(System.getProperty("user.dir") + "//src//project2//"+datafile))) {
        fields = line.split(",");
    
        lines.add(fields);
     }
 strings= (String[][]) lines.toArray(new String[lines.size()][]);
int i,j;
for( i=1;i<fields.length;i++){
    for(j=1;j<strings.length;j++){
    if(strings[j][i].contains(",")){
            String data_class[]=strings[j][i].split(",");
            hash.put(j,Double.parseDouble(data_class[1]));
        }
    else{
        
    hash.put(j,Double.parseDouble((strings[j][i])));
    
    }
 
    }
   
  findMax_Min(i);  
}

System.out.println("Starting to print DiscretizedD text file:::::: :)");
String desc_write="";
for( i=0;i<strings.length;i++){ //rows is fixed
	    for(j=1;j<fields.length;j++){ //iterating thru cols
    	
    	 int count1_final=0;
         int count2_final=0;
         int count3_final=0;
         int count4_final=0;
         int count5_final=0;
         Double Max_Gene=0.0;
         Double Min_Gene=Double.MAX_VALUE;
        int no_of_intervals=5;
         Max_Gene=maxvalue.get(j);
         Min_Gene = minvalue.get(j);
        Double interval=(Max_Gene-Min_Gene)/no_of_intervals;
        Double f=Min_Gene+interval;
        Double g=f+interval;
        Double h=g+interval;
        Double i1=h+interval;
      
            if( Double.parseDouble(strings[i][j]) >= Min_Gene &&  Double.parseDouble(strings[i][j]) < f){
              count1_final=((j-1)*5)+1;                 
               desc_write+=count1_final+" ";
           
        }
                              
            if( Double.parseDouble(strings[i][j]) >= f &&  Double.parseDouble(strings[i][j]) < g){
                count2_final=((j-1)*5)+2;
                desc_write+=count2_final+" ";
              
          }
          
            if( Double.parseDouble(strings[i][j]) >=g &&  Double.parseDouble(strings[i][j]) <h){
                count3_final=((j-1)*5)+3;
                desc_write+=count3_final+" ";
           
          }
          
             if( Double.parseDouble(strings[i][j]) >= h &&  Double.parseDouble(strings[i][j]) <i1){
               count4_final=((j-1)*5)+4;
               desc_write+=count4_final+" ";                    
            }
           
            
             if( Double.parseDouble(strings[i][j]) >= i1 &&  Double.parseDouble(strings[i][j]) <= Max_Gene){
               
               count5_final=((j-1)*5)+5;
               desc_write+=count5_final+" ";
             }
           
    }
    desc_write=desc_write+"\n";
  //  write_ToD(desc_write);
    desc_write="";
  
}

}

public static void findMax_Min(int j) throws IOException{

Double Max_Value=0.0;
Double Min_Value=Double.MAX_VALUE;
for(int x=1;x<hash.size();x++){
     if(Double.parseDouble(hash.get(x).toString())>=Max_Value){
        Max_Value=Double.parseDouble(hash.get(x).toString());
        
   }
}
for(int x=1;x<hash.size();x++){
    if(Min_Value > Double.parseDouble(hash.get(x).toString())){
        Min_Value=Double.parseDouble(hash.get(x).toString());
        
   }
}
maxvalue.put(j, Max_Value);
minvalue.put(j, Min_Value);
doDescritize(Max_Value,Min_Value,j);

}

public static void doDescritize(Double Max_Gene,Double Min_Gene,int g_num) throws IOException{
 int count1_final=0;
 int count2_final=0;
 int count3_final=0;
 int count4_final=0;
 int count5_final=0;
int no_of_intervals=5;
Double interval=(Max_Gene-Min_Gene)/no_of_intervals;
String write_Text="";
String desc_write="";

ArrayList<Integer> orderid= new ArrayList<>();
num_in_array = new Double[hash.size()];
 for(int a=1;a<hash.size();a++){
num_in_array[a]=Double.parseDouble(hash.get(a).toString());
}
 int count2=0;
 int count1=0;
 int count3=0;
 Double f=Min_Gene+interval;
  Double g=f+interval;
  Double h=g+interval;
  Double i=h+interval;
  Double j=i+interval;
  
for(int z=1;z<num_in_array.length;z++){
    
    if( num_in_array[z] >= Min_Gene &&  num_in_array[z] < f){
      count1_final=((g_num-1)*5)+1;
       int desc=count1_final;      
       orderid.add(count1_final);
   }
    
    if( num_in_array[z] >= f &&  num_in_array[z] < g){
        count2_final=((g_num-1)*5)+2;
         int desc=count2_final;
         orderid.add(count2_final);
                 
  }
    
    if( num_in_array[z] >=g &&  num_in_array[z] <h){
        count3_final=((g_num-1)*5)+3;
         int desc=count3_final;
         orderid.add(count3_final);
      		              
  }
    
    
     if( num_in_array[z] >= h &&  num_in_array[z] <i){
       count4_final=((g_num-1)*5)+4;
       int desc=count4_final;
       orderid.add(count4_final);
    
    }
    
     if( num_in_array[z] >= i &&  num_in_array[z] <= Max_Gene){
       
       count5_final=((g_num-1)*5)+5;
       int desc=count5_final;
       orderid.add(count5_final);    
    }
     if(count5_final==0)
     {count5_final++;}
     if(count4_final==0)
     {count4_final++;}
     if(count3_final==0)
     {count3_final++;}
     if(count2_final==0)
     {count2_final++;}
     if(count1_final==0)
     {count1_final++;}
    }
write_Text="Atrribute"+g_num+"\t"+"(-INFINITY,"+f+"]\t"+(count1_final)+"\n"+"Atrribute"+g_num+"\t"+"("+f+","+g+"]\t"+(count2_final)+"\n"+"Atrribute"+g_num+"\t"+"("+g+","+h+"]\t"+(count3_final)+"\n"+"Atrribute"+g_num+"\t"+"("+h+","+i+"]\t"+(count4_final)+"\n"+"Atrribute"+g_num+"\t"+"("+i+",INFINITY]"+"\t"+(count5_final)+"\n";

//write_ToFiles(write_Text);
 

}



public static void write_ToFiles(String write_Text){
 try{
	
   FileWriter fw = new FileWriter(System.getProperty("user.dir") + "//src//project2//DiscretizedMap.txt",true);
   BufferedWriter bw = new BufferedWriter(fw);
   bw.write(write_Text);
   bw.close();
 
    }
    catch(Exception e){
        e.printStackTrace();
    }
}

public static void write_ToD(String desc_write){
try{
  FileWriter fw = new FileWriter(System.getProperty("user.dir") + "//src//project2//DiscretizedD.txt",true);
  BufferedWriter bw = new BufferedWriter(fw);
  bw.write(desc_write);
  bw.close();
}
catch (Exception e) {
	e.printStackTrace();
}
}




//////////////////// End of Discretizing the data////////////////////
}