package net.aconite.affina.espinterface.webservice.restful.model;

import com.platform7.pma.card.manifestapplication.ManifestApplication;
import java.util.ArrayList;
import java.util.List;
import net.aconite.affina.espinterface.webservice.restful.service.model.StageScript;
import org.codehaus.jackson.annotate.JsonProperty;


public class UIStageScript 
{
        @JsonProperty
	private Integer id;

        @JsonProperty
	private String name;
        
        public UIStageScript()
        {
        }
        
        public UIStageScript(Integer id,String name)
        {
            this.id = id;
            this.name = name;
        }
        
        public UIStageScript(StageScript ormObject)
        {
            this.id = ormObject.getManifestApplicationOID().intValue();
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
	
	public static List<UIStageScript> convertList(List<StageScript> ormList)
        {
            List<UIStageScript> uiList  = new ArrayList<UIStageScript>();
            for(StageScript ormObject : ormList)
            {
                uiList.add(new UIStageScript(ormObject));
                
            }
            return uiList;
        }

	@Override
	public String toString() 
        {
		return "ApplicationInstance [name=" + name + ", id=" + id + "]";
	}
	
	
}
