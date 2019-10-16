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
import oeg.easytv.annotator.webservice.comm.output.EResultVideoAnnotation;
import oeg.easytv.annotator.webservice.comm.input.InputAnnotateVideo;
import oeg.easytvannotator.annotation.SignLanguageVideoAnnotator;
import oeg.easytvannotator.babelnet.BabelNetInterface;
import oeg.easytvannotator.model.ESentence;
import oeg.easytvannotator.model.EasyTVInterface;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.upm.oeg.easytv.rdfy.Mapper;
import com.babelscape.util.UniversalPOS;
import com.google.gson.Gson;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import oeg.easytv.annotator.webservice.comm.input.InputAnnotateTranslatedVideos;
import oeg.easytv.annotator.webservice.comm.input.InputUserTranslation;
import org.springframework.web.bind.annotation.RequestParam;
import org.upm.oeg.easytv.rdfy.Querier;
import org.upm.oeg.easytv.rdfy.VideoCollection;
import org.upm.oeg.easytv.rdfy.VideoTranslation;

@Controller
public class AnnotatorController {

    private String BabelNetDir;
    private String NLPDir;
    private String RdfyDir;

    private SignLanguageVideoAnnotator Annotator = null;

    private static Logger logger = Logger.getLogger(AnnotatorController.class);

    
    @PostConstruct
    public void initIt() {
	  System.out.println("Init method after properties are set : " );
    }
    
    public void initAnnotator() throws Exception {

        this.Annotator = new SignLanguageVideoAnnotator(NLPDir, BabelNetDir, true);
    }

    public void initProperties() throws Exception {

        try {

            InputStream input = this.getClass().getClassLoader().getResourceAsStream("configuration.properties");

            Properties properties = new Properties();
            properties.load(input);

            BabelNetDir = properties.getProperty("babelnetSources");
            NLPDir = properties.getProperty("nlpSources");
            RdfyDir = properties.getProperty("rdfySources");

        } catch (Exception e) {
            logger.error("Error finding properties file. Execution aborted.");
            throw new Exception("Unable to identify properties of the annotator");

        }

    }

    /*
    
    S1 Annotate video
    Presented in T3.2
    Main service used to populate the ontology

    S2 Annotate 2 translated videos
    The same process as S1 + generate a link between the videos


        S3 Get translation (2 ways)
    1. The video is already annotated in the system
    JSON input pointing to a video in the system
    2. The video is NOT annotated in the system
    Part of S1 (annotation) is executed
    JSON input from T3.1

    S4 Verify 2 translated videos
    2 already annotated videos are verified


    
     */
    public void checkAnnotatorStatus() throws Exception {

        if (BabelNetDir == null) {
            logger.info("Init Properties");
            initProperties();
        }

        if (Annotator == null) {
            logger.info("Init Annotator");
            this.initAnnotator();

        }

    }

    public String jsonCreator(Object o) {

        Gson gson = new Gson();
        return gson.toJson(o);

    }

