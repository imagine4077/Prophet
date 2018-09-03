package com.hackathon.prophet.controller;

import com.hackathon.prophet.pojo.SingleDtsBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PredictController
{
    @RequestMapping(value = "/similarities", method = RequestMethod.GET)
    public List<SingleDtsBase> findSimilarDtsByUrl(
            @RequestParam(name = "url") String url
    )
    {
        return null;
    }

}
