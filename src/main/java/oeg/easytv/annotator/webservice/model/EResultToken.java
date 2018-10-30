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
import oeg.easytvannotator.babelnet.BabelNetSynset;
import oeg.easytvannotator.model.EToken;

/**
 *
 * @author Pablo
 */
public class EResultToken {
    public String Word;
    public String POS;
    public String Lemma;
    //public String Stemm;
    public String Language;
    
    public String Order;
    public String Start;
    public String End;
    
    public List<ESynset> Synsets;
    
    
    public EResultToken(EToken token){
    
        this.Word=token.Word;
        this.POS=token.POS;
        this.Lemma=token.Lemma;
        this.Language=token.Language;
        Synsets=new ArrayList();
        
        this.Order=token.Order;
        this.Start=token.Start;
        this.End=token.End;
        
        int counter=0;
        for (BabelNetSynset syn : token.LemmaSynsets) {
            BabelSense sense = syn.MainSense;

            if (counter==3){break;}
            //  "http://babelnet.org/rdf/maestro_ES/s00046958n"
            String id = syn.ID.replace("bn:", "");

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
