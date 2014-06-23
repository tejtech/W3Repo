Ext.apply(Ext.form.VTypes, {
	//  vtype validation function
	
	daterange : function(val, field) 
	{
		var date = field.parseDate(val);

		if(!date)
		{
			return false;
		}
		if (field.startDateField) 
		{
			var start = Ext.getCmp(field.startDateField);
			if (!start.maxValue || (date.getTime() != start.maxValue.getTime())) 
			{
				start.setMaxValue(date);
				start.validate();
			}
		}
		else if (field.endDateField) 
			{
			var end = Ext.getCmp(field.endDateField);
			if (!end.minValue || (date.getTime() != end.minValue.getTime())) 
			{
				end.setMinValue(date);
				end.validate();
			}
		}
		/*
		 * Always return true since we're only using this vtype to set the
		 * min/max allowed values (these are tested for after the vtype test)
		 */
		return true;
	},
	
	time: function(val, field) 
	{
		return timeTest.test(val);
	},
	// vtype Text property: The error text to display when the validation function returns false
	timeText: 'Not a valid time.  Must be in the format "12:34 PM".',
	// vtype Mask property: The keystroke filter mask
	timeMask: /[\d\s:amp]/i,
	timeTest: /^([1-9]|1[0-9]):([0-5][0-9])(\s[a|p]m)$/i,
	//----------------------------------------------------
	IPAddress:  function(val) 
	{
		return ipAddressTest.test(val);
	},
	IPAddressText: 'Must be a numeric IP address',
	IPAddressMask: /[\d\.]/i,
	IPAddressTest : /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/,
	//----------------------------------------------------
	password : function(val, field) 
	{
		if (field.initialPassField) 
		{
			var pwd = Ext.getCmp(field.initialPassField);
			return (val == pwd.getValue());
		}
		return true;
	},	
	passwordText : 'Passwords do not match',	
	//----------------------------------------------------
	hyphen:function(val)
	{
		return this.hyphenTest.test(val);
	},   
	hyphenText	: "Password must contain 7-20 characters with uppercase letters, lowercase letters and at least one number.",
	hyphenTest	: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{7,20}$/,	
	//----------------------------------------------------
	cronMinSec:function(val)
	{
		return this.cronMinSecTest.test(val);
	},   
	cronMinSecText	: "Cron command must contain * or 0-59 numbers only",
	cronMinSecTest	: /^(\*|[0-9]|[0-5][0-9])$/,
	//----------------------------------------------------
	cronHour:function(val)
	{
		return this.cronHourTest.test(val);
	},   
	cronHourText	: "Cron command must contain * or 0-23 numbers only",
	cronHourTest	: /^(\*|[0-9]|[0-1][0-9]|[2][0-3])$/,
	//----------------------------------------------------
	cronMofDay:function(val)
	{
		return this.cronMofDayTest.test(val);
	},   
	cronMofDayText	: "Cron command must contain *,? or 1-31 numbers only",
	cronMofDayTest	: /^(\*|\?|[1-9]|[0-2][0-9]|[3][0-1])$/,
	//----------------------------------------------------
	cronMonth:function(val)
	{
		return this.cronMonthTest.test(val);
	},   
	cronMonthText	: "Cron command must contain * or 1-12 numbers only",
	cronMonthTest	: /^(\*|[1-9]|[0-1][0-2])$/,
	//----------------------------------------------------
	cronMofWeek:function(val)
	{
		return this.cronMofWeekTest.test(val);
	},   
	cronMofWeekText	: "Cron command must contain *, ? or 1-7 numbers only",
	cronMofWeekTest	: /^(\*|\?|[1-7])$/,
	//----------------------------------------------------
	cronYear:function(val)
	{
		return this.cronYearTest.test(val);
	},   
	cronYearText	: "optional",
	cronYearTest	: /^(\*|\|\?|[2][0]\d{2})$/,
	//----------------------------------------------------
	apmCommand:function(val)
	{
		return this.apmCommandTest.test(val);
	},   
	apmCommandText	: "Cron command must contain {x,y} where x<=0 and y>=0 only",
	apmCommandTest	: /^(\{)([0]|-\d{1,2})(\,)([0]|\d{1,2})(\})$/,
	//----------------------------------------------------
	
});

