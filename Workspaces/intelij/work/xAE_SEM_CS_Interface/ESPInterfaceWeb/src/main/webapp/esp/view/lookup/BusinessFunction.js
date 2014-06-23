Ext.define('App.view.lookup.BusinessFunction' ,	
{
	extend	: 'Ext.form.field.ComboBox',
    alias 	: 'widget.businessfunctionlookup',
	
	requires: ['Ext.toolbar.Toolbar'],
	
	store			: 'BusinessFunction',
    width			:  50,
	//height			:  50,
	//multiSelect :true,
	//pageSize:100,
	
	//remoteSort:true,
	//remoteFilter:true,
	//remoteGroup:true,
	
	
    displayField	: 'name',
    valueField		: 'id',
    //typeAhead		: true,
    triggerAction	: 'all',
    selectOnTab		: true,
    //fieldLabel		: 'Choose State',
    //emptyText 		: 'Choose One',
    queryMode		: 'local',//'remote',//
    forceSelection	: true,
    editable		: false,

    lazyRender		: true,
    listClass		: 'x-combo-list-small',
	
	listeners: 
	{		
		beforerender: function(combo, eOpts )
		{
			console.log('beforerender.....');	
			//combo.getStore().setPageSize(20);		
		},
		beforeselect : function(combo, record, index,eOpts )
		{			
			console.log('Before Select.....');			
		},		
		focus: function(combo, The,eOpts )	
		{
			console.log('on Focus Length.... '+this.getStore().getTotalCount()); 
			if(this.getStore().getTotalCount()<1)
			{
				this.blankText='No data to display';
			}
		},	
	},

    initComponent: function()
    {
        this.callParent(arguments);
    },

});
