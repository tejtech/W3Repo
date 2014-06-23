package net.aconite.affina.espinterface.webservice.restful.model;

import com.platform7.pma.product.PMAProduct;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;


public class UIProduct 
{
        @JsonProperty
	private Integer id;

        @JsonProperty
	private String name;
        
        public UIProduct()
        {
        }
        
        public UIProduct(Integer id,String name)
        {
            this.id = id;
            this.name = name;
        }
        
        public UIProduct(PMAProduct ormObject)
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
                
	
	public static List<UIProduct> convertList(List<PMAProduct> ormList)
        {           
            List<UIProduct> uiList  = new ArrayList<UIProduct>();
            for(PMAProduct ormObject : ormList)
            {
                uiList.add(new UIProduct(ormObject));               
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
	/*
         if(!isTest)
            {
                
            }
            else
            {
                uiList.add(new UIProduct(10,"Product1")); 
                uiList.add(new UIProduct(20,"Product2")); 
                uiList.add(new UIProduct(30,"Product3")); 
                uiList.add(new UIProduct(40,"Product4")); 
                uiList.add(new UIProduct(50,"Product5")); 
            }
         */
	
}
