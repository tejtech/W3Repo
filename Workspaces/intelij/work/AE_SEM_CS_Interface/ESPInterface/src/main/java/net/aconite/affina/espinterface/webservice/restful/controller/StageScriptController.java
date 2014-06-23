package net.aconite.affina.espinterface.webservice.restful.controller;

import com.platform7.pma.request.emvscriptrequest.ESPStageScriptFilter;
import java.text.ParseException;
import java.util.logging.Level;
import net.acointe.affina.esp.AffinaEspUtilException;
import net.acointe.affina.esp.AffinaEspUtils;
import net.aconite.affina.espinterface.exceptions.EspRestServiceValidationException;
import net.aconite.affina.espinterface.webservice.restful.common.QueryResult;
import net.aconite.affina.espinterface.webservice.restful.common.FilterCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.PagingCriteria;
import net.aconite.affina.espinterface.webservice.restful.common.UIRecords;
import net.aconite.affina.espinterface.webservice.restful.common.UIResponse;
import net.aconite.affina.espinterface.webservice.restful.model.UIStageScript;
import net.aconite.affina.espinterface.webservice.restful.service.IStageScriptService;
import net.aconite.affina.espinterface.webservice.restful.service.model.StageScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(value = "api/stagescript"/*, headers = "Accept=application/json"*/)
public class StageScriptController 
{
    private static final Logger logger = LoggerFactory.getLogger(StageScriptController.class.getName());
    
    @Autowired        
    private IStageScriptService stageScriptService;

    private String RECORD_NAME="stagescripts";


    @RequestMapping(value = "/summary", method = RequestMethod.GET)  
    @ResponseBody
    public ResponseEntity<UIResponse> getSummary(Integer id, String name,Integer scopeId,Integer productId,Integer productPartId,Integer applicationId,Integer bin,String cardId)
    {
        try
        {
            FilterCriteria filter =new FilterCriteria();
            filter.setId(id);
            filter.setName(name);
            filter.setScopeId(scopeId);
            filter.setProductId(productId);
            filter.setProductPartId(productPartId);
            filter.setApplicationId(applicationId);
            filter.setBin(bin);
            filter.setCardId(cardId);
            
            FilterCriteria vFilter=isValidFilter(filter);
            
            QueryResult<StageScript> queryResult = stageScriptService.getList(vFilter, null);
            
            int totalCount=queryResult.getTotalCount();

            if(totalCount>0)
            {
                return new ResponseEntity<UIResponse>(new UIResponse(true,String.format("%s valid scriptable application instances found", totalCount)), HttpStatus.OK);
            }
            else
            {
               return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("There are no valid scriptable application instances found")), HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            logger.debug("There has been an error in running a task" , e.getMessage());
            return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("%s request has failed", applicationId)), HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
    } 
    
