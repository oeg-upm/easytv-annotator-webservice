/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.easytv.annotator.webservice.model;

/**
 *
 * @author Pablo
 */
public class InputServiceTranslation {
    
    private String translationLanguage;
    private InputSignLanguageVideo video;

    public InputSignLanguageVideo getVideo() {
        return video;
    }

    public void setVideo(InputSignLanguageVideo video) {
        this.video = video;
    }

    public String getTranslationLanguage() {
        return translationLanguage;
    }

    public void setTranslationLanguage(String translationLanguage) {
        this.translationLanguage = translationLanguage;
    }
    
    
    
    
}
