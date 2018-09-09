package com.hackathon.prophet;


import com.hackathon.prophet.atom.service.impl.JiebaSegementationServiceImpl;


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
        String text = "现有Doc-word矩阵，采用余弦计算两两文档之间的相似度。" +
                "在实际问题中，矩阵通常是很稀疏的，为了减少计算量，" +
                "通常采用倒排索引的数据结构";
        JiebaSegementationServiceImpl nlpService = new JiebaSegementationServiceImpl();
        //System.out.println(nlpService.segmentWordsByIndexMode(text).toString());
        System.out.println(nlpService.segmentWordsBySearchMode(text).toString());
    }
}