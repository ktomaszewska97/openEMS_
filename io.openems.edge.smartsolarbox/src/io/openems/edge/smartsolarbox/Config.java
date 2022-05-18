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
	
	@AttributeDefinition(name = "GET API URL", description = "The GET URL of the API")
	String apiUrl() default "http://10.3.141.1:8080/api/status";
	
	@AttributeDefinition(name = "TURN ON URL", description = "The ON URL of the API")
	String onUrl() default "http://10.3.141.1:8080/api/switch/on";
	
	@AttributeDefinition(name = "TURN OFF URL", description = "The OFF URL of the API")
	String offUrl() default "http://10.3.141.1:8080/api/switch/off";

	String webconsole_configurationFactory_nameHint() default "Controller io.openems.edge.smartsolarbox [{id}]";

}