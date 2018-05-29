package classifier.util;

import java.util.*;
import java.lang.Math;

public class VIH
{
    Vector<Example> examples;
    Attribute target_attribute;
    Vector<Attribute> attributes;
    int mostCommonResult;
    boolean hasRoot = false;
    int totalExamples;
    
    public VIH(Vector<Example> examplesIn, Attribute target_attributeIn, Vector<Attribute> attributesIn, int totalExamplesIn)
    {
        examples = examplesIn;
        target_attribute = target_attributeIn;
        attributes = attributesIn;
        
        int negatives = 0;
        int positives = 0;
        int total = 0;
        
        for (int i = 0; i < examples.size(); i++)
        {
            total++;
            
            if (examples.get(i).returnValue(target_attribute) == 0)
            {
                negatives++;
            }
            else
            {
                positives++;
            }
        }
        
        if (negatives > positives)
        {
            mostCommonResult = 0;
        }
        else
        {
            mostCommonResult = 1;
        }
        
        totalExamples = totalExamplesIn;
    }
    
    public Node createNode(Vector<Example> subSet, Attribute target, Vector<Attribute> others)
    {
//        System.out.println("createNode starts");
        Node root = new Node();
        
        if (hasRoot == false)
        {
            root.setAsRoot();
            hasRoot = true;
        }
        
        if (others.size() == 0)
        {
            root.setLeaf(mostCommonResult);
            root.setLabel(mostCommonResult);
            return root;
        }
        
        int negatives = 0;
        int positives = 0;
        int total = 0;
        
        for (int i = 0; i < subSet.size(); i++)
        {
            total++;
            
            if (subSet.get(i).getClassValue() == 0)
            {
                negatives++;
            }
            else
            {
                positives++;
            }
        }
        
        //If there are no negatives or positives, simply return the root.
        if (negatives == 0)
        {
//            System.out.println("Hello.");
            root.setLabel(1);
            root.setAttribute(target);
            root.setLeaf(1);
            return root;
        }
        else if (positives == 0)
        {
//            System.out.println("Hello..");
            root.setLabel(0);
            root.setAttribute(target);
            root.setLeaf(0);
            return root;
        }
        
        // Create new Example subsets
        Vector<Example> positiveSubset = new Vector<Example>();
        Vector<Example> negativeSubset = new Vector<Example>();
        
        for (int i = 0; i < subSet.size(); i++)
        {
            if (subSet.get(i).getClassValue() == 0)
            {
                negativeSubset.addElement(subSet.get(i));
            }
            else
            {
                positiveSubset.addElement(subSet.get(i));
            }
        }
        
        double targetImpurity = this.calculateImpurity(subSet.size(), positiveSubset.size(), negativeSubset.size());
        
        double greatestInfoGain = 0;
        
        Vector<Example> leftwardSubset = new Vector<Example>();
        Vector<Example> rightwardSubset = new Vector<Example>();
        
        Attribute newAttribute = new Attribute("dummy");
        
        for (int i = 0; i < others.size(); i++)
        {
            Vector<Example> leftSubset = new Vector<Example>();
            Vector<Example> rightSubset = new Vector<Example>();
            
            int negAttribute_negClass = 0;
            int negAttribute_posClass = 0;
            int posAttribute_negClass = 0;
            int posAttribute_posClass = 0;
            for (int j = 0; j < subSet.size(); j++)
            {
                if (subSet.get(j).returnValue(others.get(i)) == 0 && subSet.get(j).getClassValue() == 0)
                {
                    leftSubset.addElement(subSet.get(j));
                    negAttribute_negClass++;
                }
                else if (subSet.get(j).returnValue(others.get(i)) == 0 && subSet.get(j).getClassValue() == 1)
                {
                    leftSubset.addElement(subSet.get(j));
                    negAttribute_posClass++;
                }
                else if (subSet.get(j).returnValue(others.get(i)) == 1 && subSet.get(j).getClassValue() == 0)
                {
                    rightSubset.addElement(subSet.get(j));
                    posAttribute_negClass++;
                }
                else
                {
                    rightSubset.addElement(subSet.get(j));
                    posAttribute_posClass++;
                }
            }
            
            double otherNegImpurity;
            double otherPosImpurity;
            
            if (leftSubset.size() > 0)
            {
                otherNegImpurity = this.calculateImpurity(leftSubset.size(), negAttribute_posClass, negAttribute_negClass);
            }
            else
            {
                otherNegImpurity = 0;
            }
            
            if (rightSubset.size() > 0)
            {
                otherPosImpurity = this.calculateImpurity(rightSubset.size(), posAttribute_posClass, posAttribute_negClass);
            }
            else
            {
                otherPosImpurity = 0;
            }
            
            double tempInfoGain = targetImpurity - ((double)rightSubset.size()/(double)subSet.size())*otherPosImpurity - ((double)leftSubset.size()/(double)subSet.size())*otherNegImpurity;
//            System.out.println("tempInfoGain is " + tempInfoGain + ", GreatestInfoGain is " + greatestInfoGain);
//            System.out.println("Impurity(sNegative) is " + otherNegImpurity + ", Impurity(sPositive) is " + otherPosImpurity);
            if (tempInfoGain > greatestInfoGain)
            {
                newAttribute = others.get(i);
                greatestInfoGain = tempInfoGain;
                leftwardSubset = leftSubset;
                rightwardSubset = rightSubset;
            }
        }
        
        // Create new Attribute Vector
        Vector<Attribute> newOthers = (Vector<Attribute>)others.clone();
        for (int i = 0; i < newOthers.size(); i++)
        {
            if (newAttribute.getName().equals(newOthers.get(i).getName()))
            {
                newOthers.remove(i);
                i += newOthers.size();
            }
        }
        
        root.setAttribute(newAttribute);
        
//        System.out.println("Creating a left node");
        
        root.setLeftChild(this.createNode(leftwardSubset, newAttribute, newOthers));
        root.getLeftChild().setParent(root);
        if (root.getLeftChild().isLeaf())
        {
            root.setLabel(root.getLeftChild().getLabel());
        }
        
//        System.out.println("Creating a right node");
        root.setRightChild(this.createNode(rightwardSubset, newAttribute, newOthers));
        root.getRightChild().setParent(root);
        if (root.getRightChild().isLeaf())
        {
            root.setLabel(root.getRightChild().getLabel());
        }
        
        this.cutLooseEnds(root);
        
        return root;
    }
    
    public double calculateImpurity(int setSize, int numPositives, int numNegatives)
    {
        double answer;
        if (numPositives == 0 || numNegatives == 0)
        {
            return 0;
        }
        answer = (double)(numPositives/setSize)*(double)(numNegatives/setSize)*(double)(numPositives/totalExamples) + 
                 (double)(numPositives/setSize)*(double)(numNegatives/setSize)*(double)(numNegatives/totalExamples);
        return answer;
    }
    
    public Attribute getTargetAttribute()
    {
        return target_attribute;
    }
    
    public Vector<Attribute> getAttributes()
    {
        return attributes;
    }
    
    public void cutLooseEnds(Node node)
    {
        if (node.getAttribute().getName().equals(node.getLeftChild().getAttribute().getName()) &&
            node.getAttribute().getName().equals(node.getRightChild().getAttribute().getName()) )
        {
            node.setLeaf(node.getLeftChild().getLabel());
//            node.setLeftChild(null);
//            node.setRightChild(null);
        }
    }
}