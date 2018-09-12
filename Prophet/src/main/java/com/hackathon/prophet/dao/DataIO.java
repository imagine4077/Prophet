package com.hackathon.prophet.dao;

public interface DataIO
{
    DataObject readTrainSet();

    DataObject readTestSet();
}
