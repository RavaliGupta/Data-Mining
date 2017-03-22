package project2;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Discretize_map {


     HashMap<Integer, Double> hash = new HashMap<>();
     HashMap<Integer, Double> maxvalue = new HashMap<>();
     HashMap<Integer, Double> minvalue = new HashMap<>();
     ArrayList lines = new ArrayList(); 
     static String[] fields;
     String[][] strings;
     ArrayList lines1 = new ArrayList(); 
     
     String[] strings1;
     Double[] num_in_array;
    /* static int count1_final=0;
     static int count2_final=0;
     static int count3_final=0;
     static int count4_final=0;
     static int count5_final=0;*/
    public Discretize_map(String data_Path,int k) throws FileNotFoundException, IOException
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("BANK_MARKET.txt")));
        for(String line = br.readLine();line != null;line = br.readLine()) {
            
                fields = line.split(",");
            
                lines.add(fields);
             }
         strings= (String[][]) lines.toArray(new String[lines.size()][]);
        int i,j;
        System.out.println("lenght of fields:::"+fields.length);
        for( i=1;i<fields.length;i++){
            for(j=1;j<strings.length;j++){
       //    System.out.println(Double.parseDouble(strings[j][i]));
            if(strings[j][i].contains(",")){
               // System.out.println("------------>");
                    String data_class[]=strings[j][i].split(",");
                    hash.put(j,Double.parseDouble(data_class[1]));
                    //genes.add(Double.parseDouble((data_class[0])));
                 //   System.out.println(j+" : "+Double.parseDouble(data_class[0]));
                    
                }
            else{
                
            hash.put(j,Double.parseDouble((strings[j][i])));
             //genes.add(Double.parseDouble((strings[j][i])));
                
            }
          //  findMax_Min(i,k); 
            
            }
           
          findMax_Min(i);  
        }

