package com.github.cecchisandrone.smarthome.utils;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class ZoneMinderUtilsTest {

	@Test
	@Ignore
	public void testPingHost() throws Exception {
		assertTrue(ZoneMinderUtils.pingHost("ubuntu-zm"));
	}

	@Test
	@Ignore
	public void testShutdownViaSsh() throws IOException {
		ZoneMinderUtils.shutdownZmHost("ubuntu-zm", "username", "password");
	}
}
