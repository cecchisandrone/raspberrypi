package com.github.cecchisandrone.vc.audio;

import java.io.File;
import java.net.URISyntaxException;

public class PlayerTest {

	public static void main(String[] args) throws URISyntaxException {
		Player p = new Player();
		p.play(new File(PlayerTest.class.getResource("/hello.wav").toURI()));
		p.play(new File(PlayerTest.class.getResource("/get_name.wav").toURI()));
	}
}
