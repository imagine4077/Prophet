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
import groovy.lang.Singleton;
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

@Singleton
public class Word2vecFeatureServiceImpl implements FeatureService
{
    private static Object writeLock = new Object();

    private static Map<String, double[]> wordBag = null;

    private static final String RAW_WORDS_FILE = "rawWords.txt";

    private static final String WORD2VEC_MODEL = "word2vec_model";

    private final String modelDir = FileUtils.BASE_DIR + WORD2VEC_MODEL;

    @Getter
    private DataObject dataObject;

    private Word2VEC word2Vec = new Word2VEC();

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
        //若词袋未初始化，则初始化
        if(null == wordBag)
        {
            this.getWordBag();
        }

        double[] feature = new double[this.dimension];

        int i = 0;
        for(String word:this.descriptionWordSegment(dts))
        {
            if(wordBag.containsKey(word))
            {
                feature = vecAdd(feature, wordBag.get(word));
                i++;
            }
        }
        return new FeatureFingerPrint(dts.getId(), vecDividNum(feature, i));
    }

    @Override
    public List<String> getWordBag() {
        List<String> ret = null;
        if (null == wordBag) {
            synchronized (writeLock) {
                // 将数据分词，并存入文本，用于训练WORD2VEC模型
                while (!this.dataObject.isDataEnd()) {
                    Object dts = this.dataObject.getNextLine();
                    List<String> dtsWords = descriptionWordSegment((SingleDtsBase) dts);
                    FileUtils.saveSegmentForWord2vec(RAW_WORDS_FILE, dtsWords, true);
                }
                Word2vecLearn learn = new Word2vecLearn();
                long start = System.currentTimeMillis();
                try {
                    // 训练word2vec模型
                    learn.learnFile(new File(FileUtils.BASE_DIR + RAW_WORDS_FILE));
                    System.out.println("use time " + (System.currentTimeMillis() - start));
                    learn.saveModel(new File(this.modelDir));

                    // 加载word2vec模型
                    this.word2Vec.loadJavaModel(this.modelDir);
                    wordBag = this.word2Vec.getDoubleWordMap();
                }catch (IOException e){}

            }
            ret = new ArrayList<String>(wordBag.keySet());
            this.dimension = wordBag.get(ret.get(0)).length;
        }
        else {
            ret = new ArrayList<String>(wordBag.keySet());
        }
        return ret;
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

    private double[] vecAdd(double[] vec1, double[] vec2)
    {
        if(vec1.length != this.dimension || vec2.length != this.dimension)
        {
            return null;
        }
        double[] result = new double[this.dimension];
        for(int i=0;i<this.dimension;i++){
            result[i] = vec1[i] + vec2[i];
        }
        return result;
    }

    private double[] vecDividNum(double[] vec, int n)
    {
        double[] result = new double[vec.length];
        if(n==0)
        {
            System.out.println("word2vec divid 0: no feature word exist");
            return result;
        }
        for(int i=0;i<vec.length;i++)
        {
            result[i] = result[i]/n;
        }
        return result;
    }
}