System.out.println("Starting to print DiscretizedD text file:::::: :)");
        String desc_write="";
        System.out.println("Length of strings:::::" +strings.length);
        for( i=0;i<strings.length;i++){ //rows is fixed
        	System.out.println("value of i:::::****"+i);
        	
            for(j=1;j<fields.length;j++){ //iterating thru cols
            	
            	///////////////////////***************//////////////////////////////
            	
            	int count1_final=0;
                 int count2_final=0;
                 int count3_final=0;
                 int count4_final=0;
                 int count5_final=0;
                 Double Max_Gene=0.0;
                 Double Min_Gene=Double.MAX_VALUE;
                int no_of_intervals=5;
                 Max_Gene=maxvalue.get(j);
               System.out.println("Max_Gene:::"+Max_Gene);
                 Min_Gene = minvalue.get(j);
                System.out.println("Min_Gene:::"+Min_Gene);
            
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
                   /* else
                    {
                    	 count2_final=(j*5)+2;                 
                         desc_write+=count2_final+", ";
                    }*/
                    if( Double.parseDouble(strings[i][j]) >=g &&  Double.parseDouble(strings[i][j]) <h){
                        count3_final=((j-1)*5)+3;
                        desc_write+=count3_final+" ";
                   
                  }
                   /* else
                    {
                    	 count3_final=(j*5)+3;                 
                         desc_write+=count3_final+", ";
                    }*/
                    
                     if( Double.parseDouble(strings[i][j]) >= h &&  Double.parseDouble(strings[i][j]) <i1){
                       count4_final=((j-1)*5)+4;
                       desc_write+=count4_final+" ";                    
                    }
                    /* else
                     {
                     	 count4_final=(j*5)+4;                 
                          desc_write+=count4_final+", ";
                     }*/
                    
                     if( Double.parseDouble(strings[i][j]) >= i1 &&  Double.parseDouble(strings[i][j]) <= Max_Gene){
                       
                       count5_final=((j-1)*5)+5;
                       desc_write+=count5_final+" ";
                     }
                   /*  else
                     {
                     	 count1_final=(j*5)+5;                 
                          desc_write+=count5_final+", ";
                     }*/
                 
            }
            desc_write=desc_write+"\n";
            write_ToD(desc_write);
            desc_write="";
          
        }
       
    }
    
     public void findMax_Min(int j) throws IOException{
        
        Double Max_Value=0.0;
        Double Min_Value=Double.MAX_VALUE;
        
        System.out.println("------>"+hash.size());
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
        System.out.println("Max_Value----------->"+Max_Value);
        System.out.println("Min_Value----------->"+Min_Value);
        doDescritize(Max_Value,Min_Value,j);
        
    }
    
    public void doDescritize(Double Max_Gene,Double Min_Gene,int g_num) throws IOException{
    	 int count1_final=0;
         int count2_final=0;
         int count3_final=0;
         int count4_final=0;
         int count5_final=0;
        int no_of_intervals=5;
        Double interval=(Max_Gene-Min_Gene)/no_of_intervals;
        String write_Text="";
        String desc_write="";
       // HashMap<Integer, Integer> orderid = new HashMap();
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
            		   //"a"+", ";
            //   desc_write=desc_write+"\t"+desc;
                       
        }
            
            if( num_in_array[z] >= f &&  num_in_array[z] < g){
                count2_final=((g_num-1)*5)+2;
                 int desc=count2_final;
                 orderid.add(count2_final);
               //  desc_write=desc_write+"\t"+desc;
                         
          }
            
            if( num_in_array[z] >=g &&  num_in_array[z] <h){
                count3_final=((g_num-1)*5)+3;
                 int desc=count3_final;
                 orderid.add(count3_final);
              		   //"a"+", ";
                 //desc_write=desc_write+"\t"+desc;
                         
          }
            
             if( num_in_array[z] >= h &&  num_in_array[z] <i){
               count4_final=((g_num-1)*5)+4;
            		   //((g_num*3)+4);
               int desc=count4_final;
               orderid.add(count4_final);
               //desc_write=desc_write+"\t"+desc;       
            
            }
            
             if( num_in_array[z] >= i &&  num_in_array[z] <= Max_Gene){
               
               count5_final=((g_num-1)*5)+5;
               int desc=count5_final;
               orderid.add(count5_final);
                		//"c"+", ";
              // desc_write=desc_write+"\t"+desc;
            
            }
             
            }
       write_Text="Atrribute"+g_num+"\t"+"(-INFINITY,"+f+"]\t"+(count1_final)+"\n"+"Atrribute"+g_num+"\t"+"("+f+","+g+"]\t"+(count2_final)+"\n"+"Atrribute"+g_num+"\t"+"("+g+","+h+"]\t"+(count3_final)+"\n"+"Atrribute"+g_num+"\t"+"("+h+","+i+"]\t"+(count4_final)+"\n"+"Atrribute"+g_num+"\t"+"("+i+",INFINITY]"+"\t"+(count5_final)+"\n";
      // Set<Integer> hs = new HashSet<>();
     //  hs.addAll(orderid);
     //  orderid.clear();
     //  orderid.addAll(hs);
       
       
      /* for(int k=0;k<orderid.size();k++){
       desc_write=desc_write+orderid.get(k);
       desc_write=desc_write +",";
      
        }
       desc_write=desc_write+"\n";
       System.out.println("\n");*/
       write_ToFiles(write_Text);
         //writeAccFile(k);
       // write_Text=write_Text+"Atrribute "+g_num+"\t"+"(-INFINITY,"+f+"]\t"+(count3_final);
        //write_ToFiles(write_Text,desc_write); 
        
    }
    
  /*  public void writeClass_ToFiles(int k){
        int i,j=0;
        for( i=0;i<fields.length;i++){
            for(j=0;j<strings.length;j++){
           // System.out.println(Double.parseDouble(strings[j][i]));
            if(strings[j][i].contains(",")){
                System.out.println("------------>");
                    String data_class[]=strings[j][i].split(",");
                   
                    
                    if(data_class[1].equalsIgnoreCase("positive")){
                        String classVariable=data_class[1]+"\n";
                        write_ToFiles("", classVariable);
                    }
                    else if(data_class[1].equalsIgnoreCase("negative")){
                         String classVariable=data_class[1]+"\n";
                        write_ToFiles("", classVariable);
                    }
                    
                }
            
            }
            
          
            
        }
    }*/
    
    public void write_ToFiles(String write_Text){
         try{
           FileWriter fw = new FileWriter("C:\\MS-Summer\\Data Mining\\DataMining Workspace\\Project2\\src\\fpgrowth\\DiscretizedMap.txt",true);
           BufferedWriter bw = new BufferedWriter(fw);
           bw.write(write_Text);
           bw.close();
         /*  FileWriter fd = new FileWriter("C:\\MS-Summer\\Data Mining\\DataMining Workspace\\Project2\\src\\fpgrowth\\DiscretizedD.txt",true);
           BufferedWriter bd = new BufferedWriter(fd);
           bd.write(desc_write);
           bd.close();     
         */  System.out.println("------->Done<----------------");
            }
            catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void write_ToD(String desc_write){
        try{
          FileWriter fw = new FileWriter("C:\\MS-Summer\\Data Mining\\DataMining Workspace\\Project2\\src\\fpgrowth\\DiscretizedD.txt",true);
          BufferedWriter bw = new BufferedWriter(fw);
          bw.write(desc_write);
          bw.close();
        }
        catch (Exception e) {
        	e.printStackTrace();
		}
    }
    
    
   public static void main(String args[]) throws FileNotFoundException, IOException {
	   
	   String data_path="";
	   int k =21;
	   Discretize_map df=new Discretize_map(data_path,k);
	
}
   
    }
    

