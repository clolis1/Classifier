package classifier.fileOperations;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public final class DataProcessor
{
    Scanner sc = null;
    String fileName;
    
    public DataProcessor(String fileNameIn){
        fileName = fileNameIn;
        
        try{
            sc = new Scanner(new File(fileName));
        } catch(FileNotFoundException e){
            e.printStackTrace();
            System.exit(-1);
        } finally{
            
        }
    }
    
    public DataProcessor(File file)
    {
        try
        {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            
        }
    }
    
    /**
     *  This method returns true if the scanner can read the next line in the file.
     */
    public synchronized boolean canReadNextLine(){
        if(sc.hasNextLine()){
            return true;
        }
        return false;
    }
    
    public synchronized boolean canReadNext()
    {
        if (sc.hasNext())
        {
            return true;
        }
        return false;
    }
    
    /**
     *  This method reads a line from a file and returns the line as a string.
     *  
     */
    public synchronized String readNextLine(){
        return sc.nextLine();
    }
    
    public synchronized String readNext()
    {
        return sc.next();
    }
}