package net.aconite.affina.espinterface.webservice.restful.controller;

import com.platform7.pma.product.PMAProduct;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.UIRecords;
import net.aconite.affina.espinterface.webservice.restful.common.UIResponse;
import net.aconite.affina.espinterface.webservice.restful.model.UIProduct;
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
	@Autowired        
	private IProductService productService;
        
        private String RECORD_NAME="products";
        
        
	//@Autowired
	//public TestController(ITestService testService) 
        //{
        //    this.testService = testService;
	//}
        
        @RequestMapping(method = RequestMethod.GET)
        @ResponseBody
	public UIRecords<UIProduct> getList(Integer limit, Integer start, Integer page, String sort, String dir,Integer id, String name,Integer scopeId) 
        {
            FilterCriteria filter =new FilterCriteria();
            filter.setId(id);
            filter.setName(name);
            filter.setScopeId(scopeId);
            PagingCriteria  pagingCriteria=new PagingCriteria(start,limit,page,sort,dir);

            QueryResult<PMAProduct> queryResult = productService.getList(filter, pagingCriteria);
            
            UIRecords<UIProduct> uiResords=new UIRecords<UIProduct>(queryResult.getTotalCount(), RECORD_NAME, UIProduct.convertList(queryResult.getResultsList()));
            
            return uiResords;
	}
        	
		
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
        @ResponseBody
	public UIProduct getById(@PathVariable Integer id) 
        {
		return new UIProduct(productService.getById(id));
	}
	
	/* same as above method, but is mapped to
	 * /api/product?id= rather than /api/product/{id}
	 */
	@RequestMapping(params="id", method = RequestMethod.GET)
	@ResponseBody
	public UIProduct getByIdFromParam(@RequestParam("id") Integer id) 
        {
		return new UIProduct(productService.getById(id));
	}
        
        
        
        ////////////////////add,save & update are not being implemented////////////////////////////////////////////////////
	
	/**
	 * Saves new person. Spring automatically binds the name
	 * and age parameters in the request to the person argument
	 * @param product
	 * @return String indicating success or failure of save
	 */     
        @RequestMapping(method = RequestMethod.POST)
        public ResponseEntity<UIResponse> add(@RequestBody UIProduct uiObject)
        {
            //Product product = uiProduct.toProduct();
            try
            {
                //productService.saveProduct(product);
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
        public ResponseEntity<UIResponse> update(@PathVariable("id") String Id, @RequestBody UIProduct uiObject)
        {
            try
            {
                //productService.updateProduct(uiProduct.toProduct());
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
