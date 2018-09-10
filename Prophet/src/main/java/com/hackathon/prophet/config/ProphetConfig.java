package com.hackathon.prophet.config;

import com.hackathon.prophet.atom.service.FeatureService;
import com.hackathon.prophet.atom.service.SegementationService;
import com.hackathon.prophet.atom.service.impl.IkanalyzerSegementationServiceImpl;
import com.hackathon.prophet.atom.service.impl.TfidfFeatureServiceImpl;
import com.hackathon.prophet.atom.service.impl.Word2vecFeatureServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProphetConfig
{
    private static String TFIDF_FEATURE_TYPE = "TFIDF";

    private static String WORD2VEC_FEATURE_TYPE = "word2vec";

    @Value("${feature.type:tfidf}")
    String featureType;

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
            return new TfidfFeatureServiceImpl();
        }
    }
}
