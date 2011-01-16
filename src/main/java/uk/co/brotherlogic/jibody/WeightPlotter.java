package uk.co.brotherlogic.jibody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

/**
 * Hello world!
 *
 */
public class WeightPlotter 
{
   public List<Weight> parseFile(File f) throws IOException
   {
      List<Weight> weight = new LinkedList<Weight>();
      FileInputStream reader = new FileInputStream(f);
      for(int j = 0; j < 30 ; j++)
      {
      int y1  = reader.read();
      int y2 = reader.read();
      
      int year = y1*256+y2;
      int month = reader.read();
      int day = reader.read();
      int hour = reader.read();
      int minute = reader.read();
      int second = reader.read();
      
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR,year);
      cal.set(Calendar.MONTH,month-1);
      cal.set(Calendar.DAY_OF_MONTH,day);
      cal.set(Calendar.HOUR,hour);
      cal.set(Calendar.MINUTE,minute);
      cal.set(Calendar.SECOND,second);
      
      for(int i = 0 ; i < 3 ; i++)
         reader.read();
      
      int w1 = reader.read();
      int w2 = reader.read();
      double weightInKg = (w1*256+w2)/10.0;
      double weightInStones = weightInKg*2.2;
      
      if (weightInStones > 0 && cal.getTimeInMillis() > 0)
         weight.add(new Weight(cal.getTimeInMillis(),weightInStones));
      
      for(int i = 0 ; i < 6 ; i++)
         reader.read();
      }
      
        return weight;
   }
   
   public List<Weight> getAllWeights() throws IOException
   {
      File f = new File(System.getProperty("user.home") + File.separator + "local" + File.separator + "Dropbox" + File.separator + "weight");
      List<Weight> allweights = new LinkedList<Weight>();
      for(File wf : f.listFiles())
         allweights.addAll(parseFile(wf));
      return allweights;
   }
   
    public static void main( String[] args ) throws IOException
    {
        WeightPlotter plotter = new WeightPlotter();
        List<Weight> all = plotter.getAllWeights();
        for(Weight w : all)
           System.out.println(w);
    }
}

class Weight
{
   long timestamp;
   double weight;
   
   public Weight(long time, double w)
   {
      timestamp = time;
      weight = w;
   }
   
   public String toString()
   {
      return timestamp + " " +  weight;
   }
}
