package com.zee.cv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ReplyPackageAssociation {
    public final NodeDataArray nodedataarray[];
    public final LinkDataArray linkdataarray[];
    
    @JsonCreator
    public ReplyPackageAssociation(@JsonProperty("nodedata") NodeDataArray[] nodedataarray, @JsonProperty("linkdata") LinkDataArray[] linkdataarray) {
        this.nodedataarray = nodedataarray;
        this.linkdataarray = linkdataarray;
    }
}
