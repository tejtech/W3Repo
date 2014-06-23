package net.aconite.affina.espinterface.webservice.restful.model;

import com.platform7.pma.product.PMAProductPart;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;


public class UIProductPart 
{
        @JsonProperty
	private Integer id;

        @JsonProperty
	private String name;
        
        public UIProductPart()
        {
        }
        
        public UIProductPart(Integer id,String name)
        {
            this.id = id;
            this.name = name;
        }
        
        public UIProductPart(PMAProductPart ormObject)
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
	
	public static List<UIProductPart> convertList(List<PMAProductPart> ormList)
        {
            List<UIProductPart> uiList  = new ArrayList<UIProductPart>();
            for(PMAProductPart ormObject : ormList)
            {
                uiList.add(new UIProductPart(ormObject));                
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
