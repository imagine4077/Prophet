package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.DistanceService;
import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.atom.service.HashService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KmeansHashServiceImpl implements HashService
{
    @Autowired
    private DistanceService distanceService;

    @Autowired
    private FeatureService featureService;

    @Value("${kmeans.cluster.num: 10}")
    private int clusterNum;

    private Map<String, Pair<FeatureFingerPrint ,String>> kmeans = new HashMap<>();

    @Override
    public void initHashTable(List<SingleDtsBase> trainData)
    {
        int[] meansIndex = randomCommon(0, trainData.size(), clusterNum);
        if(null == meansIndex) return;;
        for(int index: meansIndex)
        {

        }
    }

    @Override
    public List<SingleDtsBase> getHash( SingleDtsBase dts)
    {
        return null;
    }

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
}
