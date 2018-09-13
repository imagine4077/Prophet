package com.hackathon.prophet.utils;

import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.word2vec.domain.Neuron;
import com.hackathon.prophet.word2vec.domain.WordNeuron;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtils
{
    private static final Object Lock = new Object();

    private static final String SEPERATOR1 = "/";

    private static final String SEPERATOR2 = ",";

    /**
     * 保存模型
     */
    public static void saveVector(File file, FeatureFingerPrint feature) {
        String data = encodeVector(feature);
        synchronized (Lock) {
            BufferedWriter bw = null;
            try {
                if(!file.exists())
                {
                    file.createNewFile();
                }
                bw = new BufferedWriter(new FileWriter(file.toString(), true));
                bw.write(data);
                bw.newLine();
                bw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            finally {

            }
        }
    }

    public static List<FeatureFingerPrint> loadVectors(File file)
    {
        List<FeatureFingerPrint> ret = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file.toString())))
        {
            String data = br.readLine();
            while(null != data)
            {
                ret.add(decodeVector(data));
                data = br.readLine();
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 保存模型
     */
    public static void saveSegmentForWord2vec(File file, List<String> words, boolean append) {
        String data = convertWordsToLineData(words);
        synchronized (Lock) {
            BufferedWriter bw = null;
            try {
                if(!file.exists())
                {
                    file.createNewFile();
                }
                bw = new BufferedWriter(new FileWriter(file.toString(), append));
                bw.write(data);
                bw.newLine();
                bw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            finally {

            }
        }
    }

    private static String encodeVector(FeatureFingerPrint feature)
    {
        StringBuffer ret = new StringBuffer();
        ret.append(feature.getId()+ SEPERATOR1);
        int k = feature.getFeatures().size();
        for(int i=0;i<(k-1);i++)
        {
            ret.append(feature.getFeatures().get(i)+ SEPERATOR2);
        }
        ret.append(feature.getFeatures().get(k-1));
        return ret.toString();
    }

    private static FeatureFingerPrint decodeVector(String data)
    {
        String[] dataitem = data.split(SEPERATOR1);
        String id = dataitem[0];
        String[] vectorString = dataitem[1].split(SEPERATOR2);
        List<Double> vec = new ArrayList<>();
        for(String v: vectorString)
        {
            vec.add(Double.parseDouble(v));
        }
        return new FeatureFingerPrint(id, vec);
    }

    private static String convertWordsToLineData(List<String> words)
    {
        StringBuffer str = new StringBuffer();
        for(int i=0;i<(words.size()-1);i++)
        {
            str.append(words.get(i)+" ");
        }
        str.append( words.get(words.size()-1));
        return str.toString();
    }
}
