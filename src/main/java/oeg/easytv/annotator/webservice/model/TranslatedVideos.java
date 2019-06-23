/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.easytv.annotator.webservice.model;

import java.util.List;
import oeg.easytvannotator.model.SignLanguageVideo;

/**
 *
 * @author Pablo
 */
public class TranslatedVideos {
    
    private List <SignLanguageVideo>  videos;

    public List<SignLanguageVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<SignLanguageVideo> videos) {
        this.videos = videos;
    }

    
    
    
}
