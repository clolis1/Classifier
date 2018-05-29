package classifier.driver;
import classifier.util.*;
import classifier.fileOperations.*;

import java.util.*;
import java.lang.Math;

public class Driver
{
    public static void main(String[] args)
    {
        Driver driver = new Driver();
        driver.checkArgs(args);
        
        DataProcessor dp = new DataProcessor(args[2]);
        
        String attributeNames[];
        
        if (dp.canReadNextLine())
        {
            String attributeNamesString = dp.readNextLine();
//            System.out.println(attributeNamesString);
            attributeNames = attributeNamesString.split(",");
        }
        else
        {
            attributeNames = new String [0];
            System.out.println("ERROR: Can't read from file!");
            System.exit(-1);
        }
        
        Vector<Example> examples = new Vector<Example>();
        
        while (dp.canReadNextLine())
        {
            String exampleValuesString = dp.readNextLine();
            String[] exampleValues = exampleValuesString.split(",");
            
            Example example = new Example();
            
            for (int i = 0; i < exampleValues.length; i++)
            {
                Attribute attribute = new Attribute(attributeNames[i], Integer.valueOf(exampleValues[i]));
                example.addAttribute(attribute);
            }
            examples.addElement(example);
        }

        Vector<Attribute> attributes = new Vector<Attribute>();
        for (int i = 0; i < attributeNames.length - 2; i++)
        {
            attributes.add(new Attribute(attributeNames[i]));
        }
        
        Attribute rootA = attributes.get(0);
        double greatestInfoGain = 1;
        for (int i = 0; i < attributes.size(); i++)
        {
            int positive = 0;
            int negative = 0;
            for (int j = 0; j < examples.size(); j++)
            {
                if (examples.get(j).returnValue(attributes.get(i)) == 0)
                {
                    negative++;
                }
                else
                {
                    positive++;
                }
            }
            double temp = ((double)positive/(double)examples.size())*(Math.log((double)positive/(double)examples.size())/Math.log(2.0)) + ((double)negative/(double)examples.size())*(Math.log((double)negative/(double)examples.size())/Math.log(2.0));
            if (temp < greatestInfoGain)
            {
                rootA = attributes.get(i);
            }
        }
        
//        System.out.println(rootA.getName());
        for (int i = 0; i < attributes.size(); i++)
        {
            if (rootA.getName().equals(attributes.get(i).getName()))
            {
                attributes.remove(i);
                i += attributes.size();
            }
        }
        
        ID3 tree = new ID3(examples, rootA, attributes);
        Node root = tree.createNode(examples, tree.getTargetAttribute(), tree.getAttributes());
        
        VIH tree2 = new VIH(examples, new Attribute(attributeNames[(attributeNames.length - 1)]), attributes, examples.size());
        Node root2 = tree2.createNode(examples, tree2.getTargetAttribute(), tree2.getAttributes());
        
        DataProcessor dp2 = new DataProcessor(args[4]);
        
        if (dp2.canReadNextLine())
        {
            String attributeNamesString = dp2.readNextLine();
            attributeNames = attributeNamesString.split(",");
        }
        else
        {
            attributeNames = new String [0];
            System.out.println("ERROR: Can't read from file!");
            System.exit(-1);
        }
        
        Vector<Example> testExamples = new Vector<Example>();
        
        while (dp2.canReadNextLine())
        {
            String exampleValuesString = dp2.readNextLine();
            String[] exampleValues = exampleValuesString.split(",");
            
            Example example = new Example();
            
            for (int i = 0; i < exampleValues.length; i++)
            {
                Attribute attribute = new Attribute(attributeNames[i], Integer.valueOf(exampleValues[i]));
                example.addAttribute(attribute);
            }
            testExamples.addElement(example);
        }
        
        Pruner pruner = new Pruner(examples, testExamples);
        Node prunedID3 = pruner.prune(root, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        Node prunedVIH = pruner.prune(root, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        
        if (args[5].equals("yes"))
        {
            ScreenPrinter printer = new ScreenPrinter();
            System.out.println("The decision tree as implemented by the Information Gain heuristic... and pruned!");
            printer.printTree(prunedID3);
        }
        
        Accuracy acc = new Accuracy(testExamples);
            
        double accuracyID = acc.getAccuracy(prunedID3);
        double accuracyVI = acc.getAccuracy(prunedVIH);
        
        System.out.println("The accuracy of the Information Gain heuristic with pruning based on the test set is " + accuracyID*100 + "%.");
        System.out.println("The accuracy of the Variance Impurity heuristic with pruning based on the test set is " + accuracyVI*100 + "%.");
        
        NB bayes = new NB();
        bayes.trainAndTestMultinomialNB();
    }
    
    public void checkArgs(String[] args)
    {
        if(args.length != 6){
            System.err.println("ERROR: Please enter 6 arguments. They should be: <L> <K> <training set> <validation set> <test set> <yes/no>");
			System.exit(-1);
        }
    }
}
