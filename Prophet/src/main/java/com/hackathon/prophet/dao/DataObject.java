package com.hackathon.prophet.dao;

import com.hackathon.prophet.pojo.SingleDtsBase;

import java.util.List;

public class DataObject<T>
{
    private List<T> dtses;

    private int pointer;

    public DataObject(List<T> dts)
    {
        this.dtses = dts;
        pointer = 0;
    }

    public T getNextLine()
    {
        if(this.pointer> this.dtses.size())
        {
            return null;
        }
        int index = this.pointer;
        this.pointer++;
        return this.dtses.get(index);
    }

    public boolean isDataEnd()
    {
        if(this.pointer< this.dtses.size())
        {
            return false;
        }
        else {
            return true;
        }
    }

    public void resetPointer()
    {
        this.pointer = 0;
    }
}
