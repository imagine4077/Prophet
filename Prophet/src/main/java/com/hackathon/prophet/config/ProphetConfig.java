package com.hackathon.prophet.config;

import com.hackathon.prophet.atom.service.*;
import com.hackathon.prophet.atom.service.impl.*;
import com.hackathon.prophet.dao.DataIO;
import com.hackathon.prophet.dao.impl.ExcelDataIoImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProphetConfig
{
    private static String TFIDF_FEATURE_TYPE = "TFIDF";

    private static String WORD2VEC_FEATURE_TYPE = "word2vec";

    private static String COS_DISTANCE = "cos";

    private static String EUCLID_DISTANCE = "Euclid";

    @Value("${feature.type:tfidf}")
    String featureType;

    @Value("${distance.type:cos}")
    String distanceType;

    @Bean
    public SegementationService segementationServiceBean()
    {
        return new IkanalyzerSegementationServiceImpl();
    }

    @Bean
    public FeatureService featureServiceBean()
    {
        if(this.featureType.equalsIgnoreCase(WORD2VEC_FEATURE_TYPE))
        {
            return new Word2vecFeatureServiceImpl();
        }
        else
        {
            return new WordBagFeatureServiceImpl();
        }
    }

    @Bean
    public DistanceService distanceServiceBean()
    {
        if(this.distanceType.equalsIgnoreCase(EUCLID_DISTANCE))
        {
            return new EuclidDistanceServiceImpl();
        }
        else { // 默认使用余弦距离
            return new CosDistanceServiceImpl();
        }
    }

    @Bean
    public DataIO dataIOBean()
    {
        return new ExcelDataIoImpl();
    }

    @Bean
    public DimensionalityService dimensionalityServiceBean()
    {
        return new PcaDimensionalityServiceImpl();
    }

    @Bean
    public HashService hashServiceBean()
    {
        return new KmeansHashServiceImpl();
    }
}
