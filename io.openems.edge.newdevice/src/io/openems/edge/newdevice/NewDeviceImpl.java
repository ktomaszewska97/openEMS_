package io.openems.edge.newdevice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "NewDevice", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = { //
				EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE //
		} //
)
public class NewDeviceImpl extends AbstractOpenemsComponent implements NewDevice, OpenemsComponent, EventHandler {

	private Config config = null;
	private static final String USER_AGENT = "Mozilla/5.0";
	private int counter = 0;

	public NewDeviceImpl() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				NewDevice.ChannelId.values() //
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
	public void handleEvent(Event event) {
		if (!this.isEnabled()) {
			return;
		}
		switch (event.getTopic()) {
		// Triggered every second
		case EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE:
			counter++;
			if (counter == 10) {
				counter = 0;
				try {
					this.sendGet();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					if (this.config.logsEnabled()) {
						Logger logger = new Logger();
						try {
							logger.logError(e);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					e.printStackTrace();
				}
			}

			break;
		}
	}

	@Override
	public String debugLog() {
		return "Channel Values_:" + this.getLoadPowerChannel().value() + this.getArrayVoltageChannel().value();
	}

	@Override
	public Config getConfig() {
		return this.config;
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

	public void resetChannelValues() {

	}

}
