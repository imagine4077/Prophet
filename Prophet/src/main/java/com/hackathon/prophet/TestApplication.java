package com.hackathon.prophet;


import com.hackathon.prophet.atom.service.DimensionalityService;
import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.atom.service.SegementationService;
import com.hackathon.prophet.atom.service.impl.JiebaSegementationServiceImpl;
import com.hackathon.prophet.atom.service.impl.PcaDimensionalityServiceImpl;
import com.hackathon.prophet.atom.service.impl.WordBagFeatureServiceImpl;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.FeatureFingerPrint;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.utils.CollectionUtils;
import com.hackathon.prophet.utils.ExcelUtils;
import com.hackathon.prophet.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.*;

//@SpringBootApplication
public class TestApplication
{
    @Autowired
    private SegementationService segementationService;

    @Autowired
    private DimensionalityService dimensionalityService;

    @Autowired
    private FeatureService featureService;

    public static void main2(String[] args)
    {
        System.out.println("Hello prophet!");

        TestApplication t = new TestApplication();
        t.run();
    }

    public void run()
    {
        // Test mapSortByValue
        Map<String, Integer> idf = new LinkedHashMap<>();
        idf.put("word2", 2);
        idf.put("cord1", 1);
        idf.put("aord3", 3);
        idf = CollectionUtils.mapSortByValueDesc(idf);
        for(Map.Entry<String, Integer> entry: idf.entrySet())
        {
            System.out.println("KEY: " + entry.getKey() + ", VALUE: " + entry.getValue());
        }

        // Test list initialization
        System.out.println("==================================");
        float[] feature = new float[5];
        feature[2] = 6F;
        for(float f: feature){
            System.out.println(f);
        }

        // Test list initialization
        System.out.println("==================================");
        File train = new File(TestApplication.class.getClassLoader().getResource("dummyTest.xlsx").getPath());
        String xml = "/TestMapper.xml";
        List<SingleDtsBase> dtses = ExcelUtils.parse(train, SingleDtsBase.class, xml);
        DataObject<SingleDtsBase> dataObject = new DataObject<>(dtses);
        while(!dataObject.isDataEnd()){
            System.out.println(dataObject.getNextLine());
        }

        // Test file input and output
        System.out.println("==================================");
        //File hashFile = new File(TestApplication.class.getClassLoader().getResource(".").getPath().toString() + "feature.txt");
        double[] arr1 = {1.1, 2.2, 3.3};
        double[] arr2 = {1.1, 1.2, 1.3};
        FileUtils.saveVector("feature.txt", new FeatureFingerPrint("Dts1", arr1), true);
        FileUtils.saveVector("feature.txt", new FeatureFingerPrint("Dts2", arr1), true);
        FileUtils.saveVector("feature.txt", new FeatureFingerPrint("Dts3", arr2), true);

        List<FeatureFingerPrint> featureFingerPrints = FileUtils.loadVectors("feature.txt");
        for (FeatureFingerPrint f: featureFingerPrints)
        {
            System.out.println(f);
        }

        // Test PCA
        WordBagFeatureServiceImpl featureService = new WordBagFeatureServiceImpl();
        featureService.init(dataObject);


        List<String> wordBag = featureService.getWordBag();
        dataObject.resetPointer();

             // 获取训练机矩阵
        List<double[]> trainMatrix = new ArrayList<>();
        while(!dataObject.isDataEnd())
        {
            SingleDtsBase dts = dataObject.getNextLine();
            FeatureFingerPrint fingerPrint = featureService.getFeature(dts);
            trainMatrix.add(fingerPrint.getArrayFeature());
            System.out.println(fingerPrint);
        }

             // 获取主成分矩阵矩阵
        PcaDimensionalityServiceImpl pca = new PcaDimensionalityServiceImpl();
        pca.trainTransformMatrix((double[][]) trainMatrix.toArray());

        // Test kmeans
    }
}