package net.aconite.affina.espinterface.webservice.restful.model;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;


public class UIProduct 
{
        @JsonProperty
	private String id;

        @JsonProperty
	private String name;
        
        public UIProduct()
        {
        }
        
        public UIProduct(String id,String name)//Product ormObject
        {
            this.id = id;//ormObject.getCode();
            this.name = name;//ormObject.getName();
        }
	
        public String getId() 
        {
            return id;
        }

        public void setId(String id) 
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
	
	public static List<UIProduct> convertList()//(List<Country> ormList)
        {
            List<UIProduct> uiList  = new ArrayList<UIProduct>();
            //for(Country ormObject : ormList)
            {
                //uiList.add(new UICountry(ormObject));
                uiList.add(new UIProduct("10","Aasasas"));
                uiList.add(new UIProduct("11","erterg"));
                uiList.add(new UIProduct("12","fbvxvs"));
                uiList.add(new UIProduct("13","sa45sef"));
                uiList.add(new UIProduct("14","myyunmrxc"));
            }
            return uiList;
        }

	@Override
	public String toString() 
        {
		return "Product [name=" + name + ", id=" + id + "]";
	}
	
	
}
