package com.hackathon.prophet.atom.service;

import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;

import java.util.List;

public interface FeatureService
{
    /**
     * 提取单个DTS的特征
     * */
    FeatureFingerPrint getFeature(SingleDtsBase dts);

    /**
     * 获得词袋
     * */
    List<String> getWordBag();

    // 初始化
    public void init(DataObject dataObject);
}
