package net.aconite.affina.espinterface.webservice.restful.model;

import com.platform7.pma.application.Application;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;


public class UIApplication 
{
        @JsonProperty
	private Integer id;

        @JsonProperty
	private String name;
        
        public UIApplication()
        {
        }
        
        public UIApplication(Integer id,String name)
        {
            this.id = id;
            this.name = name;
        }
        
        public UIApplication(Application ormObject)
        {
            this.id = ormObject.getOID().intValue();
            this.name = ormObject.getName();
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
	
	public static List<UIApplication> convertList(List<Application> ormList)
        {
            List<UIApplication> uiList  = new ArrayList<UIApplication>();
            for(Application ormObject : ormList)
            {
                uiList.add(new UIApplication(ormObject));
                
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
