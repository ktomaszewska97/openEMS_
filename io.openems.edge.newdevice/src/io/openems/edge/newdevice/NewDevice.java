package io.openems.edge.newdevice;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.metatype.annotations.Designate;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.PersistencePriority;
import io.openems.common.channel.Unit;
import io.openems.common.exceptions.OpenemsException;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.DoubleWriteChannel;
import io.openems.edge.common.component.OpenemsComponent;

public interface NewDevice extends OpenemsComponent {

public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
		
		ARRAY_VOLTAGE(Doc.of(OpenemsType.DOUBLE)
				.unit(Unit.VOLT)
				.accessMode(AccessMode.READ_WRITE) 
				.persistencePriority(PersistencePriority.HIGH)),
		LOAD_POWER(Doc.of(OpenemsType.DOUBLE)
				.unit(Unit.VOLT)
				.accessMode(AccessMode.READ_WRITE) 
				.persistencePriority(PersistencePriority.HIGH)),
		;

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
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

	public Config getConfig();


}
