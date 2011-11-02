package org.hisp.dhis.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( value = "/" )
public class IndexController
{
    @RequestMapping( method = RequestMethod.GET )
    @ResponseBody
    public String index()
    {
        return "Nothing to see here yet.";
    }
}
