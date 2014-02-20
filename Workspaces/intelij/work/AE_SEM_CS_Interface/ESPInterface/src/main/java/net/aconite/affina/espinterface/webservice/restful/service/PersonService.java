package net.aconite.affina.espinterface.webservice.restful.service;

import java.util.Random;
import net.aconite.affina.espinterface.webservice.restful.model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonService implements IPersonService 
{

	String[] names = {"aaaaa", "bbbbbbb", "cccccccc", "ddddddddd"};

	@Override
	public Person getRandom() 
        {
		Person person = new Person();
		person.setName(randomName());
		person.setAge(randomAge());
		return person;
	}

	@Override
	public Person getById(Long id) 
        {
		Person person = new Person();
		person.setName(names[id.intValue()]);
		person.setAge(50);
		return person;
	}
	
	@Override
	public void save(Person person) 
        {
		// Save person to database ...
	}
	
	private Integer randomAge() 
        {
		Random random = new Random();
		return 10 + random.nextInt(100);
	}

	private String randomName()
        {
		Random random = new Random();
		return names[random.nextInt(names.length)];
	}

}
