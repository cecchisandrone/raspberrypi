package it.cecchi.smarthome.service.camera;

import it.cecchi.smarthome.domain.CameraConfiguration;

import org.springframework.stereotype.Service;

@Service
public interface CameraService {

	public void toggleAlarm(CameraConfiguration cameraConfiguration) throws CameraServiceException;

	public String getVideoStreamUrl(CameraConfiguration cameraConfiguration) throws CameraServiceException;
}
