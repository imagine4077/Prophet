package com.hackthon.prophet.service.impl;

import com.hackthon.prophet.service.NlpService;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.util.List;

public class NlpServiceImpl implements NlpService
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

    @Override
    public String filterHtlmTags(String text) {
        return null;
    }
}
