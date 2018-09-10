package com.hackathon.prophet.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SingleDtsBase
{
    private String id;
    private String severity;
    private boolean reappearable;
    private String simpleDescription;
    private String detailDescription;
    private List<SingleDtsBase> similarDts;

    public SingleDtsBase(String id, String severity
            , boolean reappearable, String simpleDescription
            , String detailDescription, boolean createSimilarDts)
    {
        this.id = id;
        this.severity = severity;
        this.reappearable = reappearable;
        this.simpleDescription = simpleDescription;
        this.detailDescription = detailDescription;
        this.similarDts = createSimilarDts?new ArrayList<>():null;
    }

    public SingleDtsBase(String id, String severity
            , boolean reappearable, String simpleDescription
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
            , boolean reappearable, String Description)
    {
        this.id = id;
        this.severity = severity;
        this.reappearable = reappearable;
        this.simpleDescription = null;
        this.detailDescription = detailDescription;
        this.similarDts = null;
    }
}
