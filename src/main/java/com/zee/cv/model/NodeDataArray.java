package com.zee.cv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class NodeDataArray {
    public final long key;
    public final String text;
    
    

    @JsonCreator
    public NodeDataArray(@JsonProperty("key") long key, @JsonProperty("text") String text) {
        this.key = key;
        this.text = text;
    }

}