package net.aconite.affina.espinterface.webservice.restful.service.impl;

import java.util.Random;
import net.aconite.affina.espinterface.webservice.restful.model.UITest;
import net.aconite.affina.espinterface.webservice.restful.service.ITestService;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestService implements ITestService 
{
        Integer[] ids = {32, 23, 44, 16};
	String[] names = {"vfr", "bgt", "nhy", "mju"};

	@Override
	public UITest getRandom() 
        {
		UITest person = new UITest(randomId(),randomName());		
		return person;
	}

	@Override
	public UITest getById(Long id) 
        {
		UITest person = new UITest();
		person.setName(names[id.intValue()]);
		person.setId(ids[id.intValue()]);
		return person;
	}
	
	@Override
	public void save(UITest product) 
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
