package net.aconite.affina.espinterface.webservice.restful.service;

import net.aconite.affina.espinterface.webservice.restful.model.UIProduct;

public interface IProductService 
{
	public UIProduct getRandom();
	public UIProduct getById(Long id);
	public void save(UIProduct product);
}
