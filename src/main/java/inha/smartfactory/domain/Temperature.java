package inha.smartfactory.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Temperature {
    @Id
    private int seqNum;
    private String partNo;
    private Double innerTemp;
    private Double outerTemp;
    private String date;

    @Override
    public String toString() {
        return "TemperatureInfo{" +
                "seqNum=" + seqNum +
                ", partNo='" + partNo + '\'' +
                ", innerTemp=" + innerTemp +
                ", outerTemp=" + outerTemp +
                ", date='" + date + '\'' +
                '}';
    }
}
