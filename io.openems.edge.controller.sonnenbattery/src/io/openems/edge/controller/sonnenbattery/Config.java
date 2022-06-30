package io.openems.edge.controller.sonnenbattery;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "Controller SonnenBattery", //
		description = "")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "ctrlio.openems.edge.controller.sonnenbattery0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;
	
	@AttributeDefinition(name = "GET Status parameters", description = "The GET URL of the API")
	String apiUrl() default "http://192.168.21.203:8080/api/v1/status";
	
	@AttributeDefinition(name = "GET Charge request", description = "The GET URL of the API")
	String chargeRequestUrl() default "http://192.168.21.203:8080/api/v1/setpoint/charge/100";
	
	@AttributeDefinition(name = "GET Discharge request", description = "The GET URL of the API")
	String dischargeRequestUrl() default "http://192.168.21.203:8080/api/v1/setpoint/discharge/100";

	@AttributeDefinition(name = "GET Operating mode = 2 (automatic)", description = "The GET URL of the API")
	String apiChangeAutomaticMode() default "http://192.168.21.203:8080/api/setting?EM_OperatingMode=2";
	
	@AttributeDefinition(name = "GET Operating mode = 1 (manual)", description = "The GET URL of the API")
	String apiChangeManualMode() default "http://192.168.21.203:8080/api/setting?EM_OperatingMode=1";
	
	@AttributeDefinition(name = "GET Operating mode", description = "The GET URL of the API")
	String apiGetOperatingMode() default "http://192.168.21.203:8080/api/configuration/EM_OperatingMode";
	
	String webconsole_configurationFactory_nameHint() default "Controller io.openems.edge.controller.sonnenbattery [{id}]";

}