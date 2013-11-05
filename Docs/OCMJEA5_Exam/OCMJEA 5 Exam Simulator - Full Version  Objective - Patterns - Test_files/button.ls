















		
		try
		{
		$zoho.livedesk._callbacks={},$zcb=$zoho.livedesk._callbacks,$zv=$zoho.livedesk.values,$zlm={},$zlch={};
		$zoho.livedesk._invoke=function(type,data)
		{		
			if($zcb[type] && typeof($zcb[type]) === "function")	
        	{
        		 if(typeof(data)!="object")
        		 {
        		 	if($zlm[type]===data)
        		 	{
        		 		return false;
        		 	}
        		 	$zlm[type]=data;
        		 }
        		 var returnvalue;
        		 if(data["visitid"])
        		 {
        		     returnvalue=$zcb[type](data["visitid"],data);
        		 }
        		 else
        		 {
	                 returnvalue=$zcb[type](data);
	             }
	             returnvalue=(returnvalue!=undefined)?returnvalue:-1;
		     	 var obj={};obj[type]=returnvalue;
		     	 if($zlch[type]){$zoho.livedesk.setValue("callback",obj);}
	             $zlm[type]={}; 
        	}
     	},
        $zoho.livedesk.visitor=
    	{
           	name:function(value) 
           	{
           		if(value!=null)
           		{
           			$zoho.livedesk.setValue("name",value);
           		}
           		return $zv["name"]; 
           	},
           	email:function(value) 
           	{
           		if(value!=null)
           		{
           			$zoho.livedesk.setValue("email",value);
           		}
           		return $zv["email"];     			
           	},
           	question:function(value) 
           	{
           		if(value!=null)
           		{
           			$zoho.livedesk.setValue("question",value);
           		}
           		return $zv["question"]; 
           	},
           	contactnumber:function(value)
           	{
           		if(value!=null)
           		{
           			$zoho.livedesk.setValue("phone",value);
           		}
           		return $zv["phone"];
           	},
           	info:function(object) 
           	{
           		if(object)$zv["info"]=object;
           		return $zv["info"]; 
           	},
           	authkey:function(value) 
           	{
           		if(value)$zv["authkey"]=value;
           		return $zv["authkey"]; 
           	},
           		chat:function(clbk) 
           		{
           			$zcb["visitor.chat"]=clbk; 
           		},
           		attend:function(clbk) 
           		{
           			$zcb["visitor.attend"]=clbk; 
           		},
           		missed:function(clbk) 
           		{
           			$zcb["visitor.missed"]=clbk; 
           		},
           		agentsoffline:function(clbk) 
           		{
           			$zcb["visitor.offline"]=clbk; 
           		},
           		chatmessage:function(clbk) 
           		{
           			$zcb["visitor.chatmessage"]=clbk; 
           		},
           		chatcomplete:function(clbk) 
           		{	
           			$zcb["visitor.chatcomplete"]=clbk; 
           		},
           		rating:function(clbk) 
           		{
           			$zcb["visitor.rating"]=clbk; 
           		},
           		feedback:function(clbk) 
           		{
           			$zcb["visitor.feedback"]=clbk; 
           		}
      		},
      		$zoho.livedesk.chat= 
      		{
      			mode:function(value) 
      			{
      				if(value!=null)
      				{
	      				$zoho.livedesk.setValue("chatmode",value);
      				}
      				return $zv["chatmode"]; 
      			},
      			sendmessage:function(value) 
      			{
      				if(Float && value)
      				{
      					Float.sendMessage(value); 
      				}
      			},
      			department:function(value) 
      			{
      				if(value!=null)
           			{
           				$zoho.livedesk.setValue("department",value);
           			}
	      			return $zv["department"]; 
      			},
      			defaultdepartment:function(value) 
      			{
      				if(value)$zv["defaultdepartment"]=value;
      				return $zv["defaultdepartment"]; 
      			},
      			agent:function(value) 
      			{
      				if(value)$zv["agent"]=value;
      				return $zv["agent"]; 
      			},
      			messages:function(object) 
      			{
      				if(object)$zv["chatmessages"]=object;
      				return $zv["chatmessages"]; 
      			},
      			title:function(value) 
      			{
      				if(value)$zv["title"]=value;
      				return $zv["title"]; 
      			},
      			online:function(clbk) 
           		{
           			$zcb["chat.online"]=clbk; 
           		},
           		offline:function(clbk) 
           		{
           			$zcb["chat.offline"]=clbk; 
           		},
           		logo:function(value)
           		{
           			if(value)$zv["clogo"]=value;
      				return $zv["clogo"];
           		},
           		waitinghandler:function(clbk)
           		{
           			 var a="chat.waitinghandler";
           		     $zcb[a]=$zlch[a]=clbk;
           		     $zv[a]=(clbk==null)?false:true;
           		},
           		start:function()
           		{
           			broadcastMessage("chatstart",{});
           		} 
      		},
      		$zoho.livedesk.rating=
      		{
      			visible:function(value)
      			{
	      			if(value)$zv["rating.visible"]=value;
      				return $zv["rating.visible"];
      			}
      		},
      		$zoho.livedesk.feedback=
      		{
      			visible:function(value)
      			{
      				if(value)$zv["feedback.visible"]=value;
      				return $zv["feedback.visible"];
      			}
      		},
      		$zoho.livedesk.integ=
      		{
      			requestid:function(value)
      			{
      				if(value!=null)
           			{	
           				$zoho.livedesk.setValue("requestid",value);
           			}
           			return $zv["requestid"]; 
      			}
      		},
      		$zoho.livedesk.chatbubble= 
      		{
      			visible:function(value) 
      			{
      				if(value)$zv["bubblevisible"]=value;
      				return 	$zv["bubblevisible"]; 
      			},
      			animate:function(value) 
      			{
      				if(value)$zv["bubbleanimatetimer"]=value;
      				return 	$zv["bubbleanimatetimer"];
      			},
      			src:function(value)
      			{
      				if(value)$zv["bubblesrc"]=value;
      				return $zv["bubblesrc"]; 
      			}
      		},
      		$zoho.livedesk.chatbutton= 
      		{
      			texts:function(object) 
      			{
      				if(object)$zv["buttontexts"]=object;
      				return $zv["buttontexts"]; 
      			},
      			icon:function(value) 
      			{
      				if(value)$zv["buttonicon"]=value;
      				return $zv["buttonicon"]; 
      			},
      			visible:function(value) 
      			{
      				if(value)$zv["buttonvisible"]=value;
      				try 
      				{
      					zhandleLiveEvent("buttonvisible",value); 
      				}
      				catch(e){} 
      				return $zv["buttonvisible"]; 
      			},
      			onlineicon: 
      			{
      					src:function(value) 
      					{
      						if(value)$zv["buttononlineicon"]=value;
      						return 	$zv["buttononlineicon"]; 
      					}
      			},
      			offlineicon: 
      			{
      					src:function(value) 
      					{
      						if(value)$zv["buttonofflineicon"]=value;
      						return $zv["buttonofflineicon"]; 
      					}
      			},
      			click:function(clbk) 
           		{
           			$zcb["chatbutton.click"]=clbk; 
           		},
           		width:function(value)
           		{
           			if(value)$zv["bwidth"]=value;
      				return $zv["bwidth"];
           		}
      		},
      		$zoho.livedesk.floatbutton= 
      		{
      			position:function(value) 
      			{
      				if(value)$zv["floatposition"]=value;
      				return $zv["floatposition"]; 
      			},
      			visible:function(value) 
      			{
      				if(value)$zv["floatvisible"]=value;
      				try 
      				{
      					if(Float)
      					{
      						Float.handleVisible(value); 
      					}
      				}catch(e){} 
      				return $zv["floatvisible"]; 
      			},
      			onlineicon: 
      			{
      					src:function(value) 
      					{
      						if(value)$zv["floatbuttononlinesrc"]=value;
      						return $zv["floatbuttononlinesrc"]; 
      					}
      			},
      			offlineicon: 
      			{
      					src:function(value) 
      					{
      						if(value)$zv["floatbuttonofflinesrc"]=value;
      						return $zv["floatbuttonofflinesrc"]; 
      					}
      			},
      			click:function(clbk) 
           		{
           			$zcb["floatbutton.click"]=clbk; 
           		}
      		},
      		$zoho.livedesk.chatwindow= 
      		{
      			visible:function(value) 
      			{
      				if(value)$zv["chatwindowvisible"]=value;
      				try 
      				{
      					zhandleLiveEvent("chatwindowvisible",value); 
      				}
      				catch(e){}
      				return $zv["chatwindowvisible"]; 
      			}
      		},
      		$zoho.livedesk.floatwindow= 
      		{
      			visible:function(value) 
      			{
      				if(value)$zv["floatwindowvisible"]=value;
      				try 
      				{
      					if(Float)
      					{
      						Float.handleWinVisible(value); 
      					}
      					
      				}
      				catch(e){} 
      				return $zv["floatwindowvisible"]; 
      			},
      			open:function(clbk)
      			{
      				if(clbk)
      				{
      					$zcb["chat.open"]=clbk;
      				}
      				else
      				{
      					this.visible("show");
      				}
      			},
      			close:function(clbk)
      			{
      				if(clbk)
      				{
      					$zcb["chat.close"]=clbk;
      				}
      				else
      				{
      					this.visible("hide");
      				}
      			},
      			minimize:function(clbk)
      			{
      				if(clbk)
      				{
      					$zcb["floatwindow.minimize"]=clbk;
      				}
      				else
      				{
      					try
      					{
      						Float.closeChat(true);
      					}
      					catch(e){}
      				}
      			}
      		},
      		$zoho.livedesk.custom=
      		{
      			html:function(container,object)
           		{
           			if(container && object)
           			{
           				$zv["customhtml"]=[container,object];
           				try
           				{
           					Float.drawFloatButtonHtml($zv["customhtml"]);
           				}
           				catch(e)
           				{	
           					try
           					{
           						zlsDrawButtonHtml($zv["customhtml"]);
           					}
           					catch(x)
           					{}
           				}
           			}
           			return $zv["floatbuttondraw"];
           		}
      		},
      		$zoho.livedesk.customfield=
      		{
      			add:function(value)
      			{
      				var a="customfield";
      				if(value)
      				{
      					var arr=$zv[a]=$zv[a]||[];
      					this._splice([value["name"]],arr);
				    	$zv[a].push(value);
      				}
      			},
      			clear:function(value)
      			{
      				var a="customfield";
      				if(!value){$zv[a]=[];}
      				if(Object.prototype.toString.call(value) === "[object Array]")
      				{
      				
				    	this._splice(value,$zv[a]||[]);
				   	}
				   	broadcastMessage(a,$zv[a]);
      			},
      			_splice:function(value,arr)
      			{
      					for(var i=0;i<value.length;i++)
				    	{
				    		for(var j=0;j<arr.length;j++)
				    		{
				    			if(arr[j]["name"]==value[i])
				    			{
				    				arr.splice(j,1);
				    				break;
				    			}
				    		}
				    	}
      			}
      		},
      		$zoho.livedesk.language=function(value) 
      		{
      			if(value)$zv["language"]=value;
      			return $zv["language"]; 
      		},
      		$zoho.livedesk.set=function(object) 
      		{
				for(var d in object)
				{
					try 
					{
						var s=d.split("."); 
						var func=this[s[0]]; 
						for(var i=1;i<s.length;i++)
						{
							if(s[i])
							{
								func=func[s[i]]; 
							}
						}
						if(typeof func=="function")
						{
							func(object[d]); 
						}
					 }
					 catch(e){} 
				}				      			
      		}
      		$zoho.livedesk.setValue=function(type,value)
      		{
      			if(value==null || !type){return;}
      			$zv[type]=value;
      			broadcastMessage(type,value);
      		}
      		
      		$zoho.ld={};
      		$zoho.ld.handle=
      		{
      			customClick:function(status)
      			{
      				var carr=$zoho.livedesk.values["customhtml"];
      				if(!carr)return false;
      				var cobj=carr[1],func=cobj[status+'.click'];
      				if(typeof func=="function")
      				{
      					func();
      				}
      				else
      				{
      					try
      					{
      						Float.handleClick();
      					}
      					catch(e)
      					{
      						try
      						{
      							zhandleLiveEvent("chatwindowvisible","show");
      						}
      						catch(e)
      						{}
      					}
      				}
      			}
      		}
      		}catch(e){} 
      		
