/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.easytv.annotator.webservice.model;

import it.uniroma1.lcl.babelnet.BabelSynset;
import java.util.List;

/**
 *
 * @author Pablo
 */
public class InputSegment {
    
    private String order;
    private String start;
    private String end;
    private String content;


    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

  
    /*
    public void copyTokenInformation(EToken tok) {
    
    content=tok.Word;
    this.Word=tok.Word;
    this.POS=tok.POS;
    this.Lemma=tok.Lemma;
    this.Stemm=tok.Stemm;
    this.Language=tok.Language;
    this.NE=tok.NE;
    this.WordSynsets=tok.WordSynsets;
    this.LemmaSynsets=tok.LemmaSynsets;
    
    
    }*/
}
