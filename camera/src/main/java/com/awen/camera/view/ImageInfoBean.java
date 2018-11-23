package com.awen.camera.view;

import java.io.Serializable;

public class ImageInfoBean implements Serializable {
    /**
     *
     */


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private String uri;

}