    @RequestMapping(value = "/submit", method = RequestMethod.GET)  
    @ResponseBody
    public ResponseEntity<UIResponse> submitRequest(Integer id, String name,Integer scopeId,Integer productId,Integer productPartId,Integer applicationId,String source,Integer bin,String cardId,Integer businessFunctionId,Integer maxRestageCount, String stageScriptStartDate,String stageScriptEndDate)
    {
        try
        {            
            FilterCriteria filter =new FilterCriteria();
            filter.setId(id);
            filter.setName(name);
            filter.setScopeId(scopeId);
            filter.setProductId(productId);
            filter.setProductPartId(productPartId);
            filter.setApplicationId(applicationId);
            filter.setBusinessFunctionId(businessFunctionId);
            filter.setSource(source);
            filter.setBin(bin);
            filter.setCardId(cardId);
            
            FilterCriteria vFilter=isValidFilter(filter);
            
            Long ssStartDate=convertDateFromString2Long(stageScriptStartDate,false);
            
            Long ssEndDate=convertDateFromString2Long(stageScriptEndDate,true);
                                
            StageScript submitObject = stageScriptService.submit(vFilter, maxRestageCount,ssStartDate,ssEndDate);
            
            //int totalCount=123;//queryResult.getTotalCount();

            if(submitObject!=null && submitObject.getIsDuplicate())
            {
                return new ResponseEntity<UIResponse>(new UIResponse(true,String.format("Duplicate request found and cannot be Submitted.")), HttpStatus.OK);
            }
            else if(submitObject!=null && submitObject.getTrackId()!=null)
            {
                return new ResponseEntity<UIResponse>(new UIResponse(true,String.format("Request Successfully Submitted. Reference is %s", submitObject.getTrackId())), HttpStatus.OK);
            }
            else
            {
               return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("There are no valid scriptable application instances found")), HttpStatus.OK);
            }   
            
        }
        catch (Exception ex)
        {
            logger.error("Request Sumbmission failed:" + ex.getMessage(),ex);
            return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("Request Sumbmission failed. %s", ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
    } 
        
        
        /////////////////////////////////////////////////////////
        
        
        @RequestMapping(method = RequestMethod.GET)
        @ResponseBody
	public UIRecords<UIStageScript> getList(Integer limit, Integer start, Integer page, String sort, String dir,Integer id, String name,Integer scopeId) 
        {
            FilterCriteria filter =new FilterCriteria();
            filter.setId(id);
            filter.setName(name);
            filter.setScopeId(scopeId);
            PagingCriteria  pagingCriteria=new PagingCriteria(start,limit,page,sort,dir);

            QueryResult<StageScript> queryResult = stageScriptService.getList(filter, pagingCriteria);
            
            UIRecords<UIStageScript> uiResords=new UIRecords<UIStageScript>(queryResult.getTotalCount(), RECORD_NAME, UIStageScript.convertList(queryResult.getResultsList()));
            
            return uiResords;
	}
        	
		
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
        @ResponseBody
	public UIStageScript getById(@PathVariable Integer id) 
        {
		return new UIStageScript(stageScriptService.getById(id));
	}
	
	/* same as above method, but is mapped to
	 * /api/product?id= rather than /api/product/{id}
	 */
	@RequestMapping(params="id", method = RequestMethod.GET)
	@ResponseBody
	public UIStageScript getByIdFromParam(@RequestParam("id") Integer id) 
        {
		return new UIStageScript(stageScriptService.getById(id));
	}
        
        
        
        ////////////////////add,save & update are not being implemented////////////////////////////////////////////////////
	
	/**
	 * Saves new person. Spring automatically binds the name
	 * and age parameters in the request to the person argument
	 * @param product
	 * @return String indicating success or failure of save
	 */     
        @RequestMapping(method = RequestMethod.POST)
        public ResponseEntity<UIResponse> add(@RequestBody UIStageScript uiObject)
        {
            //Product product = uiProduct.toProduct();
            try
            {
                //stageScriptService.saveProduct(product);
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
        public ResponseEntity<UIResponse> update(@PathVariable("id") String Id, @RequestBody UIStageScript uiObject)
        {
            try
            {
                //stageScriptService.updateProduct(uiProduct.toProduct());
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
                //stageScriptService.deleteProduct(id);
                HttpHeaders responseHeaders = new HttpHeaders();//getModifiedDataHeader();
                return new ResponseEntity<UIResponse>( new UIResponse(true,String.format("Id %s successfully deleted", id)), responseHeaders, HttpStatus.OK);            
            }
            catch(Exception e)
            {
                return new ResponseEntity<UIResponse>(new UIResponse(false,String.format("Id %s delete is failed", id)), HttpStatus.INTERNAL_SERVER_ERROR);            
            }

        }
        
        
        private Long convertDateFromString2Long(String stringDate,boolean isEndDate)
        {
           Long longDate=null;
            try
            {               
               if(isEndDate)
               {
                   longDate=AffinaEspUtils.getEndDateTime(stringDate,AffinaEspUtils.DATE_FORMAT_YYYY_MM_DD);
               }
               else
               {
                   longDate=AffinaEspUtils.getStartDateTime(stringDate,AffinaEspUtils.DATE_FORMAT_YYYY_MM_DD);
               }
            }
            catch (AffinaEspUtilException ex) 
            {
               logger.warn(ex.getMessage());
            }
            catch(ParseException ex)
            {
                logger.warn(ex.getMessage());
            } 
            return longDate;
        }  
        
        
        private FilterCriteria isValidFilter(FilterCriteria filter) throws EspRestServiceValidationException
        {
            if(filter==null)
            {
                throw new EspRestServiceValidationException("FilterCriteria must be defined");
            }            
            if(filter.getScopeId()==null)
            {
                throw new EspRestServiceValidationException("Scope must be defined");
            }
            
            if(filter.getApplicationId()==null)
            {
                throw new EspRestServiceValidationException("Application must be defined");
            }
            
            if(filter.getBusinessFunctionId()==null)
            {
                throw new EspRestServiceValidationException("Business Function must be defined");
            }  
            
            if(filter.getStatus()==null)
            {
                String defaultStatus=AffinaEspUtils.STATUS_INITIAL;
                logger.warn("Status is not defined and set to ",defaultStatus);
                filter.setStatus(defaultStatus);
            }   
                        
            if(filter.getSource()==null || filter.getSource().trim().length()==0)
            {
                String defaultSource=ESPStageScriptFilter.sources.REST_SERVICE.name();
                logger.warn("Source is not defined and set to ",defaultSource);
                filter.setSource(defaultSource);
            }
            
        return filter;
        }
        
}
