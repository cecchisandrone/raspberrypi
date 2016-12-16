package com.github.cecchisandrone.smarthome.utils;

import java.io.IOException;
import java.net.InetAddress;

import com.jcabi.ssh.SSHByPassword;
import com.jcabi.ssh.Shell;

public class ZoneMinderUtils {

	public static boolean pingHost(String host) throws IOException {

		InetAddress inet = InetAddress.getByName(host);
		return inet.isReachable(5000);
	}

	public static String shutdownZmHost(String host, String username, String password) throws IOException {
		Shell shell = new SSHByPassword(host, 22, username, password);
		return new Shell.Plain(shell).exec("sudo shutdown -h now");
	}
}
