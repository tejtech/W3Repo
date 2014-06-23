Ext.define('App.model.Product', 
{
	extend	: 'Ext.data.Model',
	id		: 'idProductModel',
	fields	: 
	[				
		{name: 'id'		, mapping: 'id'	, type: 'int'	, useNull: true},
		{name: 'name'		, mapping: 'name'},			
	]
	
});	
