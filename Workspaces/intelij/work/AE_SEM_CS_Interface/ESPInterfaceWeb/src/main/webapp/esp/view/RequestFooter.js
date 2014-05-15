Ext.define('App.view.RequestFooter' ,	
{
	extend	: 'Ext.toolbar.Toolbar',
    alias 	: 'widget.requestfooter',
	
	requires: ['Ext.toolbar.Toolbar'],
	

	items:
	['->',
		{
			id			: 'idRBtnSubmit',
			text		: 'Submit',
			xtype		: 'button',
			iconCls		: 'icon-submit',
			disabled	: false,
			hidden		: false,
			action		: 'submit',
		},	
		{
			id			: 'idRBtnSearch',
			text		: 'Search',
			xtype		: 'button',
			iconCls		: 'icon-search',
			disabled	: false,
			hidden		: true,
			action		: 'search',
		},			
		{
			id			: 'idRBtnReset',
			text		: 'Reset',
			xtype		: 'button',
			iconCls		: 'icon-reset',
			disabled	: false,
			hidden		: false,
			action		: 'reset',			

		},
		{
			id			: 'idRBtnClear',
			text		: 'Clear',
			xtype		: 'button',
			iconCls		: 'icon-clear',
			disabled	: false,
			hidden		: true,
			action		: 'clear',			
		}
	],
	
	initComponent: function() 
	{   
        this.callParent(arguments);
    },									

});