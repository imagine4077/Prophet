package com.hackathon.prophet.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SingleDtsBase
{
    private String id;
    private String severity;
    private String reappearable;
    private String simpleDescription;
    private String detailDescription;
    private String similarDts;

    public SingleDtsBase(String id, String severity
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

    public SingleDtsBase(String id, String severity
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

    /**
     * 将简化描述和具体描述合并，视为一个文本处理。
     * */
    public SingleDtsBase(String id, String severity
            , String reappearable, String Description)
    {
        this.id = id;
        this.severity = severity;
        this.reappearable = reappearable;
        this.simpleDescription = null;
        this.detailDescription = detailDescription;
        this.similarDts = null;
    }

    public SingleDtsBase(String id)
    {
        this.id = id;
        this.severity = null;
        this.reappearable = null;
        this.simpleDescription = null;
        this.detailDescription = null;
        this.similarDts = null;
    }
}
