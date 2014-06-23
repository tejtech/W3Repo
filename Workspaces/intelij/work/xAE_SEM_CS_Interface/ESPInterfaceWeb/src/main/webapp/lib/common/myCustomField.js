	//////////Plugin//////////////
	//var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {clicksToEdit: 2});
	//var rowEditing  = Ext.create('Ext.grid.plugin.RowEditing');

	
	//////////Date Field//////////////
	var apmDateField=Ext.create('Ext.form.field.Date',
	{					
		format: 'Y/m/d h:i:s', 
		//minValue: new Date(),
		disabledDays: [0, 6], //sunday & saturday will be disabled
		disabledDaysText: 'Plants are not available on the weekends',
		value: new Date()  // defaults to today
	});
			
			
	//////////Number Field//////////////	
	var apmSecondsField=Ext.create('Ext.form.field.Number',
	{
		//xtype: 'numberfield',
		allowBlank: true,
		emptyText :'*',
		minValue: 0,
		maxValue: 59,
		//vtype:'password'
	});
				
				
	var apmMinutesFeild=Ext.create('Ext.form.field.Number',
	{
		//xtype: 'numberfield',
		allowBlank: true,
		emptyText :'*',
		minValue: 0,
		maxValue: 59,
		//vtype:'password'
	});
	
	var apmHoursFeild=Ext.create('Ext.form.field.Number',
	{
			//xtype: 'numberfield',
			allowBlank: true,
			emptyText :'*',
			minValue: 0,
			maxValue: 23,
			//vtype:'password'
	});
	
	var apmDaysFeild=Ext.create('Ext.form.field.Number',
	{
			//xtype: 'numberfield',
			allowBlank: true,
			emptyText :'*',
			minValue: 1,
			maxValue: 31,
			//vtype:'password'
	});
	
	var apmMonthsField=Ext.create('Ext.form.field.Number',
	{
			//xtype: 'numberfield',
			allowBlank: true,
			emptyText :'*',
			minValue: 1,
			maxValue: 12,
			//vtype:'password'
	});
	
	var apmWeeksField=Ext.create('Ext.form.field.Number',
	{
			//xtype: 'numberfield',
			allowBlank: true,
			emptyText :'*',
			minValue: 1,
			maxValue: 7,
			//vtype:'password'
	});
	
	var apmYearsField=Ext.create('Ext.form.field.Number', 
	{
			//xtype: 'numberfield',
			allowBlank: true,
			emptyText :'*',
			minValue: 2012,
			maxValue: 2090,
			//vtype:'password'
	});
	
	///////////////////////////////////
	
	
	
	

	
	