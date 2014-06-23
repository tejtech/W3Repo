function render2TickerDate(val) 
{
	//yyyy-MM-dd'T'HH:mm:ss.mmmZ	
	return freeFormDate=formatDate(new Date(val),'yyyy-MM-dd HH:mm:ss');
}





function renderIcon(val) 
{	
    return '<img src="../_images/' + val.replace('/','') + '.png">';
}

function renderTopic(value, p, record) 
{
	return Ext.String.format
	(
		'<a href="http://sencha.com/forum/showthread.php?t={2}" target="_blank">{0}</a>',
		value,
		record.data.forumtitle,
		record.getId(),
		record.data.forumid
	);
}

function formatDate(value)
{
	return value ? Ext.Date.dateFormat(value, 'Y M d') : '';
}

function getYear(value)
{
	return value ? Ext.Date.dateFormat(value, 'Y') : '';
}

function getMonth(value)
{
	return value ? Ext.Date.dateFormat(value, 'm') : '';
}

function getDay(value)
{
	return value ? Ext.Date.dateFormat(value, 'd') : '';
}

function getHour(value)
{
	return value ? Ext.Date.dateFormat(value, 'H') : '';
}

function getMinute(value)
{
	return value ? Ext.Date.dateFormat(value, 'i') : '';
}

/*
function render2LocalTimeZone(val) 
{	
		var localDate = new Date()
		var localInMilli= -1*localDate.getTimezoneOffset()*60*1000;
					
		var freeFormDate=formatDate(new Date(getDateFromFormat(val,'dd-MMM-yyyy hh:mm:ss')),'MMM dd, yyyy hh:mm:ss')										
		var remoteDate = new Date(freeFormDate);
		
		var adjustedTimeInMilli=	remoteDate.getTime()+localInMilli;	
		
		var adjustedDate=new Date(adjustedTimeInMilli);			
		
		return formatDate(adjustedDate,'dd-MMM-yyyy hh:mm:ss');
}


function render2DateFormat(val) //2011-01-01T00:00:00
{	
		//return  val.replace('T00:00:00','');
		return  val.substring(0,10);
}


Ext.form.XmlErrorReader = function()
{
    Ext.form.XmlErrorReader.superclass.constructor.call(this, {
            record : 'field',
            success: '@success'
        }, [
            'id', 'msg'
        ]
    );
};
Ext.extend(Ext.form.XmlErrorReader, Ext.data.XmlReader);
*/