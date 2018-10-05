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
import oeg.easytvannotator.model.JsonInput;
import oeg.easytvannotator.model.SignLanguageSegment;

/**
 *
 * @author Pablo
 */
public class EResultVideo {
    
    public List<EResultSegment> segments;
    
    public String OriginalText;
    //public String LematizedText;
    
     public EResultVideo(){
        this.OriginalText="Failed";
     }
    public EResultVideo(JsonInput video){
    
       // this.LematizedText=video.getVideo().LematizedText;
        this.OriginalText=video.getVideo().getNls();//.OriginalText;
        
        segments=new ArrayList();
        
        for (SignLanguageSegment seg : video.getVideo().getSegments()) {

           segments.add(new EResultSegment(seg));
           
        }

        
    }
    
}
