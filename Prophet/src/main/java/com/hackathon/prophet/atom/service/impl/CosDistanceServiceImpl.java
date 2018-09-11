package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.DistanceService;
import com.hackathon.prophet.utils.MatrixUtils;

public class CosDistanceServiceImpl implements DistanceService
{
    @Override
    public double getDistance(double[] vec1, double[] vec2)
    {
        return MatrixUtils.vectorDotMultiply(vec1, vec2)
                / ( MatrixUtils.getVectorMode(vec1) *MatrixUtils.getVectorMode(vec2));
    }
}
