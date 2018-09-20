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
import oeg.easytvannotator.model.ESentence;
import oeg.easytvannotator.model.EToken;

/**
 *
 * @author Pablo
 */
public class EResultSentence {
    
    public List<EResultToken> Tokens;
    
    public String OriginalText;
    public String LematizedText;
    
    
    public EResultSentence(ESentence sentence){
    
        this.LematizedText=sentence.LematizedText;
        this.OriginalText=sentence.OriginalText;
        
        Tokens=new ArrayList();
        
        for (EToken token : sentence.ListTokens) {

           Tokens.add(new EResultToken(token));
           
        }

        
    }
}
