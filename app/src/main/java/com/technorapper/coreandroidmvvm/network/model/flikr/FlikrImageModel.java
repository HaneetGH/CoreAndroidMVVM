
package com.technorapper.coreandroidmvvm.network.model.flikr;

import java.util.HashMap;
import java.util.Map;

public class FlikrImageModel {

    private Photos photos;
    private String stat;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
