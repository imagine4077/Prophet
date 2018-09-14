package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.DimensionalityService;
import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.atom.service.SegementationService;
import com.hackathon.prophet.dao.DataIO;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.utils.CollectionUtils;
import com.hackathon.prophet.utils.FileUtils;
import com.hackathon.prophet.utils.HtmlUtils;
import com.hackathon.prophet.word2vec.Word2VEC;
import com.hackathon.prophet.word2vec.Word2vecLearn;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Word2vecFeatureServiceImpl implements FeatureService
{
    private static Object writeLock = new Object();

    private static Map<String, Double> wordBag = null;

    private static String RAW_WORDS_FILE = "rawWords.txt";

    private static String WORD2VEC_MODEL = "word2vec_model";

    @Getter
    private DataObject dataObject;

    private String modelDir = FileUtils.BASE_DIR + WORD2VEC_MODEL;

    @Value("${feature.dimension:10000}")
    private int dimension;

    @Autowired
    private DataIO dataIO;

    @Autowired
    private SegementationService segementationService;

    @Autowired
    private DimensionalityService dimensionalityService;

    @Override
    public FeatureFingerPrint getFeature(SingleDtsBase dts) {
        /*//若词袋未初始化，则初始化
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
        return new FeatureFingerPrint(dts.getId(), feature);*/
    }

    @Override
    public List<String> getWordBag() {
        if (null == wordBag) {
            synchronized (writeLock) {
                // 将数据分词，并存入文本，用于训练WORD2VEC模型
                while (!this.dataObject.isDataEnd()) {
                    Object dts = this.dataObject.getNextLine();
                    List<String> dtsWords = descriptionWordSegment((SingleDtsBase) dts);
                    FileUtils.saveSegmentForWord2vec(RAW_WORDS_FILE, dtsWords, true);
                }
                Word2vecLearn learn = new Word2vecLearn();
                Word2VEC vec = new Word2VEC();
                long start = System.currentTimeMillis();
                try {
                    // 训练word2vec模型
                    learn.learnFile(new File(FileUtils.BASE_DIR + RAW_WORDS_FILE));
                    System.out.println("use time " + (System.currentTimeMillis() - start));
                    learn.saveModel(new File(this.modelDir));

                    // 加载word2vec模型
                    vec.loadJavaModel(this.modelDir);
                }catch (IOException e){}



                // reduce dimension by config
                tmpMap = CollectionUtils.mapSortByValueAsc(tmpMap);
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
                wordBag = new ArrayList<>(idf.keySet());
            }
            this.dimension = wordBag.size();
        }
        return wordBag;
    }

    @Override
    public void init(DataObject dataObject) {
        this.dataObject = dataObject;//dataIO.readTrainSet();
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
