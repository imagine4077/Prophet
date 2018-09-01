package com.hackthon.prophet.service;

import com.huaban.analysis.jieba.SegToken;

import java.util.List;

public interface NlpService
{
    /**
     * INDEX模式下，对长的词句不仅将其自身加入token，并且将其中的长度为2和3的词也加入token中
     * */
    List<SegToken> segmentWordsByIndexMode(String text);

    /**
     * SEARCH模式下，只处理一次句子，不对长的词句再次分解
     * */
    List<SegToken> segmentWordsBySearchMode(String text);

    String filterHtlmTags(String text);
}
