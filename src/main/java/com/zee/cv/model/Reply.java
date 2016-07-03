package com.zee.cv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Reply {
    public final NodeData nodedata[];
    public final LinkData linkdata[];
    
    @JsonCreator
    public Reply(@JsonProperty("nodedata") NodeData[] nodedata, @JsonProperty("linkdata") LinkData[] linkdata) {
        this.nodedata = nodedata;
        this.linkdata = linkdata;
    }
}
