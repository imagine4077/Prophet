package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;

import java.util.List;

public class Word2vecFeatureServiceImpl implements FeatureService
{
    @Override
    public FeatureFingerPrint getFeature(SingleDtsBase dts)
    {
        return null;
    }

    /**
     * 获得词袋
     * */
    @Override
    public List<String> getWordBag()
    {
        return null;
    }
}
