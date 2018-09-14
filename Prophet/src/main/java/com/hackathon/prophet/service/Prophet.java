package com.hackathon.prophet.service;

import com.hackathon.prophet.pojo.SingleDtsBase;

import java.util.List;

public interface Prophet
{
    void init();

    List<SingleDtsBase> getSimilarDts(SingleDtsBase dts);
}
