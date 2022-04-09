package io.openems.edge.controller.newdevicecontroller;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "Controller New Device", //
		description = "")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "ctrlio.openems.edge.controller.newdevicecontroller0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;
	
	@AttributeDefinition(name = "API URL", description = "The URL of the API")
	String ssbUrl() default "http://0.0.0.0:3000/call/1";

	String webconsole_configurationFactory_nameHint() default "Controller io.openems.edge.controller.newdevicecontroller [{id}]";

}