package classifier.util;

import java.util.*;

public class Word
{
    String name;
    int hamInstances;
    int spamInstances;
    
    double hamCondProb;
    double spamCondProb;
    
    public Word(String nameIn)
    {
        name = nameIn;
        hamInstances = 0;
        spamInstances = 0;
        hamCondProb = 0.0;
        spamCondProb = 0.0;
    }
    
    public String getWord()
    {
        return name;
    }
    
    public void inHamDoc()
    {
        hamInstances++;
    }
    
    public void inSpamDoc()
    {
        spamInstances++;
    }
    
    public int getHamInstances()
    {
        return hamInstances;
    }
    
    public int getSpamInstances()
    {
        return spamInstances;
    }
    
    public void setHamCondProb(double x)
    {
        hamCondProb = x;
    }
    
    public void setSpamCondProb(double x)
    {
        spamCondProb = x;
    }
    
    public double getHamCondProb()
    {
        return hamCondProb;
    }
    
    public double getSpamCondProb()
    {
        return spamCondProb;
    }
}