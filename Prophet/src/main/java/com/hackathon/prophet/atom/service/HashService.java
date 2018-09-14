package com.hackathon.prophet.atom.service;

import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;

import java.util.List;

public interface HashService
{
    void initHashTable(List<FeatureFingerPrint> trainData);

    List<FeatureFingerPrint> getHash( FeatureFingerPrint dts, boolean append);
}
