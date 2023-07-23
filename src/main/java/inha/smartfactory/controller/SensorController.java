package inha.smartfactory.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;

@Controller
public class SensorController {
    @GetMapping("hello")
    public String hello(Model model){
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if(ip == null){
            ip = req.getRemoteAddr();
        }
        System.out.println("ip = " + ip);
        model.addAttribute("data", ip);

        return "hello";
    }
}
