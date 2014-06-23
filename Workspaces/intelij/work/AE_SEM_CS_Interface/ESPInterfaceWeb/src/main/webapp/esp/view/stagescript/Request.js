var varSearchPanelHeight=300;
var varSearchPanelWidth=800;
var varSearchPanelStatus=true;

Ext.define('App.view.stagescript.Request',
{
    extend: 'Ext.form.Panel',
    alias : 'widget.stagescriptrequest',
	
	requires: 
	[			
		'Ext.form.Panel',

		'Ext.ux.form.field.DateTime',
		
        'App.view.RequestFooter',        
		'App.view.lookup.Application',
		'App.view.lookup.BusinessFunction',		
	],
	

						
	id		: 'idStageScriptRequest',
	renderTo: 'divStageScriptRequest',	
	title	: 'Stage Script Request',
	hidden	: true,
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
            id			: 'idRBusinessFunction',
            fieldLabel	: '<span style="color:red;font-weight:bold">*</span>Business Function',
            blankText   : 'Business Function is required',
			emptyText 	: 'Select One',
            xtype		: 'businessfunctionlookup',
            name		: 'businessFunctionId',
            allowBlank	: false,
            readOnly	: false,
			//disabled    : true,
        },	
		
		{
			id:'idROptionalDataSet',
			xtype:'fieldset',
			title: 'Optional Parameters',
			checkboxToggle:true,
			collapsible : true,
			collapsed: true,
			defaultType: 'textfield',
			layout: 'anchor',
			//defaults: {anchor: '80%',value: new Date()},
			defaults: {anchor: '80%',value:'' },
			items :
			[
				{
					id:'idRStageScriptStartDate',
					xtype: 'datefield',
					fieldLabel: 'Start Date',
					name: 'stageScriptStartDate',
					format:'Y-m-d',
					vtype: 'daterange',
					endDateField: 'idRStageScriptEndDate',
					editable :false,					
				},
				{
					id:'idRStageScriptEndDate',
					xtype: 'datefield',
					fieldLabel: 'End Date',
					name: 'stageScriptEndDate',	
					format:'Y-m-d',
					vtype: 'daterange',
					startDateField: 'idRStageScriptStartDate',	
					editable :false,
				},
				{
					id			:'idRMaxRestageCount',
					fieldLabel	: 'Restage Count',
					name		: 'maxRestageCount',
					xtype		: 'numberfield',
					//fieldLabel: 'End',
					allowBlank	: true,
					//value		:'1',
					minValue	: -1,
					maxValue	: 254,
					//flex: 1
				},
			],
			listeners:
			{
				collapse: function(p)
				{
					p.cascade(function ()
					{
						//this.disable();
					});
					//alert('disable');
				},
				expand: function(p)
				{
					p.cascade(function ()
					{
						//this.enable();
					});					
					//alert('enable');
				}
			}

		},		
		
    ],
	
    dockedItems:
    [
         {
            id 		: 'idIssuerRequestFooter',
            xtype	: 'requestfooter',
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

