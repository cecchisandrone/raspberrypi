package it.cecchi.raspsonar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PythonSonarTester {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		// set up the command and parameter
		String pythonScriptPath = "";
		String[] cmd = new String[2];
		cmd[0] = "python";
		cmd[1] = pythonScriptPath;

		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		
		// retrieve output from python script
		BufferedReader bfr = new BufferedReader(new InputStreamReader(
				pr.getInputStream()));
		String line = "";
		while ((line = bfr.readLine()) != null) {
			// display each output line form python script
			System.out.println(line);
		}
	}
}