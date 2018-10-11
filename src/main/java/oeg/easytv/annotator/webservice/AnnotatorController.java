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

import java.util.ArrayList;
import javax.servlet.ServletContext;
import oeg.easytv.annotator.webservice.model.EResultSentence;
import oeg.easytv.annotator.webservice.model.EResultVideo;
import oeg.easytv.annotator.webservice.model.Greeting;
import oeg.easytv.annotator.webservice.model.InputSegment;
import oeg.easytv.annotator.webservice.model.InputService;
import oeg.easytvannotator.babelnet.BabelNetInterface;
import oeg.easytvannotator.model.ESentence;
import oeg.easytvannotator.model.EasyTVInterface;
import oeg.easytvannotator.model.JsonInput;
import oeg.easytvannotator.model.SignLanguageSegment;
import oeg.easytvannotator.model.SignLanguageVideo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AnnotatorController {

     @Autowired
    ServletContext context;

    EasyTVInterface annotator;
    
    static Logger logger = Logger.getLogger(AnnotatorController.class);
    

/*
    @RequestMapping(
            value = "/annotatesentence",
            method = RequestMethod.POST)
    @ResponseBody
    public EResultSentence run(@RequestBody InputService video) throws Exception {

        try {

            if (annotator == null) {
                annotator = new EasyTVInterface(context.getRealPath("/")+"/WEB-INF/"); //""
                BabelNetInterface.serviceweb=true;
                BabelNetInterface.ContextPath=context.getRealPath("/");
            }

            ESentence Es = annotator.processJson(mapInput(video));
            EResultSentence res = new EResultSentence(Es);
            return res;
        } catch (Exception e) {

            System.out.println("errror");
            e.printStackTrace();

        }

        return new EResultSentence();

    }
  */  
    
    @RequestMapping(
            value = "/annotate",
            method = RequestMethod.POST)
    @ResponseBody
    public EResultVideo annotateSentence(@RequestBody InputService video) throws Exception {

        try {

            if (annotator == null) {
                annotator = new EasyTVInterface(context.getRealPath("/")+"/WEB-INF/"); //context.getRealPath("/")+"/WEB-INF/"
                BabelNetInterface.serviceweb=true;
                BabelNetInterface.ContextPath=context.getRealPath("/");
            }

            JsonInput videoJson = mapInput(video);
            ESentence ese= annotator.processJson(videoJson);
            ese.associateVideos(videoJson);
            
            EResultVideo res = new EResultVideo(videoJson);
            
            return res;
        } catch (Exception e) {
            logger.error(e);
        }

        return new EResultVideo();
    }
    
    
    @RequestMapping("/greeting")
    @ResponseBody
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(1, "hello " + name);
    }
    
    
    @RequestMapping("/status")
    @ResponseBody
    public String status() {
        return "UP";
    }
    
    
    
    public JsonInput mapInput(InputService video){
    
        JsonInput input= new JsonInput();
        input.setVideo(new SignLanguageVideo());
        
        input.getVideo().setDuration(video.getVideo().getDuration());
        input.getVideo().setLanguage(video.getVideo().getLanguage());
        input.getVideo().setNls(video.getVideo().getNls());
        input.getVideo().setSls(video.getVideo().getSls());
        input.getVideo().setUrl(video.getVideo().getUrl());
        
        input.getVideo().setSegments(new ArrayList());
        
        for(InputSegment seg : video.getVideo().getSegments()){
        
            SignLanguageSegment slsegment=new SignLanguageSegment();
            slsegment.setContent(seg.getContent());
            slsegment.setStart(seg.getStart());
            slsegment.setEnd(seg.getEnd());
            slsegment.setOrder(seg.getOrder());
            
            input.getVideo().getSegments().add(slsegment);
        
        }
        return input;
    
    }
    
    
  
}
