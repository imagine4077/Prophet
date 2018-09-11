package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.DistanceService;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.atom.service.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KmeansHashServiceImpl implements HashService
{
    @Autowired
    private DistanceService distanceService;

    @Value("${kmeans.cluster.num: 10}")
    private int clusterNum;

    private Map<List<Double>, String> kmeans = new HashMap<>();

    @Override
    public void initHashTable(List<SingleDtsBase> trainData)
    {
        ;
    }

    @Override
    public List<SingleDtsBase> getHash( SingleDtsBase dts)
    {
        return null;
    }
}
