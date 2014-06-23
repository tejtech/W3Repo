package net.aconite.affina.espinterface.webservice.restful.model;

import com.platform7.standardinfrastructure.multiissuer.Scope;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;


public class UIScope 
{
        @JsonProperty
	private Integer id;

        @JsonProperty
	private String name;
        
        public UIScope()
        {
        }
        
        public UIScope(Integer id,String name)
        {
            this.id = id;
            this.name = name;
        }
        
        public UIScope(Scope ormObject)
        {
            this.id = ormObject.getPrimaryKey().intValue();
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
	
	public static List<UIScope> convertList(List<Scope> ormList)
        {
            List<UIScope> uiList  = new ArrayList<UIScope>();
            for(Scope ormObject : ormList)
            {
                uiList.add(new UIScope(ormObject));                
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
