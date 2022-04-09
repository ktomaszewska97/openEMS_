package io.openems.edge.controller.newdevicecontroller;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.PersistencePriority;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.BooleanWriteChannel;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.DoubleWriteChannel;
import io.openems.edge.common.channel.StringDoc;
import io.openems.edge.common.channel.StringWriteChannel;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.controller.api.Controller;

public interface NewDeviceController extends Controller, OpenemsComponent {

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
		BUTTON_VALUES(new StringDoc() //
				.unit(Unit.NONE) //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(channel -> { //
					// on each Write to the channel -> set the value
					((StringWriteChannel) channel).onSetNextWrite(value -> {
						NewDeviceControllerImpl newdevice = (NewDeviceControllerImpl) channel.getComponent();
						newdevice.receiveChannelValues(value);
						channel.setNextValue(value);
					});
				})),
		SWITCH_STATE(Doc.of(OpenemsType.BOOLEAN).unit(Unit.NONE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		IS_ACTIVE(Doc.of(OpenemsType.BOOLEAN).unit(Unit.NONE).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		ARRAY_VOLTAGE(Doc.of(OpenemsType.DOUBLE).unit(Unit.VOLT).accessMode(AccessMode.READ_WRITE)
				.persistencePriority(PersistencePriority.HIGH)),
		LOAD_POWER(Doc.of(OpenemsType.DOUBLE).unit(Unit.VOLT).accessMode(AccessMode.READ_WRITE)
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

	public default StringWriteChannel getButtonValues() {
		return this.channel(ChannelId.BUTTON_VALUES);
	}

	public default void _setButtonValues(String value) {
		this.getButtonValues().setNextValue(value);
	}

	public default BooleanWriteChannel getIsTriggeredChannel() {
		return this.channel(ChannelId.SWITCH_STATE);
	}

	public default void _setIsTriggered(Boolean value) {
		this.getIsTriggeredChannel().setNextValue(value);
	}

	public default BooleanWriteChannel getIsActiveChannel() {
		return this.channel(ChannelId.IS_ACTIVE);
	}

	public default void _setIsActive(Boolean value) {
		this.getIsActiveChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getArrayVoltageChannel() {
		return this.channel(ChannelId.ARRAY_VOLTAGE);
	}

	public default void _setArrayVoltage(double value) {
		this.getArrayVoltageChannel().setNextValue(value);
	}

	public default DoubleWriteChannel getLoadPowerChannel() {
		return this.channel(ChannelId.LOAD_POWER);
	}

	public default void _setLoadPower(double value) {
		this.getLoadPowerChannel().setNextValue(value);
	}

}
