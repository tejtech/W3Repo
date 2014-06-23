var varSearchPanelHeight=300;
var varSearchPanelWidth=800;
var varSearchPanelStatus=true;

Ext.define('App.view.stagescript.Search',
{
    extend: 'Ext.form.Panel',
    alias : 'widget.stagescriptsearch',
	
	requires: 
	[			
		'Ext.form.Panel',
		
        'App.view.SearchFooter',
        'App.view.lookup.Scope',
		'App.view.lookup.Product',
		'App.view.lookup.ProductPart',
		'App.view.lookup.Application',
	],
	

						
	id		: 'idStageScriptSearch',
	renderTo: 'divStageScriptSearch',	
	title	: 'Stage Script Search Criteria',
	hidden	: false,
    activeRecord: null,
    defaultType: 'textfield',
    listClass: 'x-column-header-text',

    waitMsgTarget: true,
    //frame	: true,
	//autoWidth: true,
    width	: (varSearchPanelWidth-30),	
    height	: (varSearchPanelHeight-60),
    bodyPadding: 25,
    fieldDefaults:
    {
        labelAlign: 'left',
        labelWidth: 120,
        anchor: '100%',		
    },
	
	items: 
    [           
        {
            id			: 'idSScope',
            fieldLabel	: '<span style="color:red;font-weight:bold">*</span>Scope',
            blankText   : 'Scope is required',
			emptyText 	: 'Select One',
            xtype		: 'scopelookup',
            name		: 'scopeId',
            allowBlank	: false,
            readOnly	: false,
        },
		{
            id			: 'idSProduct',
            fieldLabel	: '<span style="color:red;font-weight:bold">*</span>Product',
            blankText   : 'Product is required',
			emptyText 	: 'Select One',
            xtype		: 'productlookup',
            name		: 'productId',
            allowBlank	: false,
            readOnly	: false,
			disabled    : true,
        },
		{
            id			: 'idSProductPart',
            fieldLabel	: '<span style="color:red;font-weight:bold">*</span>Business App',
            blankText   : 'Business Application is required',
			emptyText 	: 'Select One',
            xtype		: 'productpartlookup',
            name		: 'productPartId',
            allowBlank	: false,
            readOnly	: false,
			disabled    : true,
        },
		{
            id			: 'idSApplication',
            fieldLabel	: '<span style="color:red;font-weight:bold">*</span>Scriptable App',
            blankText   : 'Scriptable Application is required',
			emptyText 	: 'Select One',
            xtype		: 'applicationlookup',
            name		: 'applicationId',
            allowBlank	: false,
            readOnly	: false,
			disabled    : true,
        },
		{
            id			: 'idSCardId',
            fieldLabel	: '<span style="color:white;font-weight:bold">*</span>CardId',
            blankText   : 'CardId is required',
            name		: 'cardId',
            allowBlank	: true,
            readOnly	: false,
			maxLength	: 32,
			enforceMaxLength: true,
			maskRe		: /[0-9A-Fa-f]/,
			regex		: /^[0-9A-Fa-f]{1,32}$/,
			//maskRe	: /[\w\.]/i,
			//regex		: /^\w{1,3}\.\w{1,3}\.\w{1,3}\.\w{1,3}$/,
			regexText	: 'Must be a hex value',
        },
		{
            id			: 'idSBIN',
            fieldLabel	: '<span style="color:white;font-weight:bold">*</span>BIN',
            blankText   : 'BIN is required',
            name		: 'bin',
			hidden		: true,
            allowBlank	: true,
            readOnly	: false,
			maxLength	: 8,
			enforceMaxLength: true,
			maskRe		: /[\d]/i,
			regex		: /^\d{1,8}$/,
			regexText	: 'Must be a numeric value',
        },
        
    ],
	
    dockedItems:
    [
         {
            id 		: 'idIssuerSearchFooter',
            xtype	: 'searchfooter',
            dock	: 'bottom',
            ui		: 'footer',
            hidden	: false,
        },        
    ],
	
	initComponent: function() 
	{   
        this.callParent(arguments);
    },	
	
});

