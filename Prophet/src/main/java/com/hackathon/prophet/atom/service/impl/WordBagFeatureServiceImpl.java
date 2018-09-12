package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.atom.service.SegementationService;
import com.hackathon.prophet.dao.DataIO;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.utils.CollectionUtils;
import com.hackathon.prophet.utils.HtmlUtils;
import groovy.lang.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.*;

@Singleton
public class WordBagFeatureServiceImpl implements FeatureService {
    private static Object writeLock = new Object();

    private static Map<String, Integer> idf = null;

    private static List<String> wordBag = null;

    private DataObject dataObject;

    @Value("${feature.dimension:50000}")
    private int dimension;

    @Autowired
    DataIO dataIO;

    @Autowired
    SegementationService segementationService;

    @Override
    public FeatureFingerPrint getFeature(SingleDtsBase dts) {
        //若词袋未初始化，则初始化
        if(null == wordBag)
        {
            this.getWordBag();
        }

        float[] feature = new float[this.dimension];

        for(String word:this.descriptionWordSegment(dts))
        {
            int index = wordBag.indexOf(word);
            feature[index] = feature[index]+1F;
        }
        return new FeatureFingerPrint(feature);
    }

    @Override
    public List<String> getWordBag() {
        if (null == wordBag) {
            synchronized (writeLock) {
                Map<String, Integer> tmpMap = new LinkedHashMap<>();
                while (!this.dataObject.isDataEnd()) {
                    Object dts = this.dataObject.getNextLine();
                    for (String word : descriptionWordSegment((SingleDtsBase) dts)) {
                        if (!tmpMap.containsKey(word)) {
                            tmpMap.put(word, 1);
                        } else {
                            tmpMap.put(word, tmpMap.get(word) + 1);
                        }
                    }
                }

                // reduce dimension by config
                tmpMap = CollectionUtils.mapSortByValue(tmpMap);
                idf = new LinkedHashMap<>();
                int i = 0;
                for (Map.Entry<String, Integer> entry : tmpMap.entrySet()) {
                    if (i >= this.dimension) {
                        break;
                    }
                    idf.put(entry.getKey(), entry.getValue());
                }
                wordBag = new ArrayList<>(idf.keySet());
            }
        }
        return wordBag;
    }

    @PostConstruct
    private void init() {
        this.dataObject = dataIO.readTrainSet();
        this.getWordBag();
    }

    private List<String> descriptionWordSegment(SingleDtsBase dts) {
        if (dts.getSimpleDescription() == null) {
            return segementationService.segmentWords(HtmlUtils.deleteAllHTMLTag(dts.getDetailDescription()));
        } else {
            List<String> words = segementationService.segmentWords(HtmlUtils.deleteAllHTMLTag(dts.getDetailDescription()));
            words.addAll(segementationService.segmentWords(HtmlUtils.deleteAllHTMLTag(dts.getSimpleDescription())));
            return words;
        }
    }
}
