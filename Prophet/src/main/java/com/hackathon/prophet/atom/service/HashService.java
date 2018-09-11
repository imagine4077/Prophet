package com.hackathon.prophet.atom.service;

import com.hackathon.prophet.pojo.SingleDtsBase;

import java.util.List;

public interface HashService
{
    void initHashTable(List<SingleDtsBase> trainData);

    List<SingleDtsBase> getHash( SingleDtsBase dts);
}
