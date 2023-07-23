package inha.smartfactory.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.util.Map;

@Controller
public class SensorController {
    @RequestMapping(value = "/sendData", method = RequestMethod.POST)
    public String getData(@RequestBody Map<String, Object> postData){
        StringBuilder sb = new StringBuilder();
        postData.forEach((key, value) -> sb.append(key + " : " + value));
        System.out.println("sb.toString() = " + sb.toString());
        return "hello";
    }
}
