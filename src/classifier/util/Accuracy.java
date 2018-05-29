package classifier.util;

import java.util.*;

public class Accuracy
{
    Vector<Example> testSet;
    int correctPredictions;
    
    public Accuracy(Vector<Example> testSetIn)
    {
        testSet = testSetIn;
        correctPredictions = 0;
    }
    
    public double getAccuracy(Node root)
    {
        correctPredictions = 0;
        for (int i = 0; i < testSet.size(); i++)
        {
            if (this.checkExample(testSet.get(i), root))
            {
                correctPredictions++;
            }
        }
        return (double)correctPredictions/(double)testSet.size();
    }
    
    public boolean checkExample(Example example, Node node)
    {
        if (node.getLeftChild() != null || node.getRightChild() != null)
        {
            if (example.returnValue(node.getAttribute()) == 0)
            {
                return this.checkExample(example, node.getLeftChild());
            }
            else
            {
                return this.checkExample(example, node.getRightChild());
            }
        }
        else if (node.getLabel() == example.getClassValue())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}