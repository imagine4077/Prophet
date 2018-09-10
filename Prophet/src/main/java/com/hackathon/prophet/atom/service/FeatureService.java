package com.hackathon.prophet.atom.service;

import java.util.List;

public interface FeatureService
{
    /**
     * 提取单个DTS的特征
     * */
    float[] getFeature();

    /**
     * 获得词袋
     * */
    List<String> getWordBag();
}
