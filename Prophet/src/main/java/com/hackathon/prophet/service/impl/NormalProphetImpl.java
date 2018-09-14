package com.hackathon.prophet.service.impl;

import com.hackathon.prophet.atom.service.*;
import com.hackathon.prophet.dao.DataIO;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.service.Prophet;
import com.hackathon.prophet.utils.CollectionUtils;
import com.hackathon.prophet.utils.FileUtils;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

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
    private DistanceService distanceService;

    @Autowired
    private HashService hashService;

    @Value("${feature.reduce:true}")
    private boolean featureReduce;

    @Value("${append.received.data:true}")
    private boolean appendData;

    @Value("${knn.num:3}")
    private int knnNum;

    @Value("${distance.threshold:1}")
    private double distanceThreashold;

    //private DataObject dataObject;

    private List<FeatureFingerPrint> trainSetFingerPrints = new LinkedList<>();

    @Override
    @PostConstruct
    public void init()
    {
        // 获取训练集数据
        DataObject dataObject = dataIO.readTrainSet();
        // 初始化训练集FeatureFingerPrint，并存入this.trainSetfingerPrints。获得的double矩阵，可用于主成分分析
        double[][] trainMatrix = getTrainMatrix(dataObject);

        // 需要降维的场景所需做的降维处理
        if(this.featureReduce) {
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
        dataObject.resetPointer();

        // 对训练集数据做Hash处理
        hashService.initHashTable(this.trainSetFingerPrints, dataObject);

    }

    @Override
    public List<SingleDtsBase> getSimilarDts(SingleDtsBase dts)
    {
        // 获得DTS指纹
        FeatureFingerPrint fingerPrint;
        if(this.featureReduce)
        {
            fingerPrint = dtsToReducedFeatureFingerPrint(dts);
        }
        else {
            fingerPrint = dtsToUnreducedFeatureFingerPrint(dts);
        }

        //求LSH
        return getLsh(fingerPrint, dts);
    }

    // 获取训练集矩阵
    private double[][] getTrainMatrix(DataObject dataObject)
    {
        //重置训练集指针
        dataObject.resetPointer();

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

    private List<SingleDtsBase> getLsh(FeatureFingerPrint fingerPrint, SingleDtsBase dtsInfo){
        // 获得哈希值
        Pair<String, List<FeatureFingerPrint>> hashedPair = hashService.getHash(fingerPrint, dtsInfo, this.appendData);
        String center = hashedPair.getKey();

        Map<String, Double> distanceMap = new HashMap<>();
        for(FeatureFingerPrint hashedFeature: hashedPair.getValue())
        {
            distanceMap.put(hashedFeature.getId(),
                    this.distanceService.getDistance(fingerPrint.getArrayFeature(), hashedFeature.getArrayFeature()));
        }

        // 降序排序所有距离
        distanceMap = CollectionUtils.mapSortByValueDesc(distanceMap);

        // 抽取K近邻
        int i = 0;
        List<SingleDtsBase> ret = new ArrayList<>();
        for(Map.Entry<String, Double> entry: distanceMap.entrySet())
        {
            if(i<this.knnNum && entry.getValue()<this.distanceThreashold)
            {
                SingleDtsBase similarDts = FileUtils.loadDtsInfo(center, entry.getKey());
                ret.add(similarDts);
            }
        }

        return ret;
    }
}
