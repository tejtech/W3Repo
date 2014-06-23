Ext.define('App.view.SearchFooter' ,	
{
	extend	: 'Ext.toolbar.Toolbar',
    alias 	: 'widget.searchfooter',
	
	requires: ['Ext.toolbar.Toolbar'],
	

	items:
	['->',
		{
			id			: 'idSBtnSubmit',
			text		: 'Submit',
			xtype		: 'button',
			iconCls		: 'icon-submit',
			disabled	: false,
			hidden		: true,
			action		: 'submit',
		},	
		{
			id			: 'idSBtnSearch',
			text		: 'Search',
			xtype		: 'button',
			iconCls		: 'icon-search',
			disabled	: false,
			hidden		: false,
			action		: 'search',
		},			
		{
			id			: 'idSBtnReset',
			text		: 'Reset',
			xtype		: 'button',
			iconCls		: 'icon-reset',
			disabled	: false,
			hidden		: false,
			action		: 'reset',			

		},
		{
			id			: 'idSBtnClear',
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