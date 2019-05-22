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
public class EResultVideoTranslation {
    
    public List<EResultSegmentTranslation> segments;
    
    public String nls;
    public String sls;
    public String url;
    
     public EResultVideoTranslation(){
        this.nls="Failed";
     }
    public EResultVideoTranslation(JsonSignLanguageAnnotationInput video){
    
        this.nls=video.getVideo().getNls();
        this.sls=video.getVideo().getSls();
        this.url=video.getVideo().getUrl();
        
        
        segments=new ArrayList();
        
        for (SignLanguageSegment seg : video.getVideo().getSegments()) {

           segments.add(new EResultSegmentTranslation(seg,video.getVideo()));
           
        }

        
    }
    
}
