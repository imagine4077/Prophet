package com.hackathon.prophet.atom.service.impl;

import com.hackathon.prophet.atom.service.SegementationService;
import org.apache.commons.io.FileUtils;
import org.apache.coyote.Constants;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class IkanalyzerSegementationServiceImpl implements SegementationService {
    private static List<String> stopWordsList;

    static
    {
        stopWordsList = getStopWordsList();
    }

    @Override
    public List<String> segmentWords(String text) {
        return getAnalyzerResult(text);
    }

    public static List<String> getAnalyzerResult(String input) {
        StringReader sr = new StringReader(input);
        IKSegmenter ik = new IKSegmenter(sr, true);//true is use smart
        Lexeme lex = null;
        List<String> ret = new ArrayList<>();

        try {
            while ((lex = ik.next()) != null) {
                if (stopWordsList.contains(lex.getLexemeText())) {
                    continue;
                }
                ret.add(lex.getLexemeText());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("failed to parse input content");
        }
        return ret;
    }

    private static List<String> getStopWordsList() {
        List<String> stopWordList = null;
        File stopWordFile = new File(IkanalyzerSegementationServiceImpl.class.getClassLoader().getResource("stopwords.dic").getPath());
        try {
            stopWordList = FileUtils.readLines(stopWordFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fail to load stop word dictionary");
        }
        return stopWordList;
    }

    public static void main(String[] args) {
        List<String> t = getAnalyzerResult("不同于计算机，人类一睁眼就能迅速看到和看明白一个场景，因为人的大脑皮层至少有一半以上海量神经元参与了视觉任务的完成。");
        System.out.println(t);
    }
}
