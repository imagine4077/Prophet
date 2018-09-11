package com.hackathon.prophet.dao;

import com.hackathon.prophet.pojo.SingleDtsBase;

import java.util.List;

public class DataObject
{
    private List<SingleDtsBase> dtses;

    private String source;

    private int pointer;

    public DataObject(List<SingleDtsBase> dts){};

    public SingleDtsBase getNextLine()
    {
        return null;
    }

    public boolean isDataEnd()
    {
        return false;
    }
}
