package org.vhmml.aop;

import java.net.ConnectException;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.vhmml.service.ApplicationConfigService;
import org.vhmml.service.ApplicationConfigService.Property;
import org.vhmml.service.EmailService;
import org.vhmml.web.VhmmlMessage;
import org.vhmml.web.VhmmlMessageUtil;

@Aspect
@Component
public class ExceptionInterceptor {
	
	private static final Logger LOG = Logger.getLogger(ExceptionInterceptor.class);
	
	@Autowired
	private ApplicationConfigService appConfigService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private VhmmlMessageUtil messageUtil;
	
	@Value("${elastic.search.host}")
	private String elasticSearchHost;
	
	@Value("${elastic.search.port}")
	private Integer elasticSearchPort;
	
	@Value("${image.server.host}")
	private String imageServerHost;
	
	@Value("${image.server.port}")
	private Integer imageServerPort;
		
	@Around("execution(* org.vhmml.service.ElasticSearchService.*(..))")
	public Object handleElasticSearchException(ProceedingJoinPoint  joinPoint) throws Throwable {
		Object returnVal = null;
		
		try {
			 returnVal = joinPoint.proceed();
		} catch(NoNodeAvailableException e) {
			LOG.error("NoNodeAvailableException while getting client connection to ElasticSearch on server " + elasticSearchHost  + ":" + elasticSearchPort + ", search will not be available.", e);
			String userMessage = appConfigService.getValue(Property.USER_MESSAGE_ELASTIC_SEARCH_UNAVAILABLE, elasticSearchHost, elasticSearchPort);
			VhmmlMessage message = new VhmmlMessage(UUID.randomUUID().toString(), userMessage, VhmmlMessage.Severity.ERROR);
			boolean newMessage = messageUtil.addGlobalMessage(message);
			
			if(newMessage) {
				List<String> addresses = appConfigService.getValues(Property.SYSTEMS_LIB_EMAIL_LIST);
				String mailMessage = appConfigService.getValue(Property.ADMIN_EMAIL_ELASTIC_SEARCH_UNAVAILABLE, elasticSearchHost, elasticSearchPort);
				emailService.sendMessages(addresses, "ElasticSearch is unavailable!", mailMessage);
			}
		} catch(Exception e) {						
			LOG.error("Unexpected exception in ElasticSearchService method: " + joinPoint.getSignature(), e);
			throw e;
		}
		
		return returnVal;				
	}
	
	@Around("execution(* org.vhmml.service.ImageService.*(..))")
	public Object handleImageServiceException(ProceedingJoinPoint  joinPoint) throws Throwable {
		Object returnVal = null;
		
		try {
			 returnVal = joinPoint.proceed();
		} catch(ConnectException e) {
			LOG.error("ConnectException while calling image service" + imageServerHost  + ":" + imageServerPort + ", images will not be available.", e);
			String userMessage = appConfigService.getValue(Property.USER_MESSAGE_IMAGE_SERVER_UNAVAILABLE, imageServerHost, imageServerPort);
			VhmmlMessage message = new VhmmlMessage(UUID.randomUUID().toString(), userMessage, VhmmlMessage.Severity.ERROR);
			boolean newMessage = messageUtil.addGlobalMessage(message);
			
			if(newMessage) {
				List<String> addresses = appConfigService.getValues(Property.SYSTEMS_LIB_EMAIL_LIST);
				String mailMessage = appConfigService.getValue(Property.ADMIN_EMAIL_IMAGE_SERVER_UNAVAILABLE, imageServerHost, imageServerPort);
				emailService.sendMessages(addresses, "CRITICAL vHMML image server is unavailable!", mailMessage);
			}
		} catch(Exception e) {						
			LOG.error("Unexpected exception in ImageService method: " + joinPoint.getSignature(), e);
			throw e;
		}
		
		return returnVal;				
	}
}
