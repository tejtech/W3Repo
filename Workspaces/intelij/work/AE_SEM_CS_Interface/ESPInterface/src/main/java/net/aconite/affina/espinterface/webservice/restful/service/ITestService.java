package net.aconite.affina.espinterface.webservice.restful.service;

import net.aconite.affina.espinterface.webservice.restful.model.UITest;

public interface ITestService 
{
	public UITest getRandom();
	public UITest getById(Long id);
	public void save(UITest product);
}
