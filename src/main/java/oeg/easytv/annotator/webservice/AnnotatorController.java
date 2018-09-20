/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.easytv.annotator.webservice;

/**
 *
 * @author Pablo
 */

import oeg.easytv.annotator.webservice.model.EResultSentence;
import oeg.easytvannotator.model.ESentence;
import oeg.easytvannotator.model.EasyTVInterface;
import oeg.easytvannotator.model.InputService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AnnotatorController {

    
    EasyTVInterface annotator;
    
    @RequestMapping(
    value = "/annotate", 
    method = RequestMethod.POST)
    @ResponseBody
    public EResultSentence run(@RequestBody InputService video) 
    throws Exception {

        if(annotator==null){annotator=new EasyTVInterface("");}
        
        ESentence Es=annotator.processJson( video);
        EResultSentence res=new EResultSentence(Es);
   
        return res;
}
    
    
  
}
