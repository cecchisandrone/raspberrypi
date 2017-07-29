package com.github.cecchisandrone.smarthome.service.gate;

import com.github.cecchisandrone.smarthome.domain.GateConfiguration;

public interface GateService {

	void open(GateConfiguration gateConfiguration) throws GateServiceException;

}
