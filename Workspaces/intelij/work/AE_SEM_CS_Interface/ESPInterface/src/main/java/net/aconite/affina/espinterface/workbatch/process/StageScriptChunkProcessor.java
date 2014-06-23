package net.aconite.affina.espinterface.workbatch.process;

import java.util.ArrayList;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.aconite.affina.espinterface.constants.EspConstant;
import net.aconite.affina.espinterface.dao.StageScriptDetailDao;
import net.aconite.affina.espinterface.jms.IMessageGenerator;
import net.aconite.affina.espinterface.jms.MessageGenerator;
import net.aconite.affina.espinterface.dao.model.StageScriptChunker;
import net.aconite.affina.espinterface.xmlmapping.sem.StageScriptRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Splitter;

/**
 * User: wakkir Date: 08/03/14 Time: 03:01
 */
public class StageScriptChunkProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(StageScriptChunkProcessor.class.getName());

    //@Autowired
    IMessageGenerator  messageGenerator = new MessageGenerator();
    
    @Splitter
    public List<Message> process(Message inMessage)
    {
        // At info level, the data recorded shall be limited to the message type
        //   and its identifier (tracking reference or service instance).
        // At debug level, the complete message shall be recorded.

        MessageHeaders inHeaders = inMessage.getHeaders();
        Object inPayload = inMessage.getPayload();

        logger.debug("==============================================");
        logger.debug("EndProcessor processing started...");

        Collection col = (Collection) inPayload;
        logger.debug("Size : {}" , col.size());
        Iterator it = col.iterator();
        
        List<Message> outMessages = new ArrayList<Message>();
        
        while (it.hasNext())
        {
            Object obj=it.next();
                                
            //insert values into db and send stagescriptrequest
            if(obj instanceof StageScriptChunker)
            {
                StageScriptChunker chunkerObj = (StageScriptChunker)obj ;
                logger.debug(chunkerObj.toString());
                
                StageScriptDetailDao stageScriptDetailDao=new StageScriptDetailDao();
        
                StageScriptRequest request=stageScriptDetailDao.add(chunkerObj);
                //System.out.println("request : "+request);
                
                Message<StageScriptRequest> outMessage = messageGenerator.<StageScriptRequest>generateMessage(inHeaders, request,EspConstant.STAGE_SCRIPT_REQUEST);
                    
                outMessages.add(outMessage);

            }
        }

        logger.debug("EndProcessor processing ended...");
        
        return outMessages;

    }
}
