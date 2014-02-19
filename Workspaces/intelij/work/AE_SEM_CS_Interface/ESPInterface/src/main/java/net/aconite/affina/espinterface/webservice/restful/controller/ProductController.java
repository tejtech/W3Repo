package net.aconite.affina.espinterface.webservice.restful.controller;

import net.aconite.affina.espinterface.webservice.restful.model.UIProduct;
import net.aconite.affina.espinterface.webservice.restful.model.UIRecords;
import net.aconite.affina.espinterface.webservice.restful.model.UIResponse;
import net.aconite.affina.espinterface.webservice.restful.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "api/product"/*, headers = "Accept=application/json"*/)
public class ProductController 
{
	
	private IProductService productService;
        private String RECORD_NAME="products";
        
        
	@Autowired
	public ProductController(IProductService productService) 
        {
            this.productService = productService;
	}
        
        @RequestMapping(method = RequestMethod.GET)
        @ResponseBody
	public UIRecords<UIProduct> getList(Integer limit, Integer start, Integer page, String sort, String dir,Integer id, String name) 
        {
            UIRecords<UIProduct> uiResords=new UIRecords<UIProduct>(12, RECORD_NAME, UIProduct.convertList());
            
            return uiResords;
	}
        
	
	@RequestMapping(value = "/random", method = RequestMethod.GET)
        @ResponseBody
	public UIProduct randomPerson() 
        {
		return productService.getRandom();
	}
		
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
        @ResponseBody
	public UIProduct getById(@PathVariable Long id) 
        {
		return productService.getById(id);
	}
	
	/* same as above method, but is mapped to
	 * /api/person?id= rather than /api/person/{id}
	 */
	@RequestMapping(params="id", method = RequestMethod.GET)
	@ResponseBody
	public UIProduct getByIdFromParam(@RequestParam("id") Long id) 
        {
		return productService.getById(id);
	}
	
	/**
	 * Saves new person. Spring automatically binds the name
	 * and age parameters in the request to the person argument
	 * @param product
	 * @return String indicating success or failure of save
	 */     
        @RequestMapping(method = RequestMethod.POST)
        public ResponseEntity<UIResponse> add(@RequestBody UIProduct uiProduct)
        {
            //Product product = uiProduct.toProduct();
            try
            {
                //productService.saveProduct(product);
                HttpHeaders responseHeaders = new HttpHeaders();//getModifiedDataHeader();
                //responseHeaders.set(HTTPResponseHeaders.LOCATION.getHttpHeader(), product.getName().toString());
                return new ResponseEntity<UIResponse>(new UIResponse(true,String.format("%s successfully added", uiProduct.getId())), responseHeaders, HttpStatus.CREATED);            
            }
            catch(Exception e)
            {
                return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("%s create is failed", uiProduct.getName())), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        
        
        @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
        public ResponseEntity<UIResponse> update(@PathVariable("id") String Id, @RequestBody UIProduct uiProduct)
        {
            try
            {
                //productService.updateProduct(uiProduct.toProduct());
                HttpHeaders responseHeaders = new HttpHeaders();//getModifiedDataHeader();
                return new ResponseEntity<UIResponse>(new UIResponse(true,String.format("%s successfully updated", uiProduct.getName())), responseHeaders, HttpStatus.OK);                        
            }
            catch(Exception e)
            {
                return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("%s update is failed", uiProduct.getName())), HttpStatus.INTERNAL_SERVER_ERROR);                        
            }
        }
        
        @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
        public ResponseEntity<UIResponse> delete(@PathVariable("id") Integer id)
        {
            try
            {
                //productService.deleteProduct(id);
                HttpHeaders responseHeaders = new HttpHeaders();//getModifiedDataHeader();
                return new ResponseEntity<UIResponse>( new UIResponse(true,String.format("Id %s successfully deleted", id)), responseHeaders, HttpStatus.OK);            
            }
            catch(Exception e)
            {
                return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("Id %s delete is failed", id)), HttpStatus.INTERNAL_SERVER_ERROR);            
            }

        }
}
