package net.aconite.affina.espinterface.webservice.restful.service;

import java.util.Random;
import net.aconite.affina.espinterface.webservice.restful.model.UIProduct;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService 
{
        String[] ids = {"32", "23", "44", "16"};
	String[] names = {"vfr", "bgt", "nhy", "mju"};

	@Override
	public UIProduct getRandom() 
        {
		UIProduct person = new UIProduct(randomId(),randomName());		
		return person;
	}

	@Override
	public UIProduct getById(Long id) 
        {
		UIProduct person = new UIProduct();
		person.setName(names[id.intValue()]);
		person.setId(ids[id.intValue()]);
		return person;
	}
	
	@Override
	public void save(UIProduct product) 
        {
		// Save person to database ...
	}
	
	private String randomId() 
        {
		Random random = new Random();
		//return String.valueOf(10 + random.nextInt(100));
                return ids[random.nextInt(ids.length)];
	}

	private String randomName()
        {
		Random random = new Random();
		return names[random.nextInt(names.length)];
	}

}
