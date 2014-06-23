package net.aconite.affina.espinterface.webservice.restful.model;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;


public class UIPing 
{
        @JsonProperty
	private Integer id;

        @JsonProperty
	private String name;
        
        public UIPing()
        {
        }
        
        public UIPing(Integer id,String name)//Product ormObject
        {
            this.id = id;//ormObject.getCode();
            this.name = name;//ormObject.getName();
        }
	
        public Integer getId() 
        {
            return id;
        }

        public void setId(Integer id) 
        {
            this.id = id;
        }
        
	public String getName() 
        {
		return name;
	}

	public void setName(String name) 
        {
		this.name = name;
	}
	
	public static List<UIPing> convertList()//(List<Country> ormList)
        {
            List<UIPing> uiList  = new ArrayList<UIPing>();
            //for(Country ormObject : ormList)
            {
                //uiList.add(new UICountry(ormObject));
                uiList.add(new UIPing(10,"Aasasas"));
                uiList.add(new UIPing(11,"erterg"));
                uiList.add(new UIPing(12,"fbvxvs"));
                uiList.add(new UIPing(13,"sa45sef"));
                uiList.add(new UIPing(14,"myyunmrxc"));
            }
            return uiList;
        }

	@Override
	public String toString() 
        {
            StringBuilder sb = new StringBuilder();
            sb.append("\nName  : ").append(name);
            sb.append("\nId  : ").append(id);            
            sb.append("\n");
            return sb.toString(); 
	}
	
	
}
