package com.zee.cv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CallGraph {
    public final String key;
    public final String name;
    public final String parent;

    @JsonCreator
    public CallGraph(@JsonProperty("key") String key, @JsonProperty("name") String name,
            @JsonProperty("parent") String parent) {
        this.key = key;
        this.name = name;
        this.parent = parent;
    }
}
