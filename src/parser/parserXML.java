/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;

/**
 *
 * @author FlyPig
 */
public class parserXML {
    
    private ArrayList<String> textToPars=new ArrayList<String>();

    public parserXML(ArrayList<String> text_) 
    {
        textToPars=text_;
    }
    
    public void setText(ArrayList<String> text_)
    {
        textToPars=text_;
    }
    
    public ArrayList<String> openedFilesPars(ArrayList<String> text_)
    {    
        textToPars=text_;
        ArrayList<String> res=new ArrayList<String>();
        for(int i=0;i<textToPars.size();i++)
        {
            switch (textToPars.get(i))
            {
                case "<?xml version=\"1.0\" encoding=\"UTF-8\"?>":
                    continue;
                case "<openedFiles>":
                    continue;
                case "</openedFiles>":
                    continue;
                case "</file>":
                    continue;
                default:    
                    textToPars.set(i,textToPars.get(i).replaceAll("<", ""));
                    textToPars.set(i,textToPars.get(i).replaceAll(">", ""));
                    textToPars.set(i,textToPars.get(i).replaceAll("\"", " "));
                    String[] split=textToPars.get(i).split(" ");
                    if(split[0].equals("file") && split[1].equals("id="))
                    {
                        textToPars.set(i+1,textToPars.get(i+1).replaceAll("<path>", ""));
                        textToPars.set(i+1,textToPars.get(i+1).replaceAll("</path>", ""));
                        if(res.size()==Integer.valueOf(split[2]))
                        {
                            res.add(Integer.valueOf(split[2]), textToPars.get(i+1));
                            i++;
                        }                        
                    }
            }
        }
        
        return res;                         
         
    }
    
    public String openedFilesToWrite(ArrayList<String> text_)                       
    {
        textToPars=text_;
        String res="";
        res=res+"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        res=res+"<openedFiles>\n";
        for(int i=0;i<textToPars.size();i++)
        {
            res=res+"<file id=\""+String.valueOf(i)+"\">\n";
            res=res+"<path>"+textToPars.get(i)+"</path>\n";
            res=res+"<file/>\n";            
        }
        res=res+"</openedFiles>\n";
        return res;        
    }
    
}
