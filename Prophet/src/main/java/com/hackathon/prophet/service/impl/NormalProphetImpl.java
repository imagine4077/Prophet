package com.hackathon.prophet.service.impl;

import com.hackathon.prophet.atom.service.DimensionalityService;
import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.atom.service.HashService;
import com.hackathon.prophet.atom.service.SegementationService;
import com.hackathon.prophet.dao.DataIO;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.service.Prophet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.Finishings;
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

    @Autowired
    private HashService hashService;

    @Value("${feature.reduce:true}")
    private boolean featureReduce;

    private DataObject dataObject;

    private List<FeatureFingerPrint> trainSetFingerPrints = new LinkedList<>();

    @Override
    @PostConstruct
    public void init()
    {
        // 获取训练集数据
        this.dataObject = dataIO.readTrainSet();
        // 初始化训练集FeatureFingerPrint，并存入this.trainSetfingerPrints。获得的double矩阵，可用于主成分分析
        double[][] trainMatrix = getTrainMatrix();

        // 需要降维的场景所需做的降维处理
        if(featureReduce) {
            // 获取PCA主成分、获取转换矩阵
            pca.trainTransformMatrix(trainMatrix);

            // 将训练集数据全部降维
            List<FeatureFingerPrint> tmpFingerPrintList = new LinkedList<>();
            for(FeatureFingerPrint fingerPrint: this.trainSetFingerPrints)
            {
                double[][] feature = pca.getReducedVectors(fingerPrint.getMatrixFeature()).getArray();
                FeatureFingerPrint finger = new FeatureFingerPrint(fingerPrint.getId(), feature[0]);
                tmpFingerPrintList.add(finger);
            }
            this.trainSetFingerPrints = tmpFingerPrintList;
        }

        //重置训练集指针
        this.dataObject.resetPointer();

        // 对训练集数据做Hash处理

    }

    // 获取训练集矩阵
    private double[][] getTrainMatrix()
    {
        List<double[]> trainMatrix = new ArrayList<>();
        while(!dataObject.isDataEnd())
        {
            SingleDtsBase dts = (SingleDtsBase)dataObject.getNextLine();
            FeatureFingerPrint fingerPrint = featureService.getFeature(dts);
            this.trainSetFingerPrints.add(fingerPrint);
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

    // 将DTS转为最终形式（已经过PCA处理）的FeatureFingerPrint。主要用于词袋模型特征场景
    private FeatureFingerPrint dtsToReducedFeatureFingerPrint(SingleDtsBase dts)
    {
        FeatureFingerPrint fingerPrint = featureService.getFeature(dts);
        double[][] feature = pca.getReducedVectors(fingerPrint.getMatrixFeature()).getArray();
        return new FeatureFingerPrint(fingerPrint.getId(), feature[0]);
    }

    // 将DTS转为最终形式（已经过PCA处理）的FeatureFingerPrint。主要用于word2vec模型特征场景
    private FeatureFingerPrint dtsToUnreducedFeatureFingerPrint(SingleDtsBase dts)
    {
        return featureService.getFeature(dts);
    }
}
