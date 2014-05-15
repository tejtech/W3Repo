package net.aconite.affina.espinterface.webservice.restful.model;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;


public class UITest 
{
        @JsonProperty
	private Integer id;

        @JsonProperty
	private String name;
        
        public UITest()
        {
        }
        
        public UITest(Integer id,String name)//Product ormObject
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
	
	public static List<UITest> convertList()//(List<Country> ormList)
        {
            List<UITest> uiList  = new ArrayList<UITest>();
            //for(Country ormObject : ormList)
            {
                //uiList.add(new UICountry(ormObject));
                uiList.add(new UITest(10,"Aasasas"));
                uiList.add(new UITest(11,"erterg"));
                uiList.add(new UITest(12,"fbvxvs"));
                uiList.add(new UITest(13,"sa45sef"));
                uiList.add(new UITest(14,"myyunmrxc"));
            }
            return uiList;
        }

	@Override
	public String toString() 
        {
		return "Product [name=" + name + ", id=" + id + "]";
	}
	
	
}
