package com.hackathon.prophet.atom.service;

import Jama.Matrix;

public interface DimensionalityService
{
    // 训练PCA模型，获得主成分及用于降维对转换矩阵
    void trainTransformMatrix(double[][] primaryArray);

    // 对primaryArray矩阵做PCA降维
    Matrix getPrincipalMatrix(double[][] primaryArray);
}
