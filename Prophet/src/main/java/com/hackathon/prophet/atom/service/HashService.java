package com.hackathon.prophet.atom.service;

import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;
import javafx.util.Pair;

import java.util.List;

public interface HashService
{
    void initHashTable(List<FeatureFingerPrint> trainData, DataObject dataObject);

    Pair<String, List<FeatureFingerPrint>> getHash(FeatureFingerPrint dts, SingleDtsBase dtsInfo, boolean append);
}
