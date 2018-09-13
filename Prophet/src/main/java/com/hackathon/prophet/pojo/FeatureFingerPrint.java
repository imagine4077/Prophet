package com.hackathon.prophet.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FeatureFingerPrint
{
    private String id;

    private List<Double> features;

    public FeatureFingerPrint(List<Double> features)
    {
        this.features = features;
    }

    public FeatureFingerPrint(String id, List<Double> features)
    {
        this.id = id;
        this.features = features;
    }

    public FeatureFingerPrint(String id, double[] features)
    {
        this.features = new ArrayList<>();
        for(double f: features)
        {
            this.features.add(f);
        }
        this.id = id;
    }

    public FeatureFingerPrint(SingleDtsBase dts)
    {
        this.id = dts.getId();
    }

    public double[] getArrayFeature()
    {
        double[] ret = new double[this.features.size()];
        for(int i=0;i<this.features.size();i++)
        {
            ret[i] = this.features.get(i);
        }
        return ret;
    }
}
