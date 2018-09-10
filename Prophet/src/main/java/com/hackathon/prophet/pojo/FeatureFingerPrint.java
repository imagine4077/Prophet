package com.hackathon.prophet.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FeatureFingerPrint
{
    List<Float> features;

    public FeatureFingerPrint(List<Float> features)
    {
        this.features = features;
    }

    public FeatureFingerPrint(float[] features)
    {
        this.features = new ArrayList<>();
        for(float f: features)
        {
            this.features.add(f);
        }
    }
}
