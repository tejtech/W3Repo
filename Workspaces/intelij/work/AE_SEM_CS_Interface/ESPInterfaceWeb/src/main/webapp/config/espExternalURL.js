///////////////////////////ENVIRONMENT/////////////////

//var varEnvironment	='LOCAL' 
//var varEnvironment	='DEV_STATIC' //which will call admin console data folder to return json, can be used for testing purpose
var varEnvironment	='DEV_BACKEND'//which will return data from Restful API and used in real enet to end system
//var varEnvironment	='EUAT'
//var varEnvironment	='LIVE'

//------------------------------------------
//Setting up default values here

var serverURL				='';
var contextPath				='';
var bundleName 				= '';
//------------------------------------------
//Country	
var urlCountryData			='';
//Institution	
var urlInstitutionData		='';
//Issuer	
var urlIssuerData			='';
//Scope	
var urlScopeData			='';
//Product	
var urlProductData			='';
//ProductPart	
var urlProductPartData			='';
//Application	
var urlApplicationData			='';
//BusinessFunction
var urlBusinessFunctionData			='';

//ValidScriptableApps
var urlScriptableAppSummary			='';

//StageScriptRequest
var urlStageScriptRequest			='';

///////////////////////////LOCAL ENVIRONMENT/////////////////

if(varEnvironment=='LOCAL')
{
	//////////URLs////////////////////
	//serverURL = 'http://localhost:8080/';
	serverURL='../../../';
	contextPath = 'esp-interface-web';
	bundleName = '';
	//////////////////////////////////////////
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	//Scope	
	urlScopeData 				= serverURL + contextPath + bundleName+ '/data/scope.json';	
	//Product	
	urlProductData 				= serverURL + contextPath + bundleName+ '/data/product.json';	
	//ProductPart	
	urlProductPartData 			= serverURL + contextPath + bundleName+ '/data/productpart.json';	
	//Application	
	urlApplicationData 			= serverURL + contextPath + bundleName+ '/data/application.json';	
	//BusinessFunction	
	urlBusinessFunctionData 	= serverURL + contextPath + bundleName+ '/data/businessfunction.json';		
	//Summary
	urlScriptableAppSummary  	= serverURL + contextPath + bundleName+ '/data/validscriptableapps.json';	
	//StageScriptRequest
	urlStageScriptRequest  		= serverURL + contextPath + bundleName+ '/data/response.json';	
	
	
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@	    

}

if(varEnvironment=='DEV_BACKEND')
{
	//////////URLs////////////////////
	//serverURL = 'http://localhost:8080/';
	serverURL='../../../';
	contextPath = 'esp-interface-web';
	bundleName = '';
	//////////////////////////////////////////
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	//Scope	
	urlScopeData 				= serverURL + contextPath + bundleName+ '/services/api/scope';		
	//Product	
	urlProductData 				= serverURL + contextPath + bundleName+ '/services/api/product';		
	//ProductPart	
	urlProductPartData 			= serverURL + contextPath + bundleName+ '/services/api/productpart';		
	//Application	
	urlApplicationData 			= serverURL + contextPath + bundleName+ '/services/api/application';		
	//BusinessFunction	
	urlBusinessFunctionData 	= serverURL + contextPath + bundleName+ '/services/api/businessfunction';	
	//ScriptableAppSummary
	urlScriptableAppSummary  	= serverURL + contextPath + bundleName+ '/services/api/stagescript/summary';	
	//StageScriptRequest
	urlStageScriptRequest  		= serverURL + contextPath + bundleName+ '/services/api/stagescript/submit';
	
	
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@	    

}

if(varEnvironment=='LIVE')
{
	//////////URLs////////////////////
	//serverURL = 'http://localhost:8080/';
	serverURL='../../../';
	contextPath = 'esp-interface-web';
	bundleName = '';
	//////////////////////////////////////////
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	//Scope	
	urlScopeData 				= serverURL + contextPath + bundleName+ '/services/api/scope';		
	//Product	
	urlProductData 				= serverURL + contextPath + bundleName+ '/services/api/product';		
	//ProductPart	
	urlProductPartData 			= serverURL + contextPath + bundleName+ '/services/api/productpart';		
	//Application	
	urlApplicationData 			= serverURL + contextPath + bundleName+ '/services/api/application';		
	//BusinessFunction	
	urlBusinessFunctionData 	= serverURL + contextPath + bundleName+ '/services/api/businessfunction';	
	//ScriptableAppSummary
	urlScriptableAppSummary  	= serverURL + contextPath + bundleName+ '/services/api/stagescript/summary';	
	//StageScriptRequest
	urlStageScriptRequest  		= serverURL + contextPath + bundleName+ '/services/api/stagescript/submit';
	
	
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@	    

}


