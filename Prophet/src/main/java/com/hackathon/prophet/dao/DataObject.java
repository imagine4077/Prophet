package com.hackathon.prophet.dao;

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

    public int getSize()
    {
        return this.dtses.size();
    }

    public int getPointer()
    {
        return this.pointer;
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
