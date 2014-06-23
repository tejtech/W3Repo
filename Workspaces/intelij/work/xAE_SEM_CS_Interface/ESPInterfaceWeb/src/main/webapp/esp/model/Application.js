Ext.define('App.model.Application', 
{
	extend	: 'Ext.data.Model',
	id		: 'idApplicationModel',
	fields	: 
	[				
		{name: 'id'		, mapping: 'id'	, type: 'int'	, useNull: true},
		{name: 'name'		, mapping: 'name'},			
	]
	
});	
