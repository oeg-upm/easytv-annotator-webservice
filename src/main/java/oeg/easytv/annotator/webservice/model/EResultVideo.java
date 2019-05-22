/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.easytv.annotator.webservice.model;

import java.util.ArrayList;
import java.util.List;
import oeg.easytvannotator.model.ESentence;
import oeg.easytvannotator.model.EToken;
import oeg.easytvannotator.model.input.JsonSignLanguageAnnotationInput;
import oeg.easytvannotator.model.SignLanguageSegment;

/**
 *
 * @author Pablo
 */
public class EResultVideo {
    
    public List<EResultSegment> segments;
    
    public String Nls;
    public String Sls;
    public String Url;
    
     public EResultVideo(){
        this.Nls="Failed";
     }
    public EResultVideo(JsonSignLanguageAnnotationInput video){
    
        this.Nls=video.getVideo().getNls();
        this.Sls=video.getVideo().getSls();
        this.Url=video.getVideo().getUrl();
        
        
        segments=new ArrayList();
        
        for (SignLanguageSegment seg : video.getVideo().getSegments()) {

           segments.add(new EResultSegment(seg,"1" ,Url,Nls,Sls));
           
        }

        
    }
    
}
