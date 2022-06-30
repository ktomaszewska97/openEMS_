package io.openems.edge.controller.sonnenbattery;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.PersistencePriority;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.DoubleWriteChannel;
import io.openems.edge.common.channel.IntegerWriteChannel;
import io.openems.edge.common.channel.StringDoc;
import io.openems.edge.common.channel.StringWriteChannel;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.controller.api.Controller;

public interface SonnenBattery extends Controller, OpenemsComponent {

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
		CONSUMPTION_W(Doc.of(OpenemsType.DOUBLE).unit(Unit.NONE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		PRODUCTION_W(Doc.of(OpenemsType.DOUBLE).unit(Unit.WATT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		GRIDFEEDIN_W(Doc.of(OpenemsType.DOUBLE).unit(Unit.WATT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		PAC_TOTAL_W(Doc.of(OpenemsType.DOUBLE).unit(Unit.WATT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		RSOC(Doc.of(OpenemsType.DOUBLE).unit(Unit.PERCENT).accessMode(AccessMode.READ_WRITE).onInit(channel -> { //
			// on each Write to the channel -> set the value
			((DoubleWriteChannel) channel).onSetNextWrite(value -> {
				channel.setNextValue(value);
			});
		}).persistencePriority(PersistencePriority.HIGH)),
		USOC(Doc.of(OpenemsType.DOUBLE).unit(Unit.PERCENT).accessMode(AccessMode.READ_WRITE).onInit(channel -> { //
			// on each Write to the channel -> set the value
			((DoubleWriteChannel) channel).onSetNextWrite(value -> {
				channel.setNextValue(value);
			});
		}).persistencePriority(PersistencePriority.HIGH)),
		FAC(Doc.of(OpenemsType.DOUBLE).unit(Unit.HERTZ).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		UAC(Doc.of(OpenemsType.DOUBLE).unit(Unit.VOLT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		UBAT(Doc.of(OpenemsType.DOUBLE).unit(Unit.VOLT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		TIMESTAMP(Doc.of(OpenemsType.STRING).unit(Unit.NONE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		ISSYSTEMINSTALLED(Doc.of(OpenemsType.DOUBLE).unit(Unit.NONE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		CHANNEL_VALUES(new StringDoc() //
				.unit(Unit.NONE) //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(channel -> { //
					// on each Write to the channel -> set the value
					((StringWriteChannel) channel).onSetNextWrite(value -> {
						SonnenBatteryImpl newdevice = (SonnenBatteryImpl) channel.getComponent();
						newdevice.receiveChannelValues(value);
						channel.setNextValue(value);
					});
				})),
		//1 - manual, 2 - automatic
		MODE_STATUS(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		CHARGE_STATUS(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		CHARGE_VALUE(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.READ_WRITE)
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

	public default DoubleWriteChannel getConsumptionWChannel() {
		return this.channel(ChannelId.CONSUMPTION_W);
	}

	public default void _setConsumptionW(double value) {
		this.getConsumptionWChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getProductionW() {
		return this.channel(ChannelId.PRODUCTION_W);
	}

	public default void _setProductionW(double value) {
		this.getProductionW().setNextValue(value);
	}

	public default DoubleWriteChannel getGridFeedInW() {
		return this.channel(ChannelId.GRIDFEEDIN_W);
	}

	public default void _setGridFeedInW(double value) {
		this.getGridFeedInW().setNextValue(value);
		System.out.println("Executed Set Grid Feed In W ");
	}

	public default DoubleWriteChannel getPacTotalW() {
		return this.channel(ChannelId.PAC_TOTAL_W);
	}

	public default void _setPacTotalW(double value) {
		this.getPacTotalW().setNextValue(value);
	}

	public default DoubleWriteChannel getRsoc() {
		return this.channel(ChannelId.RSOC);
	}

	public default void _setRsoc(double value) {
		this.getRsoc().setNextValue(value);
	}

	public default DoubleWriteChannel getUsoc() {
		return this.channel(ChannelId.USOC);
	}

	public default void _setUsoc(double value) {
		this.getUsoc().setNextValue(value);
	}

	public default DoubleWriteChannel getFac() {
		return this.channel(ChannelId.FAC);
	}

	public default void _setFac(double value) {
		this.getFac().setNextValue(value);
	}

	public default DoubleWriteChannel getUac() {
		return this.channel(ChannelId.UAC);
	}

	public default void _setUac(double value) {
		this.getUac().setNextValue(value);
	}

	public default DoubleWriteChannel getUbat() {
		return this.channel(ChannelId.UBAT);
	}

	public default void _setUbat(double value) {
		this.getUbat().setNextValue(value);
	}

	public default StringWriteChannel getTimestamp() {
		return this.channel(ChannelId.TIMESTAMP);
	}

	public default void _setTimestamp(String value) {
		this.getTimestamp().setNextValue(value);
	}

	public default DoubleWriteChannel getIsSystemInstalled() {
		return this.channel(ChannelId.ISSYSTEMINSTALLED);
	}

	public default void _setIsSystemInstalled(double value) {
		this.getIsSystemInstalled().setNextValue(value);
	}
	
	public default StringWriteChannel getChannelValues() {
		return this.channel(ChannelId.CHANNEL_VALUES);
	}

	public default void _setChannelValues(String value) {
		this.getChannelValues().setNextValue(value);
	}
	
	public default IntegerWriteChannel getModeStatus() {
		return this.channel(ChannelId.MODE_STATUS);
	}

	public default void _setModeStatus(int value) {
		this.getModeStatus().setNextValue(value);
	}
	
	public default IntegerWriteChannel getChargeStatus() {
		return this.channel(ChannelId.CHARGE_STATUS);
	}

	public default void _setChargeStatus(int value) {
		this.getChargeStatus().setNextValue(value);
	}
	
	public default IntegerWriteChannel getChargeValue() {
		return this.channel(ChannelId.CHARGE_VALUE);
	}

	public default void _setChargeValue(int value) {
		this.getChargeValue().setNextValue(value);
	}

	public Config getConfig();

}
