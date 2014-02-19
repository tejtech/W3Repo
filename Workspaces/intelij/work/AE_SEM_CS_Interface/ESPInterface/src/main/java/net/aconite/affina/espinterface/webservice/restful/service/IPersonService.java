package net.aconite.affina.espinterface.webservice.restful.service;

import net.aconite.affina.espinterface.webservice.restful.model.Person;

public interface IPersonService 
{
	public Person getRandom();
	public Person getById(Long id);
	public void save(Person person);
}
