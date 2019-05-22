/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.easytv.annotator.webservice.model;

import it.uniroma1.lcl.babelnet.BabelSense;
import java.util.ArrayList;
import java.util.List;
import oeg.easytvannotator.babelnet.BabelNetSynset;
import oeg.easytvannotator.model.SignLanguageSegment;
import oeg.easytvannotator.model.SignLanguageVideo;

/**
 *
 * @author Pablo
 */
public class EResultSegmentTranslation {
    
    public String word;
    public String pos;
    public String lemma;
    //public String Stemm;
    public String language;
    public String order;
    public String start;
    public String end;
    
    public List<ESynset> synsets;    
    public String url;
    public String nls;
    public String sls;

    public EResultSegmentTranslation(SignLanguageSegment seg, SignLanguageVideo video){
    
        this.word=seg.Word;
        this.pos=seg.POS;
        this.lemma=seg.Lemma;
        this.language=seg.Language;
        synsets=new ArrayList();
        
        this.order=seg.getOrder();
        this.start=seg.getStart();
        this.end=seg.getEnd();
        
        int counter=0;
        
        this.url= video.getUrl();
        this.nls=video.getNls();
        this.sls=video.getSls();
        
        for (BabelNetSynset syn : seg.LemmaSynsets) {
            BabelSense sense = syn.MainSense;

            if (counter==3){break;}
            //  "http://babelnet.org/rdf/maestro_ES/s00046958n"
            String id = syn.ID.replace("bn:", "");

            String url = "http://babelnet.org/rdf/s" + id;
            String sens = "http://babelnet.org/rdf/" + parseLemma(sense.getFullLemma()) + "_" + sense.getLanguage().toString() + "/s" + id;

            ESynset syns = new ESynset(url, sens);
            synsets.add(syns);
            counter++;
        }


    }

    
     public String parseLemma(String lemma){

        return lemma.replaceAll("Ã¡", "á").replaceAll("Ã©", "é").replaceAll("Ã­", "í").replaceAll("Ã³", "ó").replaceAll("Ãº", "ú");

    }
    
}
