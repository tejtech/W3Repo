package net.aconite.affina.espinterface.webservice.restful.service;

import net.aconite.affina.espinterface.webservice.restful.model.UIPing;

public interface IPingService 
{
	public UIPing getRandom();
	public UIPing getById(Long id);
	public void save(UIPing product);
}
