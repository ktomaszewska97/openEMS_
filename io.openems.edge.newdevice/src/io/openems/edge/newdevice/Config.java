package io.openems.edge.newdevice;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "NewDevice", //
		description = "")

public @interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "newdevice0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;

	@AttributeDefinition(name = "API URL", description = "The URL of the API")
	String ssbUrl() default "http://0.0.0.0:3000/call/1";

	@AttributeDefinition(name = "Print logs into txt", description = "Print all logs in yhe txt file")
	boolean logsEnabled() default false;

	String webconsole_configurationFactory_nameHint() default "Example [{id}]";

}