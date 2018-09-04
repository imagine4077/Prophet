package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.SegementationService;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.util.List;

public class SegementationServiceImpl implements SegementationService
{
    private static JiebaSegmenter segmenter = new JiebaSegmenter();

    @Override
    public List<SegToken> segmentWordsByIndexMode(String text) {
        return segmenter.process(text, JiebaSegmenter.SegMode.INDEX);
    }

    @Override
    public List<SegToken> segmentWordsBySearchMode(String text)
    {
        return segmenter.process(text, JiebaSegmenter.SegMode.SEARCH);
    }
}
