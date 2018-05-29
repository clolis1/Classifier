package classifier.util;

import java.util.*;
import java.lang.Math;
import java.io.File;

import classifier.fileOperations.*;

//NB is Naive Bayes
public class NB
{
    Vector<File> files;            //Vector of files from a folder
    Vector<String> vocabulary;     //Holds all words
    Vector<String> hamWords;       //Holds all words in ham
    Vector<String> spamWords;      //Holds all words in spam
    Vector<String> stopWords;      //Holds all stop words
    Vector<Word> vocabularyWords;  //Holds all Words; no duplicates
    
    int numDocs;                   //Total number of documents
    int hamDocs;                   //Total number of ham documents
    int spamDocs;                  //Total number of spam documents
    
    public NB()
    {
        files = new Vector<File>();
        vocabulary = new Vector<String>();
        hamWords = new Vector<String>();
        spamWords = new Vector<String>();
        stopWords = new Vector<String>();
        vocabularyWords = new Vector<Word>();
        numDocs = 0;
        hamDocs = 0;
        spamDocs = 0;
    }
    
    //For the purposes of this homework, this method creates the class vector and the document vector from within
    public void trainAndTestMultinomialNB()
    {
        List<String> results = new ArrayList<String>();

        File[] files = new File("src/classifier/train/ham/").listFiles();
        
        File stopwords = new File("stopwords.txt");
        
        DataProcessor dp = new DataProcessor(stopwords);
        
        while (dp.canReadNext())
        {
            String temp = dp.readNext();
            temp = temp.replaceAll("[^A-Za-z]+", "");
            if (!temp.equals(""))
            {
                stopWords.addElement(temp);
            }
        }
        
        for (File file : files)
        {
            hamDocs++;
            numDocs++;
            dp = new DataProcessor(file);
            while (dp.canReadNext())
            {
                String temp = dp.readNext();
                temp = temp.replaceAll("[^A-Za-z]+", "");
                
                for (int i = 0; i < stopWords.size(); i++)
                {
                    if (!temp.equals(stopWords.get(i)) && !temp.equals(""))
                    {
                        hamWords.addElement(temp);
                        vocabulary.addElement(temp);
                    }
                }
            }
        }
        
        files = new File("src/classifier/train/spam/").listFiles();
        
        for (File file : files)
        {
            spamDocs++;
            numDocs++;
            dp = new DataProcessor(file);
            while (dp.canReadNext())
            {
                String temp = dp.readNext();
                temp = temp.replaceAll("[^A-Za-z]+", "");
                
                for (int i = 0; i < stopWords.size(); i++)
                {
                    if (!temp.equals(stopWords.get(i)) && !temp.equals(""))
                    {
                        spamWords.addElement(temp);
                        vocabulary.addElement(temp);
                    }
                }
            }
        }
        
        //Priors for both Ham and Spam classes
        double priorHam = (double)hamDocs/(double)numDocs;
        double priorSpam = (double)spamDocs/(double)numDocs;
        
        //Obfuscating code that stores all words and how many times they were used in 
        //documents of each class.
        for (int i = 0; i < hamWords.size(); i++)
        {
            boolean notYetAdded = true;
            
            for (int j = 0; j < vocabularyWords.size(); j++)
            {
                if (hamWords.get(i).equals(vocabularyWords.get(j).getWord()))
                {
                    notYetAdded = false;
                    vocabularyWords.get(j).inHamDoc();
                }
            }
            if (notYetAdded)
            {
                Word newWord = new Word(hamWords.get(i));
//                System.out.println(newWord.getWord());
                vocabularyWords.add(newWord);
            }
        }
        
//        System.out.println("Hello!");
        
        for (int i = 0; i < spamWords.size(); i++)
        {
            boolean notYetAdded = true;
            
            for (int j = 0; j < vocabularyWords.size(); j++)
            {
                if (spamWords.get(i).equals(vocabularyWords.get(j).getWord()))
                {
                    notYetAdded = false;
                    vocabularyWords.get(j).inSpamDoc();
                }
            }
            if (notYetAdded)
            {
                Word newWord = new Word(spamWords.get(i));
                vocabularyWords.add(newWord);
            }
        }
        
        //Determing Conditional probability for each word in each class
        for (int i = 0; i < vocabularyWords.size(); i++)
        {
            double tempHam = ((double)vocabularyWords.get(i).getHamInstances() + 1.0)/((double)hamWords.size() + (double)vocabularyWords.size());
            vocabularyWords.get(i).setHamCondProb(tempHam);
            
            double tempSpam = ((double)vocabularyWords.get(i).getSpamInstances() + 1.0)/((double)spamWords.size() + (double)vocabularyWords.size());
            vocabularyWords.get(i).setSpamCondProb(tempSpam);
        }
        
        int numTests = 0;
        int numCorrect = 0;
        
        double hamScore = 0;
        double spamScore = 0;
        
        files = new File("src/classifier/test/ham/").listFiles();
        
        Vector<String> testWords = new Vector<String>();
        
        for (File file : files)
        {
            hamScore = 0;
            spamScore = 0;
            
            dp = new DataProcessor(file);
            while (dp.canReadNext())
            {
                String temp = dp.readNext();
                temp = temp.replaceAll("[^A-Za-z]+", "");
                
                for (int i = 0; i < stopWords.size(); i++)
                {
                    if (!temp.equals(stopWords.get(i)) && !temp.equals(""))
                    {
                        testWords.addElement(temp);
                    }
                }
            }
            
            hamScore  += Math.log(priorHam);
            spamScore += Math.log(priorSpam);
            
            for (int i = 0; i < testWords.size(); i++)
            {
                for (int j = 0; j < vocabularyWords.size(); j++)
                {
                    if (testWords.get(i).equals(vocabularyWords.get(j).getWord()))
                    {
                        hamScore  += Math.log(vocabularyWords.get(j).getHamCondProb());
                        spamScore += Math.log(vocabularyWords.get(j).getSpamCondProb());
                    }
                }
            }
            
            numTests++;
            if (hamScore > spamScore)
            {
                numCorrect++;
            }
            
            testWords.removeAllElements();
        }
        
        files = new File("src/classifier/test/spam/").listFiles();
        
        for (File file : files)
        {
            hamScore = 0;
            spamScore = 0;
            
            dp = new DataProcessor(file);
            while (dp.canReadNext())
            {
                String temp = dp.readNext();
                temp = temp.replaceAll("[^A-Za-z]+", "");
                
                for (int i = 0; i < stopWords.size(); i++)
                {
                    if (!temp.equals(stopWords.get(i)) && !temp.equals(""))
                    {
                        testWords.addElement(temp);
                    }
                }
            }
            
            hamScore  += Math.log(priorHam);
            spamScore += Math.log(priorSpam);
            
            for (int i = 0; i < testWords.size(); i++)
            {
                for (int j = 0; j < vocabularyWords.size(); j++)
                {
                    if (testWords.get(i).equals(vocabularyWords.get(j).getWord()))
                    {
                        hamScore  += Math.log(vocabularyWords.get(j).getHamCondProb());
                        spamScore += Math.log(vocabularyWords.get(j).getSpamCondProb());
                    }
                }
            }
            
            numTests++;
            if (spamScore > hamScore)
            {
                numCorrect++;
            }
            
            testWords.removeAllElements();
        }
        
        System.out.println("The accuracy of the Naive Bayes algorithm for text classification is " + ((double)numCorrect)/((double)numTests));
    }
}