function broadcastMessage(type,value)
{
	try{Float.broadcastMessage(type,value);}catch(e){}
	try{AgentsChat.broadcastMessage(type,value);}catch(e){}
	try{zlsWinBroadcastMessage(type,value)}catch(e){}
	try{zlsBtnBroadcastMessage(type,value)}catch(e){}
}

		var zbtnurl = "https://livedesk.zohopublic.com/whizlabs/drawbutton.ls?"+"embedname="+encodeURIComponent('whizlabsteam');  

			zbtnurl += "&referrer="+encodeURIComponent(window.btoa(document.referrer)).substring(0,3000);
			zbtnurl+="&cpage="+encodeURIComponent(window.btoa("")).substring(0,3000); 

		zbtnurl += "&pagetitle="+encodeURIComponent(window.document.title);

	var _zlsbtnfrm=document.getElementById("zldbtnframe"); 
	var _zlsstatus="",_zlscprocess=false;
	if(!_zlsbtnfrm)
	{
		var str = "<iframe src=\""+zbtnurl+"\" style='overflow: hidden;height:67px;width:274px' frameborder='0' border='0' allowtransparency='true' id='zldbtniframe' scrolling='no'></iframe>";
    	document.write(str);
	}
	else
	{	
		var $zbtniframe=document.createElement("iframe");
		zbtnurl=zbtnurl+"&src=script";
		$zbtniframe.src=zbtnurl; 
		$zbtniframe.style.overflow="hidden";
		$zbtniframe.style.height="67"+"px";
		$zbtniframe.style.width="274"+"px";
		$zbtniframe.style.minWidth="51"+"px";
		$zbtniframe.frameBorder='0'; 
		$zbtniframe.border='0'; 
		$zbtniframe.allowTransparency='true'; 
		$zbtniframe.id='zldbtniframe'; 
		$zbtniframe.scrolling='no'; 
		$zbtniframe.name="button";
	}
	
    if(window.addEventListener) 
    {
          window.addEventListener('load',function(){ZloadButton()}, false); 
    }
    else  
    {
        window.attachEvent('onload', function(){ZloadButton()}); 
    }
     	
	function ZloadButton()
	{
		try
		{
			$zoho.livedesk.ready();
			var width=$zoho.livedesk.values["bwidth"];
			if(width && !isNaN(width))
			{
				$zbtniframe.style.width=width+"px";
			}
		}catch(e){} 
		try 
		{
			if (_zlsbtnfrm.nextSibling) 
			{
 				 _zlsbtnfrm.parentNode.insertBefore($zbtniframe, _zlsbtnfrm.nextSibling); 
			}
			else  
			{
  				_zlsbtnfrm.parentNode.appendChild($zbtniframe); 
			}
		}
		catch(e){} 
		
    	if (window.addEventListener)
    	{
    		addEventListener("message", zlsbtnPostMessage, false);
    	}	 
    	else 
    	{
    		attachEvent("onmessage",zlsbtnPostMessage);
    	}
    }
    
    function zhandleLiveEvent(evnt,value)
    {
    		try 
      		{
      				var msg={"operation":"liveevent","value":evnt};
				    msg[evnt]=value;
				    if(evnt=="buttonvisible")
				    {
				    	handleBtnIframeVisible(value);
				    }
			}
			catch(e){msg={};}
			setTimeout(function(){try{document.getElementById("zldbtniframe").contentWindow.postMessage(convetToJSON(msg),"*")}catch(e){}},0);
    }
    
    function handleBtnIframeVisible(value)
    {
    	var btnifrm=document.getElementById("zldbtniframe");	
    	if(value=="hide")
		{
			btnifrm.style.display="none";
		}
		else
		{
			btnifrm.style.display="block";	
		}
    }
    
    function zlsbtnPostMessage(event)
    {
    	if(event.origin.indexOf("https://livedesk.zohopublic.com")==-1) 
     	{
        	return; 
       	}
     	var data=eval("("+event.data+")");
     	var operation=data["operation"];
      	if(data=="apimessage")
      	{
	      	try 
      		{
				var msg=$zoho.livedesk.values;    
			}
			catch(e){msg={};}
			if(msg["buttonvisible"])
			{
				handleBtnIframeVisible(msg["buttonvisible"]);
			}
			zlsBtnPostMessage(msg);
      	}
      	else if(operation && operation=="callbacks")
    	{
    		try 
    		{
    			var value=	data["value"]; 
    			if($zoho && value)
    			{
    				for(var d in value)
    				{
    					if(d=="chat.online" || d=="chat.offline")
    					{
    						_zlsstatus=d.split(".")[1];
    						if(_zlscprocess==false)
    						{
    							try{zlsDrawButtonHtml($zoho.livedesk.values["customhtml"]);}catch(e){}
    						}
    					}
    					
    					$zoho.livedesk._invoke(d,value[d]); 
    				}
    			}
    		}
    		catch(e){} 
    	}
    }
    
    function zlsBtnBroadcastMessage(type,value)
    {
    		var msg={'operation':'broadcast'};
    		try 
			{
				var content={};
				content[type]=value;
				msg["value"]=content;
			}	 
			catch(exp) 
			{}
			zlsBtnPostMessage(msg);
    }

    function convetToJSON(message)
   {
        return Object.toJSON?Object.toJSON(message):JSON.stringify(message)
   }
    
    function zlsBtnPostMessage(obj)
    {
    	if(!obj){return;}
    	setTimeout(function(){try{document.getElementById("zldbtniframe").contentWindow.postMessage(convetToJSON(obj),"*")}catch(e){}},0);
    }
    
    function zlsDrawButtonHtml(obj)
	{
		if(!obj || !_zlsstatus){return false;}
		try
		{
			if(Float){return false;} //To Check the Float Window in the same window
		}
		catch(e)
		{}
		
		var contobj=document.getElementById(obj[0]);
	
		var tobj=obj[1];
		if(tobj[_zlsstatus+".html"])
		{
			contobj.innerHTML=tobj[_zlsstatus+".html"];
		}
		
		if(!_zlscprocess)
		{
	   		if(contobj.addEventListener)
	   		{
		   		contobj.addEventListener("click",function(){$zoho.ld.handle.customClick(_zlsstatus)}, true);
	   		}
	   		else
	   		{
		   		contobj.attachEvent("onclick", function(){$zoho.ld.handle.customClick(_zlsstatus)});
	   		}
	   	}
	   	_zlscprocess=true;
 	}

