/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.easytv.annotator.webservice.model;

import java.util.ArrayList;
import java.util.List;
import oeg.easytvannotator.model.input.JsonSignLanguageAnnotationInput;
import oeg.easytvannotator.model.SignLanguageSegment;

/**
 *
 * @author Pablo
 */
public class EResultVideoAnnotation {
    
    public List<EResultSegment> Segments;
    
    public String Nls;
    public String Sls;
    public String Url;
    public String Language;
    
    
    

     public EResultVideoAnnotation(){
        this.Nls="Failed";
     }
     
    public EResultVideoAnnotation(JsonSignLanguageAnnotationInput video){
    
        this.Nls=video.getVideo().getNls();
        this.Sls=video.getVideo().getSls();
        this.Url=video.getVideo().getUrl();
        this.Language= video.getVideo().getLanguage();
        
        
        Segments=new ArrayList();
        
        int counter=0;
        for (SignLanguageSegment seg : video.getVideo().getSegments()) {

           String next;
           next= String.valueOf(counter+1);
           if(counter+1 >= video.getVideo().getSegments().size()){
               next=null;
           }
            
           Segments.add(new EResultSegment(seg,next ,Url,Nls,Sls));
           counter++;
        }

        
    }
    
}
