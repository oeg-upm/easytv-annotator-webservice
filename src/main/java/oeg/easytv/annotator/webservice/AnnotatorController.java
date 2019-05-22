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
import oeg.easytv.annotator.webservice.model.EResultVideo;
import oeg.easytv.annotator.webservice.model.EResultVideoAnnotation;
import oeg.easytv.annotator.webservice.model.InputSegment;
import oeg.easytv.annotator.webservice.model.InputService;
import oeg.easytv.annotator.webservice.model.InputServiceTranslation;
import oeg.easytvannotator.annotation.SignLanguageVideoAnnotator;
import oeg.easytvannotator.babelnet.BabelNetInterface;
import oeg.easytvannotator.model.ESentence;
import oeg.easytvannotator.model.EasyTVInterface;
import oeg.easytvannotator.model.input.JsonSignLanguageAnnotationInput;
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
import org.upm.oeg.easytv.rdfy.Mapper;
import com.babelscape.util.UniversalPOS;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import it.uniroma1.lcl.jlt.util.Language;



@Controller
public class AnnotatorController {

     @Autowired
    ServletContext context;

    EasyTVInterface annotator=null;
    
    SignLanguageVideoAnnotator Annotator=null;
    
    static Logger logger = Logger.getLogger(AnnotatorController.class);
    

    public void initAnnotator(){
    
         this.Annotator = new SignLanguageVideoAnnotator(context.getRealPath("/")+"/WEB-INF/",context.getRealPath("/"),true); 
    
    }
    
    
  
    
    @RequestMapping(
            value = "/annotateVideo",
            consumes = "application/json;charset=UTF-8",
            produces= "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public EResultVideoAnnotation annotateVideo(@RequestBody InputService video) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        try {

            if (Annotator == null) {
                logger.info("Init Annotator");
                this.initAnnotator();
               
            }

            JsonSignLanguageAnnotationInput videoJson = mapInput(video);
            Annotator.annotateSignLanguageVideo(videoJson);
            EResultVideoAnnotation res = new EResultVideoAnnotation(videoJson);
            
            
            Gson gson = new Gson();
            String json = gson.toJson(res);

            System.out.println(json);

            
            Mapper.createFile(context.getRealPath("/")+"/WEB-INF/source/json/test.json", json);
            Mapper map=new Mapper(context.getRealPath("/")+"/WEB-INF/");
            map.generateMappings();
            
            
            return res;
            
        } catch (Exception e) {
            logger.error("Error in REST service",e);
            logger.error(e.getCause().toString());
            e.printStackTrace();
            
            
            return new EResultVideoAnnotation();
        }

       
    }
    
    
    @RequestMapping(
            value = "/uploadGraph",
            consumes = "application/json;charset=UTF-8",
            produces= "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public String uploadGraph(@RequestBody String json) throws Exception {

        try{
            System.out.println(json);

            
            Mapper.createFile(context.getRealPath("/")+"/WEB-INF/source/json/test.json", json);
            Mapper map=new Mapper(context.getRealPath("/")+"/WEB-INF/");
            map.generateMappings();
            
            
            return "Done";
            
        } catch (Exception e) {
            logger.error("Error in REST service",e);
            logger.error(e.getCause().toString());
            e.printStackTrace();
            
            
            return e.getCause().toString();
        }

       
    }
    
    
    
    
    
    @RequestMapping("/status")
    @ResponseBody
    public String status() {
        return "UP";
    }
    
    
    
    @RequestMapping("/testGreek")
    @ResponseBody
    public String testGreek() {
        
       if (annotator == null) {
                logger.info("Init Annotator");
                annotator = new EasyTVInterface(context.getRealPath("/")+"/WEB-INF/",context.getRealPath("/"),true); 
               
          }
       ESentence ese= annotator.procesSentence("EL","το σπίτι είναι κόκκινο");
       
        return ese.print();
    }
    
    
    @RequestMapping("/testBabelNetConnection")
    @ResponseBody
    public String testSpanish() {
        
        BabelNetInterface bn= new BabelNetInterface(context.getRealPath("/"),true); 
        bn.initInstance();
        return bn.callBabelNetWordPOS("casa", Language.ES, UniversalPOS.NOUN).get(0).ID;

    }
    
    @RequestMapping("/restartAnnotator")
    @ResponseBody
    public String restartAnnotator() {
        
        logger.info("Restart Annotator");
        initAnnotator();      
        return "Done";
    }
    
    
   
    
    
    
    public JsonSignLanguageAnnotationInput mapInput(InputService video){
    
        JsonSignLanguageAnnotationInput input= new JsonSignLanguageAnnotationInput();
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
            slsegment.setWord(seg.getContent());
            slsegment.setLanguage(video.getVideo().getLanguage());
            input.getVideo().getSegments().add(slsegment);
        
        }
        
        return input;
    
    }
    
    
    /*
    public JsonSignLanguageAnnotationInput mapInput2(InputServiceTranslation video){
    
        JsonSignLanguageAnnotationInput input= new JsonSignLanguageAnnotationInput();
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
    */
    

  
}
