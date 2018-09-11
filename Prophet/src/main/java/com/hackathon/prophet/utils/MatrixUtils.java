package com.hackathon.prophet.utils;

import Jama.Matrix;

public class MatrixUtils
{
    public static double vectorDotMultiply(double[] vec1, double[] vec2)
    {
        if(vec1.length != vec2.length)
        {
            //throw new;
        }
        double ret = 0;
        for(int i=0; i<vec1.length; i++){
            ret += vec1[i]*vec2[i];
        }
        return ret;
    }

    public static double getVectorMode(double[] vec)
    {
        return Math.sqrt( vectorDotMultiply(vec, vec));
    }
}
