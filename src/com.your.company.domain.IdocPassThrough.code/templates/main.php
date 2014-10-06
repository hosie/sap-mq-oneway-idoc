<?php

	// system properties
	$nameOfProject = $_MB["PATTERN_INSTANCE_NAME"];
	$location = $_MB["WORKSPACE_ROOT"];
	
	// user defined properties
	$jar = $_MB["PP"]['pp7'];
	$dllORso = $_MB["PP"]['pp8'];
	$host = $_MB["PP"]['pp10'];
	$client = $_MB["PP"]['pp14'];
	$csName = $_MB["PP"]['pp9'];
	$gatewayHost = $_MB["PP"]['pp12'];
	$gatewayService = $_MB["PP"]['pp13'];
	$nol = $_MB["PP"]['pp16'];
	$rfcID = $_MB["PP"]['pp11'];
	$systemNumber = $_MB["PP"]['pp15'];
	
	$file = $location . '/' . $nameOfProject . "_IDOCPassThrough_SAPAdapter_Tx1/.settings/com.ibm.adapter.j2ca.conf.xml";
	$SAP_SETUP = '<?xml version="1.0" encoding="UTF-8"?>' . "\n"
	. '<configuration xmlns="http://j2ca.adapter.ibm.com">' . "\n"
	. '<systemEntry uri="' . $jar . '"/>' . "\n"
	. '<systemEntry uri="' . $dllORso . '"/>' . "\n"
	. '</configuration>' . "\n";
	file_put_contents($file, $SAP_SETUP);
	
	$file = $location . '/' . $nameOfProject . "_IdocPassThroughMessageSet/idocpassthrough.inadapter.configurableservice";
	$SAP_SYSTEM = '<?xml version="1.0" encoding="UTF-8"?>' . "\n"
	. '<configurableservice' . "\n"
	. 'RFCTraceLevel="1"' . "\n"
	. 'RFCTraceOn="false"' . "\n"
	. 'RFCTracePath=""' . "\n"
	. 'applicationServerHost="' . $host . '"' . "\n"
	. 'client="' . $client . '"' . "\n"
	. 'csName="' . $csName . '"' . "\n"
	. 'csType="SAPConnection"' . "\n"
	. 'gatewayHost="' . $gatewayHost . '"' . "\n"
	. 'gatewayService="' . $gatewayService . '"' . "\n"
	. 'numberOfListeners="' . $nol . '"' . "\n"
	. 'rfcProgramID="' . $rfcID . '"' . "\n"
	. 'systemNumber="' . $systemNumber . '"/>';
	file_put_contents($file, $SAP_SYSTEM);
?>
