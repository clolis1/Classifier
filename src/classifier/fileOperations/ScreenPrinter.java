package classifier.fileOperations;

import classifier.util.*;

public class ScreenPrinter
{
    public ScreenPrinter()
    {
        
    }
    
    public void printTree(Node node)
    {
        Node temp = node;
        
        if (node == null) {System.out.println("WTF");}
        while (temp.isRoot() == false)
        {
            System.out.print("| ");
            temp = temp.getParent();
        }
        
        System.out.print(node.getAttribute().getName() + " = 0 :");
        if (node.getLeftChild() == null || node.getLeftChild().isLeaf())
        {
            System.out.println(" 0");
            
        }
        else
        {
            System.out.println("");
            this.printTree(node.getLeftChild());
        }
        
        temp = node;
        while(temp.getParent() != null)
        {
            System.out.print("| ");
            temp = temp.getParent();
        }
        
        System.out.print(node.getAttribute().getName() + " = 1 :");
        if(node.getRightChild() == null || node.getRightChild().isLeaf())
        {
            System.out.println(" 1");
        }
        else
        {
            System.out.println("");
            this.printTree(node.getRightChild());
        }
    }
}