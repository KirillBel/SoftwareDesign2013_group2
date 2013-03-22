/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import java.util.ArrayList;

/**
 *
 * @author Kirill
 */

class CollectionElem
{
    public CollectionElem(String n, PropertyList l)
    {
        name=new String(n);
        list=l;
    };
    
    public String name=null;
    public PropertyList list=null;
};


public class PropertyCollection {
    ArrayList<CollectionElem> collection=new ArrayList<CollectionElem>();
    
    public void add(String name, PropertyList list)
    {
        int t=find(name);
        if(t!=-1) return;
        
        collection.add(new CollectionElem(name,list));
    };
    
    int find(PropertyList list)
    {
        for(int i=0;i<collection.size();i++)
        {
            if(collection.get(i).list==list) return i;
        };
        return -1;
    };
    
    int find(String str)
    {
        for(int i=0;i<collection.size();i++)
        {
            if(collection.get(i).name==str) return i;
        };
        return -1;
    };
    
    PropertyList get(String name)
    {
        int t=find(name);
        if(t==-1) return null;
        
        return collection.get(t).list;
    };
    
    public void clear()
    {
        collection.clear();
    };
}
