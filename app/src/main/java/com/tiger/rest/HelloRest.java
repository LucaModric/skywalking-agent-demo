/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/22 23:41
 */
package com.tiger.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRest {

    @RequestMapping("/test")
    public String test(){
        return "hello world!";
    }

}
