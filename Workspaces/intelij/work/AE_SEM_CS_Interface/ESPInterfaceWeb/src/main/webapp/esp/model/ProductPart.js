Ext.define('App.model.ProductPart', 
{
	extend	: 'Ext.data.Model',
	id		: 'idProductPartModel',
	fields	: 
	[				
		{name: 'id'		, mapping: 'id'	, type: 'int'	, useNull: true},
		{name: 'name'		, mapping: 'name'},			
	]
	
});	
