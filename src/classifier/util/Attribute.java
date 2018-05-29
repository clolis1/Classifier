package classifier.util;

public class Attribute
{
    String name;
    int value;
    
    public Attribute(String nameIn)
    {
        name = nameIn;
    }
    
    public Attribute(String nameIn, int valueIn)
    {
        name = nameIn;
        value = valueIn;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getValue()
    {
        return value;
    }
    
    @Override
    public Attribute clone()
    {
        Attribute clone = new Attribute(this.name, this.value);
        
        return clone;
    }
}