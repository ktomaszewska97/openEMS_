package io.openems.edge.smartsolarbox;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.channel.BooleanWriteChannel;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.controller.api.Controller;
import io.openems.common.channel.AccessMode;
import io.openems.common.channel.PersistencePriority;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.DoubleWriteChannel;
import io.openems.edge.common.channel.StringDoc;
import io.openems.edge.common.channel.StringWriteChannel;
import io.openems.edge.common.component.OpenemsComponent;

public interface SmartSolarBox extends OpenemsComponent {

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
		SOLAR_AMPS(Doc.of(OpenemsType.DOUBLE).unit(Unit.AMPERE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		SOLAR_VOLTS(Doc.of(OpenemsType.DOUBLE).unit(Unit.VOLT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		SOLAR_POWER(Doc.of(OpenemsType.DOUBLE).unit(Unit.WATT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		BATTERY_AMPS(Doc.of(OpenemsType.DOUBLE).unit(Unit.AMPERE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		BATTERY_VOLTS(Doc.of(OpenemsType.DOUBLE).unit(Unit.VOLT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		BATTERY_SOC(Doc.of(OpenemsType.DOUBLE).unit(Unit.PERCENT).accessMode(AccessMode.READ_WRITE).onInit(channel -> { //
			// on each Write to the channel -> set the value
			((DoubleWriteChannel) channel).onSetNextWrite(value -> {
				channel.setNextValue(value);
			});
		}).persistencePriority(PersistencePriority.HIGH)),
		LOAD_AMPS(Doc.of(OpenemsType.DOUBLE).unit(Unit.AMPERE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		LOAD_VOLTS(Doc.of(OpenemsType.DOUBLE).unit(Unit.VOLT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		LOAD_POWER(Doc.of(OpenemsType.DOUBLE).unit(Unit.WATT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		GEN_TOTAL(Doc.of(OpenemsType.DOUBLE).unit(Unit.KILOWATT_HOURS).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		CONSUMP_TOTAL(Doc.of(OpenemsType.DOUBLE).unit(Unit.KILOWATT_HOURS).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		BOX_TEMPERATURE(Doc.of(OpenemsType.DOUBLE).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		TIMESTAMP(Doc.of(OpenemsType.DOUBLE).unit(Unit.NONE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		BUTTON_VALUES(new StringDoc() //
				.unit(Unit.NONE) //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(channel -> { //
					// on each Write to the channel -> set the value
					((StringWriteChannel) channel).onSetNextWrite(value -> {
						SmartSolarBoxImpl newdevice = (SmartSolarBoxImpl) channel.getComponent();
						newdevice.receiveChannelValues(value);
						channel.setNextValue(value);
					});
				})),
		SWITCH_STATUS(Doc.of(OpenemsType.BOOLEAN).unit(Unit.NONE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),;

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
	}

	public default DoubleWriteChannel getSolarAmpsChannel() {
		return this.channel(ChannelId.SOLAR_AMPS);
	}

	public default void _setSolarAmps(double value) {
		this.getSolarAmpsChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getSolarVoltsChannel() {
		return this.channel(ChannelId.SOLAR_VOLTS);
	}

	public default void _setSolarVolts(double value) {
		this.getSolarVoltsChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getSolarPowerChannel() {
		return this.channel(ChannelId.SOLAR_POWER);
	}

	public default void _setSolarPower(double value) {
		this.getSolarPowerChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getBatteryAmpsChannel() {
		return this.channel(ChannelId.BATTERY_AMPS);
	}

	public default void _setBatteryAmps(double value) {
		this.getBatteryAmpsChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getBatteryVoltsChannel() {
		return this.channel(ChannelId.BATTERY_VOLTS);
	}

	public default void _setBatteryVolts(double value) {
		this.getBatteryVoltsChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getBatterySoCChannel() {
		return this.channel(ChannelId.BATTERY_SOC);
	}

	public default void _setBatterySoC(double value) {
		this.getBatterySoCChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getLoadAmpsChannel() {
		return this.channel(ChannelId.LOAD_AMPS);
	}

	public default void _setLoadAmps(double value) {
		this.getLoadAmpsChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getLoadVoltsChannel() {
		return this.channel(ChannelId.LOAD_VOLTS);
	}

	public default void _setLoadVolts(double value) {
		this.getLoadVoltsChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getLoadPowerChannel() {
		return this.channel(ChannelId.LOAD_POWER);
	}

	public default void _setLoadPower(double value) {
		this.getLoadPowerChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getGenTotalChannel() {
		return this.channel(ChannelId.GEN_TOTAL);
	}

	public default void _setGenTotal(double value) {
		this.getGenTotalChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getComsumpTotalChannel() {
		return this.channel(ChannelId.CONSUMP_TOTAL);
	}

	public default void _setConsumpTotal(double value) {
		this.getComsumpTotalChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getBoxTemperatureChannel() {
		return this.channel(ChannelId.BOX_TEMPERATURE);
	}

	public default void _setBoxTemperature(double value) {
		this.getBoxTemperatureChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getTimestampChannel() {
		return this.channel(ChannelId.TIMESTAMP);
	}

	public default void _setTimestamp(double value) {
		this.getTimestampChannel().setNextValue(value);
	}

	public default StringWriteChannel getButtonValuesChannel() {
		return this.channel(ChannelId.BUTTON_VALUES);
	}

	public default void _setButtonValues(String value) {
		this.getButtonValuesChannel().setNextValue(value);
	}
	
	public default BooleanWriteChannel getSwitchStatusChannel() {
		return this.channel(ChannelId.SWITCH_STATUS);
	}

	public default void _setSwitchStatus(Boolean value) {
		this.getSwitchStatusChannel().setNextValue(value);
	}

	public Config getConfig();

}
