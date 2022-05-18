package io.openems.edge.controller.sonnenbattery;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "Controller io.openems.edge.controller.sonnenbattery", //
		description = "")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "ctrlio.openems.edge.controller.sonnenbattery0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;
	
	@AttributeDefinition(name = "GET API URL", description = "The GET URL of the API")
	String apiUrl() default "http://10.3.141.1:8080/api/vi/status";
	
	@AttributeDefinition(name = "GET API URL", description = "The GET URL of the API")
	String chargeRequestUrl() default "http://192.168.33.185:8080/api/v1/setpoint/charge/100";
	
	@AttributeDefinition(name = "GET API URL", description = "The GET URL of the API")
	String dischargeRequestUrl() default "http://192.168.33.185:8080/api/v1/setpoint/discharge/100";

	@AttributeDefinition(name = "GET API URL", description = "The GET URL of the API")
	String apiChangeAutomaticMode() default "http://192.168.33.185:8080/api/setting?EM_OperatingMode=2";
	
	@AttributeDefinition(name = "GET API URL", description = "The GET URL of the API")
	String apiChangeManualMode() default "http://192.168.33.185:8080/api/setting?EM_OperatingMode=1";
	
	String webconsole_configurationFactory_nameHint() default "Controller io.openems.edge.controller.sonnenbattery [{id}]";

}