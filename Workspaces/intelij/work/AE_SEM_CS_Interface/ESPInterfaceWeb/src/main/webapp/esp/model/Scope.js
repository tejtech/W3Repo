Ext.define('App.model.Scope', 
{
	extend	: 'Ext.data.Model',
	id		: 'idScopeModel',
	fields	: 
	[				
		{name: 'id'		, mapping: 'id'	, type: 'int'	, useNull: true},
		{name: 'name'		, mapping: 'name'},
	]
	
});	
