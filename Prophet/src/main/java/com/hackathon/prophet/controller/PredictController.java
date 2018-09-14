package com.hackathon.prophet.controller;

import com.hackathon.prophet.pojo.DtsBase;
import com.hackathon.prophet.service.Prophet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
public class PredictController
{
    @Autowired
    private Prophet prophet;

    @ResponseBody
    @RequestMapping(value = "url/similarities", method = RequestMethod.GET)
    public ResponseEntity<List<DtsBase>> findSimilarDtsByUrl(
            @RequestParam(name = "url") String url
    )
    {
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "similarities", method = RequestMethod.POST)
    public ResponseEntity<List<DtsBase>> findSimilarDts(
            @RequestBody DtsBase dts
    )
    {
        List<DtsBase> result = this.prophet.getSimilarDts(dts);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
