package net.aconite.affina.espinterface.webservice.restful.model;

import com.platform7.pma.request.emvscriptrequest.ESPBusinessFunction;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;


public class UIBusinessFunction 
{
        @JsonProperty
	private Integer id;

        @JsonProperty
	private String name;
        
        public UIBusinessFunction()
        {
        }
        
        public UIBusinessFunction(Integer id,String name)
        {
            this.id = id;
            this.name = name;
        }
        
        public UIBusinessFunction(ESPBusinessFunction ormObject)
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
	
	public static List<UIBusinessFunction> convertList(List<ESPBusinessFunction> ormList)
        {
            List<UIBusinessFunction> uiList  = new ArrayList<UIBusinessFunction>();
            for(ESPBusinessFunction ormObject : ormList)
            {
                uiList.add(new UIBusinessFunction(ormObject));                
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
