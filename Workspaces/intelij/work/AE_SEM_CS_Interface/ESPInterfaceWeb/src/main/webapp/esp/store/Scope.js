var varScopeMask = new Ext.LoadMask(Ext.getBody(), {msg:"Please wait..."});

Ext.define('App.store.Scope',
{
	extend		: 'Ext.data.Store',
	id			: 'idScopeStore',
	model		: 'App.model.Scope',
	remoteSort	: true,
	// allow the grid to interact with the paging scroller by buffering
	buffered	: false,
	autoSync	: true,
	autoLoad	: false,
	pageSize	: varGridDefaultPageSize,
	proxy		: 
	{
		// load using script tags for cross domain, if the data in on the same domain as
		// this page, an HttpProxy would be better		
		url 	: urlScopeData,
		type	: varProxyType,					
		reader	: 
		{
			type: varReaderType,
			root: varRootRecord,
			idProperty: varIdProperty,
			totalProperty: varTotalProperty,					
			successProperty	: varSuccessProperty,
			messageProperty: varMessageProperty,
		},
		writer: 
		{
			type			: varWriterType,
			writeAllFields : varWriteAllFields,
		},
		simpleSortMode: varSimpleSortModeOn,
		listeners:
        {
            exception: function(proxy, response, operation)
            {
                Ext.MessageBox.show(
                {
                    title: 'Remote Exception',
                    msg: operation.getError(),
                    icon: Ext.MessageBox.ERROR,
                    buttons: Ext.Msg.OK
                });
            }
        },
	},	
	listeners: 
	{		
		beforeload : function(store, operation, eOpts )
		{			
			console.log('Before Loading.....');
			varScopeMask.show();
		},
		
		load: function(store,records,options) 
		{			
			store.loaded = true;
			console.log('Loaded.....');		
			varScopeMask.hide();
		} ,
		write: function(proxy, operation)
		{
            console.log('operation.action.....'+operation.action);
            //if (operation.action == 'destroy')
            //{
            //    main.child('#form').setActiveRecord(null);
            //}
            //Ext.example.msg(operation.action, operation.resultSet.message);
            Ext.Msg.alert(operation.action, operation.resultSet.message);
        }
	},
	sorters: [{property: 'name', direction: 'DESC'}]	
});	
///////////////////////////////////////////////////