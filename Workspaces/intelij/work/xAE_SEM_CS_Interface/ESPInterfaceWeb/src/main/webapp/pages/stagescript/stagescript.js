Ext.application(
{
	//requires: 'Ext.container.Viewport',
	
    name: 'App',

    appFolder: 'esp',
	
	// All the paths for custom classes
	paths: 
	{
		'Ext.ux': 'lib/extjs/ux'
	},
	
	
	// automatically create an instance of esp.view.Viewport
	//autoCreateViewport: true,

	controllers: 
	[
		'StageScript'
	],
	
	
	launch: function() 
	{
		Ext.create('Ext.form.Panel', 
		{
			items: 
			[	
				{xtype: 'stagescriptsearch'},
				{xtype: 'stagescriptrequest'},
            ]			
		});
	}
	
});
