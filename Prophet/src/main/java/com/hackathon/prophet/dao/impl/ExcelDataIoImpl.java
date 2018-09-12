package com.hackathon.prophet.dao.impl;

import com.hackathon.prophet.dao.DataIO;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.SingleDtsBase;
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
        List<SingleDtsBase> dtses = ExcelUtils.parse(train, SingleDtsBase.class, xml);
        return new DataObject<SingleDtsBase>(dtses);
    }

    @Override
    public DataObject readTestSet()
    {
        File train = new File(ExcelDataIoImpl.class.getClassLoader().getResource("test.xlsx").getPath());
        String xml = "/TestMapper.xml";
        List<SingleDtsBase> dtses = ExcelUtils.parse(train, SingleDtsBase.class, xml);
        return new DataObject<SingleDtsBase>(dtses);
    }

    public boolean appendWrite()
    {
        return true;
    }
}
