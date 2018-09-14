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

    private static final String SEPERATOR1 = "/*prophet*/";

    private static final String SEPERATOR2 = ",";

    private static final String DTS_INFO_FILE_SUFFIX = "_dts_info";

    public static final String BASE_DIR = FileUtils.class.getClassLoader().getResource(".").getPath().toString();

    /**
     * 保存word bag的hash指纹
     */
    public static void saveVector(String fileName, FeatureFingerPrint feature, boolean append) {
        File file = new File(BASE_DIR+fileName);
        String data = encodeVector(feature);
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

    public static List<FeatureFingerPrint> loadVectors(String fileName)
    {
        File file = new File(BASE_DIR+fileName);
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
     * 保存DTS信息
     */
    public static void saveDtsInfo(String fileName, SingleDtsBase dts, boolean append) {
        File file = new File(BASE_DIR + fileName + DTS_INFO_FILE_SUFFIX);
        String data = encodeDts(dts);
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

    public static SingleDtsBase loadDtsInfo(String fileName, String dtsId)
    {
        File file = new File(BASE_DIR + fileName + DTS_INFO_FILE_SUFFIX);
        SingleDtsBase ret = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file.toString())))
        {
            String data = br.readLine();
            while(null != data)
            {
                SingleDtsBase tmpDts = decodeDts(data);
                if(tmpDts.getId().equalsIgnoreCase(dtsId)) {
                    ret = tmpDts;
                    break;
                }
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
     * 保存词数据用于word2vec学习
     */
    public static void saveSegmentForWord2vec(String filename, List<String> words, boolean append) {
        File file = new File(BASE_DIR + filename);
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

    private static String encodeDts(SingleDtsBase dts)
    {
        StringBuffer ret = new StringBuffer();
        ret.append(dts.getId()+ SEPERATOR1);
        ret.append(dts.getSeverity() + SEPERATOR1);
        ret.append(dts.getReappearable()+ SEPERATOR1);
        ret.append(dts.getSimpleDescription()+ SEPERATOR1);
        ret.append(dts.getDetailDescription());
        return ret.toString();
    }

    private static SingleDtsBase decodeDts(String data)
    {
        String[] dataitem = data.split(SEPERATOR1);
        SingleDtsBase ret = new SingleDtsBase();
        ret.setId(dataitem[0]);
        ret.setSeverity(dataitem[1]);
        ret.setReappearable(dataitem[2]);
        ret.setSimpleDescription(dataitem[3]);
        ret.setDetailDescription(dataitem[4]);
        return ret;
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
