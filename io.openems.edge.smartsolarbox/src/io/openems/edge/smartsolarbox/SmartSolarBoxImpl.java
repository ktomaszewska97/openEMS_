package io.openems.edge.smartsolarbox;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.controller.api.Controller;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "Controller.SmartSolarBox", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE //
)
public class SmartSolarBoxImpl extends AbstractOpenemsComponent implements SmartSolarBox, Controller, OpenemsComponent {

	private Config config = null;
	private static final String USER_AGENT = "Mozilla/5.0";
	private int counter = 0;
	private boolean previous_state;

	public SmartSolarBoxImpl() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Controller.ChannelId.values(), //
				SmartSolarBox.ChannelId.values() //
		);
	}

	@Activate
	void activate(ComponentContext context, Config config) {
		super.activate(context, config.id(), config.alias(), config.enabled());
		this.config = config;
		this.resetValues();
		previous_state = false;
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
			try {
				logger.logError(e1);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (this.getSwitchStatusChannel().value().get() != previous_state) {
			try {
				this.sendPost();
				// previous_state = getIsTriggeredChannel().value().get();
				// this.resetChargingPlan();
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
				this._setSolarAmps(jo.get("Solar_Amps").getAsDouble());
				this._setSolarVolts(jo.get("Solar_Volts").getAsDouble());
				this._setSolarPower(jo.get("Solar_Power").getAsDouble());
				this._setBatteryAmps(jo.get("Battery_Amps").getAsDouble());
				this._setBatteryVolts(jo.get("Battery_Volts").getAsDouble());
				this._setBatterySoC(jo.get("Battery_SOC").getAsDouble());
				this._setLoadAmps(jo.get("Load_Amps").getAsDouble());
				this._setLoadVolts(jo.get("Load_Volts").getAsDouble());
				this._setLoadPower(jo.get("Load_Power").getAsDouble());
				this._setGenTotal(jo.get("Gen_Total").getAsDouble());
				this._setConsumpTotal(jo.get("Consump_Total").getAsDouble());
				this._setBoxTemperature(jo.get("Box_Temperature").getAsDouble());
				// this._setTimestamp();
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
		con.setDoOutput(true);

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :  " + responseCode);
		System.out.println("POST Response Message : " + con.getResponseMessage());

		if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
			System.out.println("POST IS SENT.");
		} else {
			System.out.println("POST NOT WORKED");
		}

	}

	public void receiveChannelValues(String channelValues) {

		this.resetValues();
		if (JsonParser.parseString(channelValues.toString()).isJsonObject()) {
			JsonObject jo = JsonParser.parseString(channelValues.toString()).getAsJsonObject();
			if (jo.get("switch_status") != null) {
				this._setSwitchStatus(jo.get("switch_status").getAsBoolean());
			}
		}
	}

	private void resetValues() {
		this._setSwitchStatus(false);
	}

	@Override
	public Config getConfig() {
		return this.config;
	}

}
