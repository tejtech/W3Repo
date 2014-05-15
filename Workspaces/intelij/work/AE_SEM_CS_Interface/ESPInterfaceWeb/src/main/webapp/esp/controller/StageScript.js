//StageScript mvc gui controller defined here
//Here we link all the stoes,models,views associate to stagescript ui
//Controller will manipulate Stagescript ui naviagtion and handling event across the panel

//var varIssuerCellEditing = Ext.create('Ext.grid.plugin.CellEditing', {clicksToEdit: 2});

Ext.define('App.controller.StageScript', 
{
    extend	: 'Ext.app.Controller',
	
	id		: 'idStageScriptController',
	
	stores	: ['Scope','Product','ProductPart','Application','BusinessFunction'],
	
	models	: ['Scope','Product','ProductPart','Application','BusinessFunction'],
	
	views	: 
	[
        'stagescript.Search',
		'stagescript.Request',		
    ],
	
	refs: 
	[
		{ref: 'stageScriptSearch'	, selector: 'stagescriptsearch'},	
		{ref: 'stageScriptRequest'	, selector: 'stagescriptrequest'},	        
    ],
	
	requires: 
	[
			
		'Ext.form.Panel',       
	],
	
	//###################################################################################
	/////////////////// Initializing Controller /////////////////////////////////////////
    init: function() 
	{
        this.control(
		{

	
			/////////////////Start: Search Panel Actions /////////////////////////////////			
            'stagescriptsearch > toolbar button[action=submit]':
            {
                 click: this.searchpanelSubmit
            },	
			'stagescriptsearch > toolbar button[action=search]':
            {
                 click: this.searchpanelSearch
            },	
            'stagescriptsearch > toolbar button[action=reset]':
            {
                click: this.searchpanelReset
            },
            'stagescriptsearch  > toolbar button[action=clear]':
            {
                click: this.searchpanelClear
            },
            //--------------            
			'stagescriptsearch #idSScope':
            {
                select: this.onScopeComboSelect
            },
			'stagescriptsearch #idSProduct':
            {
                select: this.onProductComboSelect
            },
			'stagescriptsearch #idSProductPart':
            {
                select: this.onProductPartComboSelect
            },
			'stagescriptsearch #idSApplication':
            {
                select: this.onApplicationComboSelect				
            },	
            /////////////////End: Search Panel Actions /////////////////////////////////
			/////////////////Start: Request Panel Actions /////////////////////////////////			
            'stagescriptrequest > toolbar button[action=submit]':
            {
                 click: this.requestpanelSubmit
            },	
			'stagescriptrequest > toolbar button[action=search]':
            {
                 click: this.requestpanelSearch
            },	
            'stagescriptrequest > toolbar button[action=reset]':
            {
                click: this.requestpanelReset
            },
            'stagescriptrequest  > toolbar button[action=clear]':
            {
                click: this.requestpanelClear
            },
            //--------------           
			
			'stagescriptrequest #idRBusinessFunction':
            {
                select: this.onBusinessFunctionComboSelect
            },	
            /////////////////End: Request Panel Actions /////////////////////////////////
        });				
		
    },
	//###################################################################################
	/////////////////// Implementing Action Functions ///////////////////////////////////

	onLaunch: function()
    {
        console.log('onLaunch.... ');

		this.getScopeStore().getProxy().extraParams={limit:1000};
        this.getScopeStore().load();
		
         //this.getStageScriptStore().load();     //will be loaded by search button


    },
    onPanelRendered: function() 
	{
        console.log('The panel was rendered '+new Date().getTime());
    },
	/////////////////Start Grid Panel Actions ///////////////////////////////////
	//---------start : Grid Actions---------------
	// please use this section to setup the application panel features //
	
	beforeGridRender: function() 
	{		
		console.log('beforeGridRender ');
		//Ext.ComponentQuery.query("issuerlist > toolbar")[0].hide();
		//Ext.ComponentQuery.query("issuerlist actioncolumn")[0].hide();		
		
	},
	//---------end : Grid Actions-----------------------
	/////////////////Start: Search Panel Actions /////////////////////////////////	
    onScopeComboSelect: function(combo, records,eOpts )
    {
		console.log('onSelect Length.... '+records.length);
        console.log('onScopeComboSelect.... '+records[0].get('id'));
        this.getProductStore().getProxy().extraParams={limit:1000,scopeId: records[0].get('id')};
        this.getProductStore().load();
        Ext.ComponentQuery.query("#idSProduct")[0].enable();        
    },
	onProductComboSelect: function(combo, records,eOpts )
    {
		console.log('onSelect Length.... '+records.length);
        console.log('onScopeComboSelect.... '+records[0].get('id'));
        this.getProductPartStore().getProxy().extraParams={limit:1000,productId: records[0].get('id')};
        this.getProductPartStore().load();
        Ext.ComponentQuery.query("#idSProductPart")[0].enable();
    },
	onProductPartComboSelect: function(combo, records,eOpts )
    {
		console.log('onSelect Length.... '+records.length);
        console.log('onScopeComboSelect.... '+records[0].get('id'));
        this.getApplicationStore().getProxy().extraParams={limit:1000,productPartId: records[0].get('id')};
        this.getApplicationStore().load();
        Ext.ComponentQuery.query("#idSApplication")[0].enable();
    },
	onApplicationComboSelect: function(combo, records,eOpts )
    {
		console.log('onSelect Length.... '+records.length);
        console.log('onScopeComboSelect.... '+records[0].get('id'));        
    },
	
	//=================================   
	
	searchpanelSubmit: function()
	{
		console.log('Clicked searchpanelClearSubmit....');	
			
	},
	searchpanelSearch: function()
	{
		console.log('Clicked searchpanelSearch....');
		
		var fp = this.getStageScriptSearch();
		var store = this.getBusinessFunctionStore();
		if(fp.getForm().isValid())
		{				
			this.loadData(fp,store);			
			this.getStageScriptRequest().show();
		}	
	},
	searchpanelReset: function()
	{
		console.log('Clicked searchpanelReset....');
		var fp = this.getStageScriptSearch();		
		fp.getForm().reset();
		var fp = this.getStageScriptRequest();		
		fp.getForm().reset();
		//this.getStageScriptList().hide();
		this.getStageScriptRequest().hide();
	},    	

	searchpanelClear: function()
	{
		console.log('Clicked searchpanelClear....');
		var form = this.getStageScriptSearch().getForm();
		form.reset();
		var sb = this.getStageScriptSearch().getStatusbar();
		// once completed
		sb.clearStatus();
	},
    	
	
    /////////////////End: Search Panel Actions /////////////////////////////////	
	/////////////////Start: Request Panel Actions /////////////////////////////////
	
    onBusinessFunctionComboSelect: function(combo, records,eOpts )
    {
        console.log('onSelect Length.... '+records.length);
		console.log('onBusinessFunctionComboSelect.... '+records[0].get('id'));        
    },
	
	//=================================   
	
	requestpanelSubmit: function()
	{
		console.log('Clicked requestpanelClearSubmit....');	
		var fpParent = this.getStageScriptSearch();
		var fp = this.getStageScriptRequest();		
		if(fp.getForm().isValid())
		{				
			this.requestData(fp,fpParent);
		}
			
	},
	requestpanelSearch: function()
	{
		console.log('Clicked requestpanelSearch....');
				
	},
	requestpanelReset: function()
	{
		console.log('Clicked requestpanelReset....');
		var fp = this.getStageScriptRequest();		
		fp.getForm().reset();
		this.getStageScriptRequest().hide();
	},    	

	requestpanelClear: function()
	{
		console.log('Clicked requestpanelClear....');
		var form = this.getStageScriptSearch().getForm();
		form.reset();
		var sb = this.getStageScriptSearch().getStatusbar();
		// once completed
		sb.clearStatus();
	},    	
	
    /////////////////End: Search Panel Actions /////////////////////////////////
		
	
	//###################################################################################

    	setActiveRecord: function(record)
    	{
            this.getStageScriptEdit().activeRecord = record;
            if (record)
    		{                
    			this.getStageScriptEdit().down('#idBtnSave').enable();
                this.getStageScriptEdit().getForm().loadRecord(record);
            }
    		else
    		{                
    			this.getStageScriptEdit().down('#idBtnSave').disable();
                this.getStageScriptEdit().getForm().reset();
            }
        },
		
		loadData: function(formpanel,store)
    	{
            formpanel.getEl().mask();
			
			//getForm() retrieves the Ext.basic.Form (from Ext.panel.Form)
			var params = formpanel.getForm().getValues();	
			//Write over
			store.getProxy().extraParams = params;
			//load
			store.load();
			
			formpanel.getEl().unmask();
			
        },
		
		requestSummary: function(formpanel,status)
    	{            
			console.log('Clicked request summary... ');

			//Ext.getBody().mask('Triggering the scheduler task...');
			var mergedParams = formpanel.getValues();			

			Ext.Ajax.request(
			{
				url: urlScriptableAppSummary,
				method: 'GET',                            
				params:mergedParams, 
				success : function(response, opts)
				{
					response = Ext.decode(response.responseText);
					if(response.success==true)
					{									
						InfoMsgBox("Info",response.message);
						
						status=true;
					}
					else
					{									
						ErrorMsgBox("Error",response.message);	
						status=false;						
					}
				},
				failure:function(response, opts)
				{							  
				  ErrorMsgBox("Error","Exception is occured: "+response.message);			
				}
			});
		
        },
		
		
		requestData: function(formpanel,formpanelParent)
    	{            
			console.log('Clicked request submit... ');

			Ext.Msg.confirm
			(
				'Confirm',
				'Are you sure you want to Submit Stage Script Request ?',
				function (btn)
				{
					if (btn == 'yes')
					{
                        //Ext.getBody().mask('Triggering the scheduler task...');
                        var mergedParams = formpanel.getValues();
                        Ext.apply(mergedParams, formpanelParent.getValues());

                        Ext.Ajax.request(
                        {
                            url: urlStageScriptRequest,
                            method: 'GET',                            
                            params:mergedParams, 
                            success : function(response, opts)
							{
								response = Ext.decode(response.responseText);
								if(response.success==true)
								{									
									InfoMsgBox("Info",response.message);
								}
								else
								{									
									ErrorMsgBox("Error",response.message);									
								}
							},
							failure:function(response, opts)
						    {							  
							  ErrorMsgBox("Error","Exception is occured: "+response.message);			
						    }
						});
					}
				}
			);
		
        },

    	//###################################################################################

});

 function InfoMsgBox(title,message)
 {
   Ext.MessageBox.show({ 
	  title: title,
      msg: message ,      
      icon: Ext.MessageBox.INFO,
	  buttons: Ext.MessageBox.OK,
     });
 };
 
function ErrorMsgBox(title,message)
 {
   Ext.MessageBox.show({
		title: title,
		msg: message,
		icon: Ext.MessageBox.ERROR,
		buttons: Ext.Msg.OK,
	});
};


//http://stackoverflow.com/questions/6240053/how-can-i-send-values-from-multiple-forms-in-an-ext-ajax-request
/* 1.
var values = this.form1.getForm().getFieldValues();
Ext.apply(values, this.form2.getForm().getFieldValues());
Ext.apply(values, this.form3.getForm().getFieldValues());

Ext.Ajax.request({
	url: 'save.php',
	params: values
}); 
*/					
/* 2.
var forme1 = this.form1.getForm().getFieldValues();
var forme2 = this.form2.getForm().getFieldValues();
var forme3 = this.form3.getForm().getFieldValues();
var forme  = Ext.Object.merge(forme1,forme2,forme3);

Ext.Ajax.request(
	{
		url: 'save.php',
		params: forme
	}); 
*/						
/* 3.
Ext.Ajax.request({
url: 'SomeUrl',
params: Ext.encode({
	formData1: myForm1.getValues(),
	formData2: myForm2.getValues(),
	formData3: myForm3.getValues(),
}),
method: 'POST'
...
}) 
*/