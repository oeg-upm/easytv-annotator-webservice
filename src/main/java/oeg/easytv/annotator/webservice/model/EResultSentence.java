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

/**
 *
 * @author Pablo
 */
public class EResultSentence {
    
    public List<EResultToken> Tokens;
    
    public String OriginalText;
    public String LematizedText;
    
     public EResultSentence(){
        this.OriginalText="Failed";
     }
    public EResultSentence(ESentence sentence){
    
        this.LematizedText=sentence.LematizedText;
        this.OriginalText=sentence.OriginalText;
        
        Tokens=new ArrayList();
        
        for (EToken token : sentence.ListTokens) {

           Tokens.add(new EResultToken(token));
           
        }

        
    }
}
