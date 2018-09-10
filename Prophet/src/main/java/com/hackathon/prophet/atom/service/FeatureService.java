package com.hackathon.prophet.atom.service;

import com.hackathon.prophet.pojo.SingleDtsBase;

import java.util.List;

public interface FeatureService
{
    /**
     * 提取单个DTS的特征
     * */
    List<Float> getFeature(SingleDtsBase dts);

    /**
     * 获得词袋
     * */
    List<String> getWordBag();
}
