package com.hackathon.prophet.controller;

import com.hackathon.prophet.pojo.SingleDtsBase;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

@Controller
public class PredictController
{
    @ResponseBody
    @RequestMapping(value = "similarities", method = RequestMethod.GET)
    public ResponseEntity<List<SingleDtsBase>> findSimilarDtsByUrl(
            @RequestParam(name = "url") String url
    )
    {
        return null;
    }

}
