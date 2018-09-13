package com.hackathon.prophet.service.impl;

import com.hackathon.prophet.atom.service.DimensionalityService;
import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.atom.service.SegementationService;
import com.hackathon.prophet.dao.DataIO;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.service.Prophet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class NormalProphetImpl implements Prophet
{
    @Autowired
    private SegementationService segementationService;

    @Autowired
    private DimensionalityService pca;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private DataIO dataIO;

    private DataObject dataObject;

    private List<FeatureFingerPrint> trainSetfingerPrints = new LinkedList<>();

    @Override
    @PostConstruct
    public void init()
    {
        this.dataObject = dataIO.readTrainSet();
        pca.trainTransformMatrix( getTrainMatrix());
        this.dataObject.resetPointer();
        for(FeatureFingerPrint fingerPrint: this.trainSetfingerPrints)
        {
            double[][] feature = pca.getPrincipalMatrix(fingerPrint.getMatrixFeature()).getArray();
        }
    }

    private double[][] getTrainMatrix()
    {
        // 获取训练集矩阵
        List<double[]> trainMatrix = new ArrayList<>();
        while(!dataObject.isDataEnd())
        {
            SingleDtsBase dts = (SingleDtsBase)dataObject.getNextLine();
            FeatureFingerPrint fingerPrint = featureService.getFeature(dts);
            trainSetfingerPrints.add(fingerPrint);
            trainMatrix.add(fingerPrint.getArrayFeature());
            System.out.println(fingerPrint); //////////////////////
        }

        double[][] ret = new double[trainMatrix.size()][trainMatrix.get(0).length];
        for(int i=0;i<trainMatrix.size();i++)
        {
            ret[i] = trainMatrix.get(i);
        }
        return ret;
    }
}
