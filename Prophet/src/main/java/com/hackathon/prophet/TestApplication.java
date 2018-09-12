package com.hackathon.prophet;


import com.hackathon.prophet.atom.service.impl.JiebaSegementationServiceImpl;
import com.hackathon.prophet.dao.DataObject;
import com.hackathon.prophet.pojo.SingleDtsBase;
import com.hackathon.prophet.utils.CollectionUtils;
import com.hackathon.prophet.utils.ExcelUtils;

import java.io.File;
import java.util.*;


public class TestApplication
{
    public static void main(String[] args)
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
        idf = CollectionUtils.mapSortByValue(idf);
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
    }
}