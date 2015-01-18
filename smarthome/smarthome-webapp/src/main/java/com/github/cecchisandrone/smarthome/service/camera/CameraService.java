package com.github.cecchisandrone.smarthome.service.camera;

import org.springframework.stereotype.Service;

import com.github.cecchisandrone.smarthome.domain.CameraConfiguration;

@Service
public interface CameraService {

	public void toggleAlarm(CameraConfiguration cameraConfiguration) throws CameraServiceException;

	public String getVideoStreamUrl(CameraConfiguration cameraConfiguration) throws CameraServiceException;
}
