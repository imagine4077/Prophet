package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.DimensionalityService;
import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.atom.service.SegementationService;
import com.hackathon.prophet.dao.DataIO;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.DtsBase;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.utils.CollectionUtils;
import com.hackathon.prophet.utils.HtmlUtils;
import groovy.lang.Singleton;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@Singleton
public class WordBagFeatureServiceImpl implements FeatureService {
    private static Object writeLock = new Object();

    private static Map<String, Integer> idf = null;

    private static List<String> wordBag = null;

    @Getter
    private DataObject dataObject;

    @Value("${feature.dimension:10000}")
    private int dimension;

    @Autowired
    private DataIO dataIO;

    @Autowired
    private SegementationService segementationService;

    @Autowired
    private DimensionalityService dimensionalityService;

    @Override
    public FeatureFingerPrint getFeature(DtsBase dts) {
        //若词袋未初始化，则初始化
        if(null == wordBag)
        {
            this.getWordBag();
        }

        double[] feature = new double[this.dimension];

        for(String word:this.descriptionWordSegment(dts))
        {
            if(wordBag.contains(word))
            {
                int index = wordBag.indexOf(word);
                feature[index] = feature[index]+1F;
            }
        }
        return new FeatureFingerPrint(dts.getId(), feature);
    }

    @Override
    public List<String> getWordBag() {
        if (null == this.wordBag) {
            synchronized (writeLock) {
                Map<String, Integer> tmpMap = new LinkedHashMap<>();
                while (!this.dataObject.isDataEnd()) {
                    Object dts = this.dataObject.getNextLine();
                    for (String word : descriptionWordSegment((DtsBase) dts)) {
                        if (!tmpMap.containsKey(word)) {
                            tmpMap.put(word, 1);
                        } else {
                            tmpMap.put(word, tmpMap.get(word) + 1);
                        }
                    }
                }

                // reduce dimension by config
                /*tmpMap = CollectionUtils.mapSortByValue(tmpMap);*/
                tmpMap = CollectionUtils.mapSortByValueDesc(tmpMap);
                this.dimension = this.dimension>tmpMap.size()?tmpMap.size(): this.dimension;
                idf = new LinkedHashMap<>();
                int i = 0;
                for (Map.Entry<String, Integer> entry : tmpMap.entrySet()) {
                    // FIXME 在全体训练集中仅出现一次的词，我们认为可能是无意义词,不统计这样的词，用以降维
                    if(entry.getValue()>1)
                    {
                        idf.put(entry.getKey(), entry.getValue());
                        i++;
                    }
                    if (i >= this.dimension) {
                        break;
                    }
                }
                this.wordBag = new ArrayList<>(idf.keySet());
            }
            this.dimension = this.wordBag.size();
        }
        return this.wordBag;
    }

    @Override
    public void init(DataObject dataObject) {
        this.dataObject = dataObject;
        this.getWordBag();
    }

    private List<String> descriptionWordSegment(DtsBase dts) {
        if (dts.getSimpleDescription() == null) {
            return segementationService.segmentWords(HtmlUtils.deleteAllHTMLTag(dts.getDetailDescription()));
        } else {
            List<String> words = segementationService.segmentWords(HtmlUtils.deleteAllHTMLTag(dts.getDetailDescription()));
            words.addAll(segementationService.segmentWords(HtmlUtils.deleteAllHTMLTag(dts.getSimpleDescription())));
            return words;
        }
    }
}
