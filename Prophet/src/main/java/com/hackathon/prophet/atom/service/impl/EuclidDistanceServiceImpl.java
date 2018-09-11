package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.DistanceService;
import com.hackathon.prophet.utils.MatrixUtils;

public class EuclidDistanceServiceImpl implements DistanceService
{
    @Override
    public double getDistance(double[] vec1, double[] vec2)
    {
        double[] vec = new double[vec1.length];
        for(int i=0;i<vec.length;i++){
            vec[i] = vec1[i] - vec2[i];
        }
        return Math.sqrt(MatrixUtils.getVectorMode(vec));
    }
}
