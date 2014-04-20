package it.cecchi.smarthome.service.raspsonar;

import org.springframework.stereotype.Service;

@Service
public interface RaspsonarService {

	public Double getDistance(boolean resetAverageDistance) throws RaspsonarServiceException;

	public void toggleRelay(boolean status) throws RaspsonarServiceException;

	public void checkDistanceTask();
}
