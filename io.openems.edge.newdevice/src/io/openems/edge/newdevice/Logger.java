package io.openems.edge.newdevice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {
	
	public void logError(IOException e) throws FileNotFoundException {
		Date date = new Date();
		PrintWriter out = new PrintWriter("errorLogs.txt");
		out.write(date.toString());
		out.write("\n");
		e.printStackTrace(out);
		out.close();
	}

}
