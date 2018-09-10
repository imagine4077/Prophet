package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.atom.service.SegementationService;
import com.hackathon.prophet.dao.DataIO;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.utils.HtmlUtils;
import groovy.lang.Singleton;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class TfidfFeatureServiceImpl implements FeatureService
{
    private static List<String> wordBag = null;

    private static Object writeLock = new Object();

    private DataObject dataObject;

    @Autowired
    DataIO dataIO;

    @Autowired
    SegementationService segementationService;

    @Override
    public float[] getFeature()
    {
        return null;
    }

    @Override
    public List<String> getWordBag()
    {
        if(null == wordBag)
        {
            Set<String> tmpWordSet = new HashSet<>();
            while (!this.dataObject.isDataEnd())
            {
                SingleDtsBase dts = this.dataObject.getNextLine();
                tmpWordSet.addAll(descriptionWordSegment(dts));
            }
            wordBag = new ArrayList<>(tmpWordSet);
        }
        return wordBag;
    }

    @PostConstruct
    private void init()
    {
        this.dataObject = dataIO.getDo("");
    }

    private List<String> descriptionWordSegment(SingleDtsBase dts)
    {
        if(dts.getSimpleDescription() == null)
        {
            return segementationService.segmentWords(HtmlUtils.deleteAllHTMLTag(dts.getDetailDescription()));
        }
        else
        {
            List<String> words = segementationService.segmentWords(HtmlUtils.deleteAllHTMLTag(dts.getDetailDescription()));
            words.addAll(segementationService.segmentWords(HtmlUtils.deleteAllHTMLTag(dts.getSimpleDescription())));
            return words;
        }
    }
}
