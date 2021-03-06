package io.openems.edge.bridge.modbus.api.element;

import io.openems.common.types.OpenemsType;

import java.nio.ByteBuffer;

/**
 * A FloatQuadruplewordElement represents a Float value in an
 * {@link AbstractQuadrupleWordElement}.
 */
public class FloatQuadruplewordElement extends AbstractQuadrupleWordElement<FloatQuadruplewordElement, Double> {

	public FloatQuadruplewordElement(int address) {
		super(OpenemsType.DOUBLE, address);
	}

	@Override
	protected FloatQuadruplewordElement self() {
		return this;
	}

	protected Double fromByteBuffer(ByteBuffer buff) {
		return Double.valueOf(buff.getDouble());
	}

	protected ByteBuffer toByteBuffer(ByteBuffer buff, Double value) {
		return buff.putDouble(value.doubleValue());
	}

}
