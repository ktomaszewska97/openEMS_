package io.openems.edge.controller.sonnenbattery;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.controller.api.Controller;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "Controller.io.openems.edge.controller.sonnenbattery", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE //
)
public class SonnenBatteryImpl extends AbstractOpenemsComponent implements SonnenBattery, Controller, OpenemsComponent {

	private Config config = null;
	private static final String USER_AGENT = "Mozilla/5.0";

	public SonnenBatteryImpl() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Controller.ChannelId.values(), //
				SonnenBattery.ChannelId.values() //
		);
	}

	@Activate
	void activate(ComponentContext context, Config config) {
		super.activate(context, config.id(), config.alias(), config.enabled());
		this.config = config;
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	public void run() throws OpenemsNamedException {
		try {
			this.sendGet();
		} catch (IOException e1) {
			Logger logger = new Logger();
			System.out.println("Get not sent properly.");
			try {
				logger.logError(e1);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Toggle - change the mode, and charge or discharge
		
		if (this.getModeStatus().value().get() == 1) {
			try {
				this.sendChangeModeManual();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (this.getModeStatus().value().get() == 2) {
			try {
				this.sendChangeModeAutomatic();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (this.getModeStatus().value().get() == 1) {
			try {
				this.sendChargeRequest();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (this.getModeStatus().value().get() == 2) {
			try {
				this.sendDischargeRequest();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void sendGet() throws IOException {
		URL obj = new URL(this.config.apiUrl());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();

			if (JsonParser.parseString(response.toString()).isJsonObject()) {
				JsonObject jo = JsonParser.parseString(response.toString()).getAsJsonObject();
				this._setConsumptionW(jo.get("Consumption_W").getAsDouble());
				this._setProductionW(jo.get("Production_W").getAsDouble());
				this._setPacTotalW(jo.get("Pac_total_W").getAsDouble());
				this._setRsoc(jo.get("RSOC").getAsDouble());
				this._setUsoc(jo.get("USOC").getAsDouble());
				this._setFac(jo.get("Fac").getAsDouble());
				this._setUac(jo.get("Uac").getAsDouble());
				this._setUbat(jo.get("Ubat").getAsDouble());
				this._setTimestamp(jo.get("Timestamp").getAsDouble());
				this._setIsSystemInstalled(jo.get("IsSystemInstalled").getAsDouble());
				
			} else {
				System.out.println("Not JsonObject");
				System.out.println("JsonArray:" + JsonParser.parseString(response.toString()).isJsonArray());
			}
		}
	}
	
	private void sendChargeRequest() throws IOException {
		// This URL should be with a parameter
		URL obj = new URL(this.config.chargeRequestUrl());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :  " + responseCode);
		System.out.println("GET Response Message : " + con.getResponseMessage());

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			System.out.println("On command sent.");
		} else {
			System.out.println("GET NOT WORKED in CHARGE");
		}
	}
	
	private void sendDischargeRequest() throws IOException {		
		URL obj = new URL(this.config.dischargeRequestUrl());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :  " + responseCode);
		System.out.println("GET Response Message : " + con.getResponseMessage());

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			System.out.println("On command sent.");
		} else {
			System.out.println("GET NOT WORKED in DISCHARGE");
		}
	}
	
	private void sendChangeModeAutomatic() throws IOException {
		URL obj = new URL(this.config.apiChangeAutomaticMode());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :  " + responseCode);
		System.out.println("POST Response Message : " + con.getResponseMessage());

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			System.out.println("On command sent.");
		} else {
			System.out.println("POST NOT WORKED in CHANGE AUTO MODE");
		}
		
		con.disconnect();

	}
	
	private void sendChangeModeManual() throws IOException {
		URL obj = new URL(this.config.apiChangeManualMode());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :  " + responseCode);
		System.out.println("POST Response Message : " + con.getResponseMessage());

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			System.out.println("On command sent.");
		} else {
			System.out.println("POST NOT WORKED in CHANGE MANUAL MODE");
		}
		
		con.disconnect();

	}

	public void receiveChannelValues(String channelValues) {
		
		if (JsonParser.parseString(channelValues.toString()).isJsonObject()) {
			JsonObject jo = JsonParser.parseString(channelValues.toString()).getAsJsonObject();
			
			System.out.println("\n");
			System.out.println(jo);
			System.out.println("\n");
			
			if (jo.get("modeStatus") != null) {
				this._setModeStatus(jo.get("modeStatus").getAsInt());
			}
			
			if (jo.get("chargeStatus") != null) {
				this._setChargeStatus(jo.get("chargeStatus").getAsInt());
			}
		}
		System.out.println("\n");
		System.out.println("Receive Channel Values " + this.getChannelValues().value());
		System.out.println("\n");
	}

	@Override
	public Config getConfig() {
		return this.config;
	}
}
