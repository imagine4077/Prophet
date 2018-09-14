package com.hackathon.prophet.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DtsBase
{
    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "severity")
    private String severity;

    @JsonProperty(value = "reappearable")
    private String reappearable;

    @JsonProperty(value = "simpleDescription")
    private String simpleDescription;

    @JsonProperty(value = "detailDescription")
    private String detailDescription;

    @JsonIgnore
    private String similarDts;

    /*public DtsBase(String id, String severity
            , String reappearable, String simpleDescription
            , String detailDescription, String similarDts)
    {
        this.id = id;
        this.severity = severity;
        this.reappearable = reappearable;
        this.simpleDescription = simpleDescription;
        this.detailDescription = detailDescription;
        this.similarDts = similarDts;
    }

    public DtsBase(String id, String severity
            , String reappearable, String simpleDescription
            , String detailDescription)
    {
        this.id = id;
        this.severity = severity;
        this.reappearable = reappearable;
        this.simpleDescription = simpleDescription;
        this.detailDescription = detailDescription;
        this.similarDts = null;
    }

    *//**
     * 将简化描述和具体描述合并，视为一个文本处理。
     * *//*
    public DtsBase(String id, String severity
            , String reappearable, String Description)
    {
        this.id = id;
        this.severity = severity;
        this.reappearable = reappearable;
        this.simpleDescription = null;
        this.detailDescription = detailDescription;
        this.similarDts = null;
    }

    public DtsBase(String id)
    {
        this.id = id;
        this.severity = null;
        this.reappearable = null;
        this.simpleDescription = null;
        this.detailDescription = null;
        this.similarDts = null;
    }*/
}
