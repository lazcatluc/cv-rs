
package com.zee.cv.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphCallReply {
    private GraphCallResponse[] response;

    private Boolean error;

    @JsonProperty("response_msg")
    private String responseMsg;

    public GraphCallResponse[] getResponse() {
        return response;
    }

    public void setResponse(GraphCallResponse[] response) {
        this.response = response;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}