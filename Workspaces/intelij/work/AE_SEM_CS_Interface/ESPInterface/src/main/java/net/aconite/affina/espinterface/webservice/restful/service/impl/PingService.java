package net.aconite.affina.espinterface.webservice.restful.service.impl;

import java.util.Random;
import net.aconite.affina.espinterface.webservice.restful.model.UIPing;
import net.aconite.affina.espinterface.webservice.restful.service.IPingService;
import org.springframework.stereotype.Service;

@Service("pingService")
public class PingService implements IPingService 
{
        Integer[] ids = {32, 23, 44, 16};
	String[] names = {"vfr", "bgt", "nhy", "mju"};

	@Override
	public UIPing getRandom() 
        {
		UIPing person = new UIPing(randomId(),randomName());		
		return person;
	}

	@Override
	public UIPing getById(Long id) 
        {
		UIPing person = new UIPing();
		person.setName(names[id.intValue()]);
		person.setId(ids[id.intValue()]);
		return person;
	}
	
	@Override
	public void save(UIPing product) 
        {
		// Save person to database ...
	}
	
	private Integer randomId() 
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
