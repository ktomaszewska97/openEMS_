package io.openems.edge.core.componentmanager;

import org.junit.Test;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.test.ComponentTest;
import io.openems.edge.common.test.DummyConfigurationAdmin;

public class ComponentManagerImplTest {

	@Test
	public void test() throws OpenemsException, Exception {
		final DummyConfigurationAdmin cm = new DummyConfigurationAdmin();
		cm.getOrCreateEmptyConfiguration(ComponentManager.SINGLETON_SERVICE_PID);
		new ComponentTest(new ComponentManagerImpl()) //
				.addReference("cm", cm) //
				.activate(MyConfig.create() //
						.build());
	}

}
