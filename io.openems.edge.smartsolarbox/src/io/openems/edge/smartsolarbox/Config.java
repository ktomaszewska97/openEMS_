package io.openems.edge.smartsolarbox;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "Controller SmartSolarBox", //
		description = "")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "ctrlio.openems.edge.smartsolarbox0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;
	
	@AttributeDefinition(name = "API URL", description = "The URL of the API")
	String ssbUrl() default "http://0.0.0.0:3000/smartsolarbox";

	String webconsole_configurationFactory_nameHint() default "Controller io.openems.edge.smartsolarbox [{id}]";

}