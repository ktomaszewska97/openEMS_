package io.openems.edge.controller.sonnenbattery;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;

public class CSVLogger {
	
	CSVWriter writer;

	
	public void initializeFileWriter() throws IOException {
		String[] header = {"timestamp", "production", "consumption", "SOC"};
		writer = new CSVWriter(new FileWriter("CSVLogs.csv"));
		writer.writeNext(header);
	}
	
	public void writeData(String[] record) throws IOException {
		writer.writeNext(record);
        writer.flush();
        writer.close();
    }


    public String[] createCsvData(String timestamp, String production, String consumption, String SOC) {
        String[] record = {timestamp, production, consumption, SOC};
        return record;
    }
}
