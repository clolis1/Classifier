package classifier.util;

import java.util.*;

public class Example
{
    Vector<Attribute> attributes;
    
    public Example()
    {
        attributes = new Vector<Attribute>();
    }
    
    public void addAttribute(Attribute attribute)
    {
        attributes.addElement(attribute);
    }
    
    public Vector<Attribute> getAttributes()
    {
        return attributes;
    }
    
    public int returnValue(Attribute target)
    {
        for (int i = 0; i < attributes.size() - 1; i++)
        {
            if (target.getName() == null) {System.out.println("ouch");}
            if (attributes.get(i).getName().equals(target.getName()))
            {
                return attributes.get(i).getValue();
            }
        }
//        System.out.println("ERROR: Target attribute not found.");
        Random rand = new Random();
        return rand.nextInt(2);
    }
    
    public int getClassValue()
    {
        return attributes.get(attributes.size() - 1).getValue();
    }
}