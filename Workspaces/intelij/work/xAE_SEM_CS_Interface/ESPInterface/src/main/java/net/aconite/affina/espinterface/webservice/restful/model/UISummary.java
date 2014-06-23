package net.aconite.affina.espinterface.webservice.restful.model;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;


public class UISummary 
{
        @JsonProperty
	private Integer id;

        @JsonProperty
	private String name;
        
        public UISummary()
        {
        }
        
        public UISummary(Integer id,String name)//Product ormObject
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
	
	public static List<UISummary> convertList()//(List<Country> ormList)
        {
            List<UISummary> uiList  = new ArrayList<UISummary>();
            //for(Country ormObject : ormList)
            {
                //uiList.add(new UICountry(ormObject));
                uiList.add(new UISummary(10,"Aasasas"));
                uiList.add(new UISummary(11,"erterg"));
                uiList.add(new UISummary(12,"fbvxvs"));
                uiList.add(new UISummary(13,"sa45sef"));
                uiList.add(new UISummary(14,"myyunmrxc"));
            }
            return uiList;
        }

	@Override
	public String toString() 
        {
		return "Product [name=" + name + ", id=" + id + "]";
	}
	
	
}
