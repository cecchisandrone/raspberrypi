package com.github.cecchisandrone.smarthome.service.zm;

import org.springframework.stereotype.Service;

import com.github.cecchisandrone.smarthome.domain.ZoneMinderConfiguration;

@Service
public interface ZoneMinderService {

	public void wakeUpZmHost(ZoneMinderConfiguration configuration) throws ZoneMinderServiceException;

	public String shutdownZmHost(ZoneMinderConfiguration configuration) throws ZoneMinderServiceException;
}
