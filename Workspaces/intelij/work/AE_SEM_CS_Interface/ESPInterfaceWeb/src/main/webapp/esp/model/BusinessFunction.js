Ext.define('App.model.BusinessFunction', 
{
	extend	: 'Ext.data.Model',
	id		: 'idBusinessFunctionModel',
	fields	: 
	[				
		{name: 'id'		, mapping: 'id'	, type: 'int'	, useNull: true},
		{name: 'name'		, mapping: 'name'},			
	]
	
});	
