package com.hackathon.prophet.service;

import com.hackathon.prophet.pojo.DtsBase;

import java.util.List;

public interface Prophet
{
    void init();

    List<DtsBase> getSimilarDts(DtsBase dts);
}
