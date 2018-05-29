package classifier.util;

import java.util.*;
import java.lang.Math;

public class Pruner
{
    Vector<Example> examples;
    Vector<Example> testSet;
    
    public Pruner(Vector<Example> examplesIn, Vector<Example> testSetIn)
    {
        examples = examplesIn;
        testSet = testSetIn;
    }
    
    public Node prune(Node root, int L, int K)
    {
        Node bestNewRoot = null;
        double bestAcc = 0.0;
        
        for (int i = 1; i < L; i++)
        {
            Node pruned = root.clone();
            Random rand = new Random();
            
            int M = 1 + rand.nextInt(K);
            
            for (int j = 1; j < M; j++)
            {
                int numNonLeafs = this.countNodes(pruned, 0);
//                System.out.println(numNonLeafs);
                if (numNonLeafs != 0)
                {
                    int P = 1 + rand.nextInt(numNonLeafs);
                    
//                    System.out.println("numNonLeafs is " + numNonLeafs);
                    this.replaceAt(pruned, pruned, P);
                }
            }
            
            Accuracy acc = new Accuracy(testSet);
            
            double accuracyP = acc.getAccuracy(pruned);
//            System.out.println("The new accuracy is " + accuracyP);
            
            if (accuracyP > bestAcc)
            {
                bestNewRoot = pruned;
                bestAcc = accuracyP;
            }
        }
        if (bestNewRoot != null)
        {
            return bestNewRoot;
        }
        else 
        {
            return root;
        }
    }
    
    //Returns the number of non-leaf nodes starting from some root
    public int countNodes(Node root, int counter)
    {
        if (root.getLeftChild() != null)
        {
            counter =  this.countNodes(root.getLeftChild(), counter);
        }
        
        if (!root.isLeaf())
        {
            counter++;
            root.setMarker(counter);
        }
        
        if (root.getRightChild() != null)
        {
            counter = this.countNodes(root.getRightChild(), counter);
        }
        return counter;
    }
    
    //during first iteration, root and toBePruned should be same node
    public void replaceAt(Node root, Node toBePruned, int position)
    {
        if (root.isLeaf())
        {
            return;
        }
        if (toBePruned.getMarker() == position)
        {
            toBePruned.setLeftChild(null);
            toBePruned.setRightChild(null);
            
            Vector<Example> temp = (Vector<Example>)examples.clone();
            
            toBePruned.setLabel(this.findMajorityClass(root, toBePruned, temp));
            toBePruned.setLeaf(this.findMajorityClass(root, toBePruned, temp));
        }
        else if (toBePruned.getMarker() > position)
        {
            if (toBePruned.getLeftChild() == null)
            {
                return;
            }
            this.replaceAt(root, toBePruned.getLeftChild(), position);
        }
        else
        {
            if (toBePruned.getRightChild() == null)
            {
                return;
            }
            this.replaceAt(root, toBePruned.getRightChild(), position);
        }
    }
    
    public int findMajorityClass(Node root, Node toBePruned, Vector<Example> prunedExamples)
    {
        if (root.getMarker() == toBePruned.getMarker())
        {
            int positives = 0;
            int negatives = 0;
            
            for (int i = 0; i < prunedExamples.size(); i++)
            {
                if (prunedExamples.get(i).getClassValue() == 0)
                {
                    negatives++;
                }
                else
                {
                    positives++;
                }
            }
            
            if (positives > negatives)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        else if (root.getMarker() > toBePruned.getMarker())
        {
            for (int i = 0; i < prunedExamples.size(); i++)
            {
                if (prunedExamples.get(i).returnValue(root.getAttribute()) == 1)
                {
                    prunedExamples.remove(i);
                    i--;
                }
            }
            return this.findMajorityClass(root.getLeftChild(),toBePruned, prunedExamples);
        }
        else
        {
            for (int i = 0; i < prunedExamples.size(); i++)
            {
                if (prunedExamples.get(i).returnValue(root.getAttribute()) == 0)
                {
                    prunedExamples.remove(i);
                    i--;
                }
            }
            return this.findMajorityClass(root.getRightChild(),toBePruned, prunedExamples);
        }
    }
}