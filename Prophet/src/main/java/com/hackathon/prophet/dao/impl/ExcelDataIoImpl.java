package com.hackathon.prophet.dao.impl;

import com.hackathon.prophet.dao.DataIO;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.DtsBase;
import com.hackathon.prophet.utils.ExcelUtils;

import java.io.File;
import java.util.List;

public class ExcelDataIoImpl implements DataIO
{
    @Override
    public DataObject readTrainSet()
    {
        File train = new File(ExcelDataIoImpl.class.getClassLoader().getResource("train.xlsx").getPath());
        String xml = "/TrainMapper.xml";
        List<DtsBase> dtses = ExcelUtils.parse(train, DtsBase.class, xml);
        return new DataObject<DtsBase>(dtses);
    }

    @Override
    public DataObject readTestSet()
    {
        File train = new File(ExcelDataIoImpl.class.getClassLoader().getResource("test.xlsx").getPath());
        String xml = "/TestMapper.xml";
        List<DtsBase> dtses = ExcelUtils.parse(train, DtsBase.class, xml);
        return new DataObject<DtsBase>(dtses);
    }

    public boolean appendWrite()
    {
        return true;
    }
}
