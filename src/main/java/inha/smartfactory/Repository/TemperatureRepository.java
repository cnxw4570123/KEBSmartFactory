package inha.smartfactory.Repository;

import inha.smartfactory.domain.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureRepository extends JpaRepository<Temperature, Integer> {

}
