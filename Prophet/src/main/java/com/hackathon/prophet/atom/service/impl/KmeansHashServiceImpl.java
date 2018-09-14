package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.DistanceService;
import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.atom.service.HashService;
import com.hackathon.prophet.utils.FileUtils;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

public class KmeansHashServiceImpl implements HashService
{
    @Autowired
    private DistanceService distanceService;

    @Autowired
    private FeatureService featureService;

    @Value("${kmeans.cluster.num: 10}")
    private int clusterNum;

    private Map<String, FeatureFingerPrint> kmeans = new HashMap<>();

    private String defaultCenter;

    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    private static int[] randomCommon(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    @Override
    public void initHashTable(List<FeatureFingerPrint> trainData, DataObject dataObject)
    {
        // 获得kmeans中心个数
        clusterNum = clusterNum<trainData.size()? clusterNum:trainData.size();
        // 从训练集中随机获得N个kmeans中心点
        int[] meansIndex = randomCommon(0, trainData.size(), clusterNum);
        if(null == meansIndex) return;

        // 初始化 this.kmeans，key为DTS单号，同时也是此类数据FingerPrint被保存所在文件名
        for(int index: meansIndex)
        {
            FeatureFingerPrint feature = trainData.get(index);
            kmeans.put(feature.getId(), feature);
        }
        this.defaultCenter = trainData.get(meansIndex[0]).getId();

        // 对数据进行hash映射, 并将指纹存入对应对文件
        for(FeatureFingerPrint feature: trainData)
        {
            String center = getNearestCenter(feature);
            FileUtils.saveVector(center, feature, true);
            SingleDtsBase dtsInfo = getDtsInfo(feature.getId(), dataObject);
            FileUtils.saveDtsInfo(center, dtsInfo, true);
        }
    }

    @Override
    public Pair<String, List<FeatureFingerPrint>> getHash( FeatureFingerPrint dts, SingleDtsBase dtsInfo, boolean append)
    {
        String center = getNearestCenter(dts);
        List<FeatureFingerPrint> ret = FileUtils.loadVectors(center);
        // 若新数据需要保存，且数据集中没有这个DTS，则存入
        if(append && !isContain(ret, dts)) {
            FileUtils.saveVector(center, dts, append);
            FileUtils.saveDtsInfo(center, dtsInfo, append);
        }
        return new Pair<String, List<FeatureFingerPrint>>(center, ret);
    }

    private String getNearestCenter(FeatureFingerPrint feature)
    {
        double nearestDis = Double.POSITIVE_INFINITY;
        String nearestKey = this.defaultCenter;
        for(FeatureFingerPrint center: this.kmeans.values())
        {
            double dis = this.distanceService.getDistance(feature.getArrayFeature(), center.getArrayFeature());
            if(nearestDis>dis)
            {
                nearestDis = dis;
                nearestKey = center.getId();
            }
        }

        return nearestKey;
    }

    private boolean isContain(List<FeatureFingerPrint> hashed, FeatureFingerPrint dts)
    {
        List<String> idList = hashed.stream().map(FeatureFingerPrint::getId).collect(Collectors.toList());
        return idList.contains(dts.getId());
    }

    private SingleDtsBase getDtsInfo(String dtsId, DataObject dataObject)
    {
        dataObject.resetPointer();
        SingleDtsBase dts = null;
        while(!dataObject.isDataEnd())
        {
            dts = (SingleDtsBase) dataObject.getNextLine();
            if(dtsId.equalsIgnoreCase(dts.getId()))
            {
                break;
            }
        }
        return dts;
    }
}
