package inha.smartfactory.controller;

import inha.smartfactory.domain.Temperature;
import inha.smartfactory.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;



@Controller
public class SensorController {

    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @RequestMapping(value = "/sendData", method = RequestMethod.POST)
    public String getData(@RequestBody Map<String, Object> postData){
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        postData.put("Date",dateFormat.format(date));
        postData.forEach((key, value) -> sb.append(key).append(" : ").append(value).append("\n"));
        Temperature temp;
        this.sensorService.create(postData);
        return "hello";
    }

    @GetMapping("/getData")
    public String list(Model model){
        List<Temperature> temperatureInfos = this.sensorService.getList();
        model.addAttribute("temperatureList", temperatureInfos);
        return "temp_list";
    }


    @RequestMapping(value = "/controlAC", method = RequestMethod.GET)
    public String changeLED()  {
        this.sensorService.ControlAC();
        return "hello";
    }
}
