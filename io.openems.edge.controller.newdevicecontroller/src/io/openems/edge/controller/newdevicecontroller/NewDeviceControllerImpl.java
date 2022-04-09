package io.openems.edge.controller.newdevicecontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.controller.api.Controller;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "Controller.NewDeviceController", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE //
)
public class NewDeviceControllerImpl extends AbstractOpenemsComponent
		implements NewDeviceController, Controller, OpenemsComponent {

	private Config config = null;
	private static final String USER_AGENT = "Mozilla/5.0";
	private int counter = 0;
	private boolean previous_state = false;
 
	public NewDeviceControllerImpl() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Controller.ChannelId.values(), //
				NewDeviceController.ChannelId.values() //
		);
	}

	@Activate
	void activate(ComponentContext context, Config config) {
		super.activate(context, config.id(), config.alias(), config.enabled());
		this.config = config;

		this.resetChargingPlan();
	
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Before send post");
		
		if (this.getIsTriggeredChannel().value().get() != previous_state) {
			try {
				System.out.println("Send post");
				this.sendPost();
				previous_state = getIsTriggeredChannel().value().get();
				//this.resetChargingPlan();
				System.out.println("Value after send post and sleep: " + this.getIsTriggeredChannel().value().get());
				counter++;
				System.out.println("Counter: " + this.counter);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void sendGet() throws IOException {
		URL obj = new URL(this.config.ssbUrl());
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
				this._setArrayVoltage(jo.get("arrayVoltage").getAsDouble());
				this._setLoadPower(jo.get("loadPower").getAsDouble());
				System.out.println(this.getArrayVoltageChannel());
				System.out.println(this.getLoadPowerChannel());
			} else {
				System.out.println("Not JsonObject");
				System.out.println("JsonArray:" + JsonParser.parseString(response.toString()).isJsonArray());
			}
		}
	}

	private void sendPost() throws IOException {
		URL obj = new URL(this.config.ssbUrl());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		// con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :  " + responseCode);
		System.out.println("POST Response Message : " + con.getResponseMessage());

		if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
			
			System.out.println("POST is sent.");

			/*
			 * BufferedReader in = new BufferedReader(new
			 * InputStreamReader(con.getInputStream())); String inputLine; StringBuffer
			 * response = new StringBuffer();
			 * 
			 * while ((inputLine = in.readLine()) != null) { response.append(inputLine); }
			 * in.close(); if (JsonParser.parseString(response.toString()).isJsonObject()) {
			 * JsonObject jo =
			 * JsonParser.parseString(response.toString()).getAsJsonObject();
			 * System.out.println(jo);
			 * this._setArrayVoltage(jo.get("arrayVoltage").getAsDouble());
			 * this._setLoadPower(jo.get("loadPower").getAsDouble());
			 * System.out.println(this.getArrayVoltageChannel());
			 * System.out.println(this.getLoadPowerChannel()); } else {
			 * System.out.println("Not JsonObject"); System.out.println("JsonArray:" +
			 * JsonParser.parseString(response.toString()).isJsonArray()); }
			 * System.out.println(response.toString());
			 */

		} else {
			System.out.println("POST NOT WORKED");
		}

	}

	public void receiveChannelValues(String channelValues) {

		this.resetChargingPlan();
		// System.out.println("Values before receiving: " + channelValues);

		if (JsonParser.parseString(channelValues.toString()).isJsonObject()) {
			JsonObject jo = JsonParser.parseString(channelValues.toString()).getAsJsonObject();
			if(jo.get("isTriggered") != null) {
				this._setIsTriggered(jo.get("isTriggered").getAsBoolean()); 
				}

			// System.out.println("Values after receiving: " +
			// this.getIsTriggeredChannel().value());
			// System.out.println("Values after receiving: " +
			// this.getIsActiveChannel().value());

		}
	}

	private void resetChargingPlan() {
		this._setIsTriggered(false);
		this._setIsActive(false);
	}

}
