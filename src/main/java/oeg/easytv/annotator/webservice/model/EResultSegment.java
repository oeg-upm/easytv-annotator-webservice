/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.easytv.annotator.webservice.model;

import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.kb.ExternalResource;
import java.util.ArrayList;
import java.util.List;
import oeg.easytvannotator.babelnet.BabelLangInterface;
import oeg.easytvannotator.model.EToken;
import oeg.easytvannotator.model.SignLanguageSegment;

/**
 *
 * @author Pablo
 */
public class EResultSegment {
    public String Word;
    public String POS;
    public String Lemma;
    //public String Stemm;
    public String Language;
    
    public String Order;
    public String Start;
    public String End;
    
    public List<ESynset> Synsets;
    
    
    public EResultSegment(SignLanguageSegment seg){
    
        this.Word=seg.Word;
        this.POS=seg.POS;
        this.Lemma=seg.Lemma;
        this.Language=seg.Language;
        Synsets=new ArrayList();
        
        this.Order=seg.getOrder();
        this.Start=seg.getStart();
        this.End=seg.getEnd();
        
        int counter=0;
        
        
        for (BabelSynset syn : seg.LemmaSynsets) {
            BabelSense sense = syn.getMainSense(BabelLangInterface.getLangType(Language)).get();

            if (counter==3){break;}
            //  "http://babelnet.org/rdf/maestro_ES/s00046958n"
            String id = syn.getID().toString().replace("bn:", "");

            String url = "http://babelnet.org/rdf/s" + id;
            String sens = "http://babelnet.org/rdf/" + parseLemma(sense.getFullLemma()) + "_" + sense.getLanguage().toString() + "/s" + id;

            ESynset syns = new ESynset(url, sens);
            Synsets.add(syns);
            counter++;
        }


    }
    
     public String parseLemma(String lemma){

        return lemma.replaceAll("Ã¡", "á").replaceAll("Ã©", "é").replaceAll("Ã­", "í").replaceAll("Ã³", "ó").replaceAll("Ãº", "ú");

    }
    
}
