package it.cecchi.smarthome.service.raspsonar;

import org.springframework.stereotype.Service;

@Service
public interface RaspsonarService {

	public Double getDistance() throws RaspsonarServiceException;

	public void checkDistanceTask();
}
