package net.aconite.affina.espinterface.webservice.restful.controller;

import net.aconite.affina.espinterface.webservice.restful.model.UITest;
import net.aconite.affina.espinterface.webservice.restful.common.UIRecords;
import net.aconite.affina.espinterface.webservice.restful.common.UIResponse;
import net.aconite.affina.espinterface.webservice.restful.service.ITestService;
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
@RequestMapping(value = "api/test"/*, headers = "Accept=application/json"*/)
public class TestController 
{
	@Autowired        
	private ITestService testService;
        
        private String RECORD_NAME="tests";
        
        
	//@Autowired
	//public TestController(ITestService testService) 
        //{
        //    this.testService = testService;
	//}
        
        @RequestMapping(method = RequestMethod.GET)
        @ResponseBody
	public UIRecords<UITest> getList(Integer limit, Integer start, Integer page, String sort, String dir,Integer id, String name) 
        {
            UIRecords<UITest> uiResords=new UIRecords<UITest>(12, RECORD_NAME, UITest.convertList());
            
            return uiResords;
	}
        
	
	@RequestMapping(value = "/random", method = RequestMethod.GET)
        @ResponseBody
	public UITest randomPerson() 
        {
		return testService.getRandom();
	}
		
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
        @ResponseBody
	public UITest getById(@PathVariable Long id) 
        {
		return testService.getById(id);
	}
	
	/* same as above method, but is mapped to
	 * /api/person?id= rather than /api/person/{id}
	 */
	@RequestMapping(params="id", method = RequestMethod.GET)
	@ResponseBody
	public UITest getByIdFromParam(@RequestParam("id") Long id) 
        {
		return testService.getById(id);
	}
	
	/**
	 * Saves new person. Spring automatically binds the name
	 * and age parameters in the request to the person argument
	 * @param product
	 * @return String indicating success or failure of save
	 */     
        @RequestMapping(method = RequestMethod.POST)
        public ResponseEntity<UIResponse> add(@RequestBody UITest uiObject)
        {
            //Product product = uiProduct.toProduct();
            try
            {
                //testService.saveProduct(product);
                HttpHeaders responseHeaders = new HttpHeaders();//getModifiedDataHeader();
                //responseHeaders.set(HTTPResponseHeaders.LOCATION.getHttpHeader(), product.getName().toString());
                return new ResponseEntity<UIResponse>(new UIResponse(true,String.format("%s successfully added", uiObject.getId())), responseHeaders, HttpStatus.CREATED);            
            }
            catch(Exception e)
            {
                return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("%s create is failed", uiObject.getName())), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        
        
        @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
        public ResponseEntity<UIResponse> update(@PathVariable("id") String Id, @RequestBody UITest uiObject)
        {
            try
            {
                //testService.updateProduct(uiProduct.toProduct());
                HttpHeaders responseHeaders = new HttpHeaders();//getModifiedDataHeader();
                return new ResponseEntity<UIResponse>(new UIResponse(true,String.format("%s successfully updated", uiObject.getName())), responseHeaders, HttpStatus.OK);                        
            }
            catch(Exception e)
            {
                return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("%s update is failed", uiObject.getName())), HttpStatus.INTERNAL_SERVER_ERROR);                        
            }
        }
        
        @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
        public ResponseEntity<UIResponse> delete(@PathVariable("id") Integer id)
        {
            try
            {
                //testService.deleteProduct(id);
                HttpHeaders responseHeaders = new HttpHeaders();//getModifiedDataHeader();
                return new ResponseEntity<UIResponse>( new UIResponse(true,String.format("Id %s successfully deleted", id)), responseHeaders, HttpStatus.OK);            
            }
            catch(Exception e)
            {
                return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("Id %s delete is failed", id)), HttpStatus.INTERNAL_SERVER_ERROR);            
            }

        }
}