    @RequestMapping(
            value = "/annotateVideo",
            consumes = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public String annotateVideo(@RequestBody InputAnnotateVideo InputJson) {

        try {

            // properties and annotator
            checkAnnotatorStatus();

            //LOG : recived
            createLogFile(InputJson.getVideo().getNls() + ".json", this.RdfyDir + "logs/jsonvideo", jsonCreator(InputJson));

            // Annotate
            Annotator.annotateSignLanguageVideo(InputJson.getVideo());
            EResultVideoAnnotation res = new EResultVideoAnnotation(InputJson.getVideo());

            /// MAP TO ONTOLOGY
            Mapper map = new Mapper(this.RdfyDir);
            map.createJsonFile(InputJson.getVideo().getNls(), jsonCreator(res));
            map.generateMappings(InputJson.getVideo().getNls());

            logger.info("Created:" + "http://easytv.linkeddata.es/page/resource/Video/" + res.UrlID.toString());
            return "http://easytv.linkeddata.es/page/resource/Video/" + res.UrlID.toString();

        } catch (Exception e) {
            logger.error("Error:", e);
            return "Error:" + e;
        }

    }

    @RequestMapping(
            value = "/annotateTranslatedVideos",
            consumes = "application/json;charset=UTF-8",
            //produces= "application/json;charset=UTF-8",
            //produces= "text/plain;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public String annotateTranslatedVideos(@RequestBody InputAnnotateTranslatedVideos InputJson) {

        try {

            if (Annotator == null) {
                logger.info("Init Annotator");
                this.initAnnotator();
            }

            //Gson gson = new Gson(); 
            //LOG : recived
            createLogFile(InputJson.getVideo1().getNls() + ".json", this.RdfyDir + "logs/jsonvideo", jsonCreator(InputJson));

            // video1
            Annotator.annotateSignLanguageVideo(InputJson.getVideo1());
            EResultVideoAnnotation res1 = new EResultVideoAnnotation(InputJson.getVideo1());

            // video2
            Annotator.annotateSignLanguageVideo(InputJson.getVideo2());
            EResultVideoAnnotation res2 = new EResultVideoAnnotation(InputJson.getVideo2());

            /// MAP TO ONTOLOGY
            Mapper map = new Mapper(this.RdfyDir);
            map.createJsonFile(InputJson.getVideo1().getNls(), jsonCreator(res1));
            map.generateMappings(InputJson.getVideo1().getNls());

            map.createJsonFile(InputJson.getVideo2().getNls(), jsonCreator(res2));
            map.generateMappings(InputJson.getVideo2().getNls());

            // CREATE MAPPING QUERY
            map.createTranslation(res1.UrlID, res2.UrlID, res1.Nls, res2.Nls, res1.Sls, res2.Sls);
           
            return "\"url1\"=\"http://easytv.linkeddata.es/page/resource/Video/" + res1.UrlID.toString()
                    + "\",\"url2\"=\"http://easytv.linkeddata.es/page/resource/Video/" + res2.UrlID.toString() + "\"";

        } catch (Exception e) {
            logger.error("Error in REST service", e);

            return "Failed";
        }

    }

    @RequestMapping(
            value = "/getTranslation",
            method = RequestMethod.GET)
    @ResponseBody
    public VideoTranslation getTranslation(@RequestParam String videoURI, @RequestParam String targetLang) {
        //@RequestMapping(value = "/personId")              
        //String getId(@RequestParam String personId
        //localhost:8090/home/personId?personId=5

        try {

            this.initProperties();
            String s = videoURI + "  " + targetLang + "";
            logger.info(s);

            Querier querier = new Querier(this.RdfyDir); //.getRealPath("/")

            VideoTranslation v = querier.sendQueryGetVideoTranslation(videoURI, targetLang);
            return v;

        } catch (Exception e) {

            logger.error("Failed:" + e);

            return new VideoTranslation("", "", "", "");
        }

    }

    
    
    @RequestMapping(
            value = "/getAllTranslations",
            method = RequestMethod.GET)
    @ResponseBody
    public List<VideoTranslation> getAllTranslations(@RequestParam String videoURI) {
        //@RequestMapping(value = "/personId")              
        //String getId(@RequestParam String personId
        //localhost:8090/home/personId?personId=5

        try {
            this.initProperties();
            String s = videoURI + "  " + " all";
            logger.info(s);

            Querier querier = new Querier(this.RdfyDir); //.getRealPath("/")

            List<VideoTranslation> list = querier.sendQueryGetAllVideoTranslations(videoURI);
            return list;

        } catch (Exception e) {

            logger.error("Failed:" + e);

            return new ArrayList();
        }

    }

    @RequestMapping(
            value = "/verifyTranslations",
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public String verifyTranslations(@RequestBody InputUserTranslation translation) throws Exception {
        this.initProperties();

        try {

            /// MAP TO ONTOLOGY
            Mapper map = new Mapper(this.RdfyDir);

            // CREATE MAPPING QUERY
            map.createTranslationByUser(translation.video1.url, translation.video2.url, translation.video1.nls, translation.video2.nls, translation.video1.sls, translation.video2.sls, translation.confidence);
            //createTranslation(String UrlID1, String UrlID2, String Nls1, String Nls2, String Sls1, String Sls2);

            return "Done";

        } catch (Exception e) {
            logger.error("Error in REST service", e);

            return "Failed";
        }

    }

    /**
     * OTHERS    *
     */
    @RequestMapping(
            value = "/getAllVideos",
            method = RequestMethod.GET)
    @ResponseBody
    public VideoCollection getAllFromSparql() {

        try {
            this.initProperties();
            logger.info("Get All service");
            Querier querier = new Querier(this.RdfyDir); //.getRealPath("/")
            VideoCollection v = querier.sendQueryAll();
            logger.info("query processed");
            return v;
        } catch (Exception e) {

            logger.error("Failed:" + e);

            return new VideoCollection();
        }

    }

    @RequestMapping(
            value = "/getAllVideosOfLang",
            method = RequestMethod.GET)
    @ResponseBody
    public VideoCollection getAllofLangFromSparql(@RequestParam String lang) {
        //@RequestMapping(value = "/personId")              
        //String getId(@RequestParam String personId
        //localhost:8090/home/personId?personId=5
        try {
            this.initProperties();
            logger.info("Get All service of a language");
            Querier querier = new Querier(this.RdfyDir); //.getRealPath("/")
            VideoCollection v = querier.sendQueryAllOfLang(lang);
            logger.info("query processed");
            return v;
        } catch (Exception e) {

            logger.error("Failed:" + e);

            return new VideoCollection();
        }

    }

    @RequestMapping(
            value = "/uploadGraph",
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public String uploadGraph(@RequestBody String json) throws Exception {

        initProperties();
        try {
            logger.info(json);

            Mapper map = new Mapper(RdfyDir);
            map.createJsonFile("manual", json);
            map.generateMappings("manualgraph");

            return "Done";

        } catch (Exception e) {
            logger.error("Error in REST service", e);

            return e.getCause().toString();
        }

    }

    @RequestMapping(
            value = "/parseVideo",
            consumes = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public EResultVideoAnnotation parsevideo(@RequestBody InputAnnotateVideo InputJson) {

        try {

            if (Annotator == null) {
                logger.info("Init Annotator");
                this.initAnnotator();
            }

            Gson gson = new Gson();

            //LOG : recived
            createLogFile(InputJson.getVideo().getNls() + ".json", this.RdfyDir + "logs/jsonvideo", gson.toJson(InputJson));

            Annotator.annotateSignLanguageVideo(InputJson.getVideo());
            EResultVideoAnnotation res = new EResultVideoAnnotation(InputJson.getVideo());

            return res;

        } catch (Exception e) {
            logger.error("Error in REST service", e);

            return new EResultVideoAnnotation();
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

        try {
            initProperties();
            EasyTVInterface annotator = null;
            if (annotator == null) {
                logger.info("Init Annotator");
                annotator = new EasyTVInterface(this.NLPDir, this.BabelNetDir, true);

            }
            ESentence ese = annotator.procesSentence("EL", "το σπίτι είναι κόκκινο");

            return ese.print();
        } catch (Exception e) {

            logger.error("Test Failed:" + e);
            return "Test Restart";
        }
    }

    @RequestMapping("/testBabelNetConnection")
    @ResponseBody
    public String testSpanish() {
        try {
            initProperties();
            BabelNetInterface bn = new BabelNetInterface(this.BabelNetDir, true);
            bn.initInstance();
            return bn.callBabelNetWordPOS("casa", Language.ES, UniversalPOS.NOUN).get(0).ID;
        } catch (Exception e) {

            logger.error("Test Failed:" + e);
            return "Test Restart";
        }

    }

    @RequestMapping("/restartAnnotator")
    @ResponseBody
    public String restartAnnotator() {

        logger.info("Restart Annotator");

        try {
            initProperties();
            initAnnotator();
            return "Done";

        } catch (Exception e) {

            logger.error("Restart Failed:" + e);
            return "Failed Restart";
        }

    }

    public static File createLogFile(String FileName, String Dir, String Text) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = timestamp.toString().replaceAll(":", "\\.");

        File file = null;
        try {

            file = new File(Dir + File.separator + time + "-" + FileName);

            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF8"));

            out.append(Text);

            out.flush();
            out.close();

        } catch (IOException e) {
            logger.error(e);

        }

        return file;

    }

}
