package classifier.util;

import java.util.*;

public class Node
{
    Node parent;
    Node leftChild;
    Node rightChild;
    boolean isRoot = false;
    boolean hasLabel = false;
    boolean isLeaf = false;
    
    int label;
    
    int leaf;
    
    int marker;
    
    Attribute attribute;
    
    public Node()
    {
        label = -1;
    }
    
    public void setParent(Node parentIn)
    {
        parent = parentIn;
    }
    
    public void setLeftChild(Node leftChildIn)
    {
//        System.out.println("I'm here!");        
        leftChild = leftChildIn;
    }
    
    public void setRightChild(Node rightChildIn)
    {
        rightChild = rightChildIn;
    }
    
    public void setAsRoot()
    {
        isRoot = true;
    }
    
    public void setAttribute(Attribute attributeIn)
    {
        attribute = attributeIn;
    }
    
    public Node getParent()
    {
        return parent;
    }
    
    public Node getLeftChild()
    {
        return leftChild;
    }
    
    public Node getRightChild()
    {
        return rightChild;
    }
    
    public Attribute getAttribute()
    {
        return attribute;
    }
    
    public void setLeaf(int leafIn)
    {
        leaf = leafIn;
        isLeaf = true;
    }
    
    public int getLeaf()
    {
        return leaf;
    }
    
    public void setLabel(int labelIn)
    {
        label = labelIn;
        hasLabel = true;
    }
    
    public int getLabel()
    {
        return label;
    }
    
    public boolean isRoot()
    {
        return this.isRoot;
    }
    
    public boolean hasLabel()
    {
        return this.hasLabel;
    }
    
    public boolean isLeaf()
    {
        return this.isLeaf;
    }
    
    @Override
    public Node clone()
    {
        Node clone = new Node();
        clone.isRoot = this.isRoot;
        clone.hasLabel = this.hasLabel;
        clone.isLeaf = this.isLeaf;
        
        clone.label = this.label;
        clone.leaf = this.leaf;
        
        clone.attribute = this.attribute.clone();
        
        if (this.getLeftChild() != null)
        {
            clone.setLeftChild(this.getLeftChild().clone());
            clone.getLeftChild().setParent(clone);
        }
        else
        {
            this.setLeftChild(null);
        }
        
        if (this.getRightChild() != null)
        {
            clone.setRightChild(this.getRightChild().clone());
            clone.getRightChild().setParent(clone);
        }
        else
        {
            this.setRightChild(null);
        }
        
        return clone;
    }
    
    public void setMarker(int markerIn)
    {
        marker = markerIn;
    }
    
    public int getMarker()
    {
        return marker;
    }
}