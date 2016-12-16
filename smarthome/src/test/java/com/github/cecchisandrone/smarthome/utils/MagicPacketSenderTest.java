package com.github.cecchisandrone.smarthome.utils;

import org.junit.Ignore;
import org.junit.Test;

public class MagicPacketSenderTest {

	@Test
	@Ignore
	public void testSendStringString() throws Exception {
		MagicPacketSender.send("00:0c:6e:bb:77:83", "192.168.1.255");
	}

}
