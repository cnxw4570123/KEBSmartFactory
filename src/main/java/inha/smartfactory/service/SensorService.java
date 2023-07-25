package inha.smartfactory.service;

import inha.smartfactory.Repository.TemperatureRepository;
import inha.smartfactory.domain.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@Service
public class SensorService {
    private int id = 1;
    private final TemperatureRepository temperatureRepository;
    @Autowired
    public SensorService(TemperatureRepository temperatureRepository){
        this.temperatureRepository = temperatureRepository;
    }
    public List<Temperature> getList(){
        return this.temperatureRepository.findAll();
    }

    public void create(Map<String, Object> data){
        Temperature temp = new Temperature();
        temp.setPartNo((String) data.get("partNo"));
        temp.setInnerTemp((double) data.get("innerTemp"));
        temp.setOuterTemp((double) data.get("outerTemp"));
        temp.setDate((String)data.get("Date"));
        temp.setSeqNum(id++);
        System.out.println(temp);
        this.temperatureRepository.save(temp);
    }

    public void ControlAC(){
        System.out.println("SensorService.ControlAC");
        WebClient webClient = WebClient.create("172.30.1.1/ControlAC");
        String respBody = webClient.get()
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
