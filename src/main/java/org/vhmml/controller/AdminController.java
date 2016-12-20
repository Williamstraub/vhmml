package org.vhmml.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.controller.helper.UserControllerHelper;
import org.vhmml.controller.helper.UserControllerHelper.SaveAction;
import org.vhmml.dto.Report;
import org.vhmml.dto.UserSearchResult;
import org.vhmml.entity.ConfigurationValue;
import org.vhmml.entity.Role;
import org.vhmml.entity.User;
import org.vhmml.form.EmailUsersForm;
import org.vhmml.form.RegistrationForm;
import org.vhmml.form.ReportForm;
import org.vhmml.listener.AppStartupListener;
import org.vhmml.service.ApplicationConfigService;
import org.vhmml.service.ElasticSearchService;
import org.vhmml.service.ReportService;
import org.vhmml.service.UserService;
import org.vhmml.service.UserService.SearchType;
import org.vhmml.web.VhmmlMessage;
import org.vhmml.web.VhmmlMessage.Severity;
import org.vhmml.web.VhmmlMessageUtil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseValidationController {
	
	private static final Logger LOG = Logger.getLogger(AdminController.class);
	
	public static final String VIEW_ADMIN_HOME = "admin/home";
	public static final String VIEW_USER_ADMIN = "admin/users";
	public static final String VIEW_CONFIG_ADMIN = "admin/config";
	public static final String VIEW_GLOBAL_MESSAGE_ADMIN = "admin/globalMessages";
	public static final String VIEW_GLOBAL_EMAIL_USERS = "admin/emailUsers";	
	public static final String VIEW_REPORTS = "admin/reports";	
	public static final String VIEW_REPORT = "admin/report";	
	
	public static final String REQUEST_ATT_SEARCH_RESULT = "searchResult";
	public static final String REQUEST_ATT_ROLES = "roles";
	public static final String REQUEST_ATT_ROLES_JS = "rolesJs";	
	public static final String REQUEST_ATT_REPORT_TYPE = "reportType";	
	public static final String REQUEST_ATT_REPORT_TYPE_DISPLAY_NAME = "reportTypeDisplayName";	
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PagingUtil pagingUtil;
	
	@Autowired
	private ApplicationConfigService configService;
	
	@Autowired
	private UserControllerHelper userControllerHelper;
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private VhmmlMessageUtil messageUtil;	
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@RequestMapping({"", "/"})
	public ModelAndView home() {		
		return new ModelAndView(VIEW_ADMIN_HOME);
	}
	
	@ResponseBody
	@RequestMapping(value = "/reindex", method = RequestMethod.POST)
	public ResponseEntity<String> reindex() {
		ResponseEntity<String> response = new ResponseEntity<String>("Re-index completed successfully.", HttpStatus.OK);
		
		try {
			elasticSearchService.reindexAllSearchIndexes();
		} catch(Exception e) {
			LOG.error("Unexpected exception while trying to re-index all search data.", e);
			response = new ResponseEntity<String>("Re-index failed to complete. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		
		return response;
	}
		
	@RequestMapping("/users")
	public ModelAndView viewUserAdmin() throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_USER_ADMIN);
		Pageable defaultPageable = pagingUtil.getDefaultPageable(new String[]{"username"});
		UserSearchResult searchResult = userSearch(null, null, defaultPageable);
		modelAndView.addObject(REQUEST_ATT_SEARCH_RESULT, objectMapper.writeValueAsString(searchResult));
		modelAndView.addObject(REQUEST_ATT_ROLES, Role.Name.values());
		modelAndView.addObject(REQUEST_ATT_ROLES_JS, objectMapper.writeValueAsString(Role.Name.values()));
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/user/accountSettings/{userId}", method = RequestMethod.GET)
	public ModelAndView viewUserAccountSettings(@PathVariable("userId") Long userId) throws IOException {
		User user = userService.getUser(userId);
		return userControllerHelper.getAccountSettingsView(SaveAction.ADMIN_SAVE_ACCOUNT_SETTINGS, user);
	}
	
	@RequestMapping(value = "/user/accountSettings", method = RequestMethod.POST)
	public ModelAndView saveUserAccountSettings(@ModelAttribute @Valid RegistrationForm registrationForm, BindingResult result) throws IOException {
		return userControllerHelper.saveAccountSettings(registrationForm, result, VIEW_ADMIN_HOME);
	}
	
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public ModelAndView viewConfigAdmin() throws IOException {		
		return new ModelAndView(VIEW_CONFIG_ADMIN);
	}
	
	@ResponseBody
	@RequestMapping(value = "/config", method = RequestMethod.POST)
	public ResponseEntity<String> saveConfig(@RequestBody List<ConfigurationValue> configValues, HttpServletRequest request) throws IOException {
		ResponseEntity<String> response = null;
		
		try {		
			configService.setValues(configValues);			
			request.getServletContext().setAttribute(AppStartupListener.APP_ATT_CONFIG_VALUES, configService.getAllAsMap());
			response = new ResponseEntity<String>("Configuration values saved successfully.", HttpStatus.OK);			
		} catch(Exception e) {
			response = new ResponseEntity<String>("An unexpected error occurred while trying to save configuration values.", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		
		return response;
	}
	
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	public ModelAndView viewGlobalMessages() throws IOException {
		return new ModelAndView(VIEW_GLOBAL_MESSAGE_ADMIN);
	}	
	
	@RequestMapping(value = "/messages/add", method = RequestMethod.POST)
	public  ResponseEntity<Object> addGlobalMessage(@RequestParam String message, @RequestParam Severity severity) throws IOException {
		ResponseEntity<Object> response = null;
		
		try {
			String messageKey = UUID.randomUUID().toString();
			VhmmlMessage newMessage = new VhmmlMessage(messageKey, message, severity);
			messageUtil.addGlobalMessage(newMessage);
			response = new ResponseEntity<Object>(newMessage, HttpStatus.OK);
		} catch(Exception e) {
			LOG.error("Unexpected exception attempting to remove global messages.", e);
			response = new ResponseEntity<Object>("There was an unexpected error while trying to add the message. Please check the server logs for details or contact the system administrator.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	@RequestMapping(value = "/messages/remove", method = RequestMethod.POST)
	public  ResponseEntity<String> removeGlobalMessage(@RequestParam(required = false) String key) throws IOException {
		boolean allMessages = StringUtils.isEmpty(key);
		String successMessage = "Message" + (allMessages ? "s" : "") + " removed successfully.";
		ResponseEntity<String> response = new ResponseEntity<String>(successMessage, HttpStatus.OK);
		
		try {
			if(allMessages) {
				messageUtil.removeAllGlobalMessages();
			} else {
				messageUtil.removeGlobalMessage(key);
			}			
		} catch(Exception e) {
			LOG.error("Unexpected exception attempting to remove global messages.", e);
			String failureMessage = "There was an unexpected error while trying to remove the message" + (allMessages ? "s" : "") + ". Please check the server logs for details";
			response = new ResponseEntity<String>(failureMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/user/setEnabled", method = RequestMethod.POST)
	public ResponseEntity<String> setUserEnabled(Long userId, boolean enable) {
		ResponseEntity<String> response = new ResponseEntity<String>("User " + (enable ? "enabled" : "disabled") + " successfully.", HttpStatus.OK);
		
		try {
			if(enable) {
				userService.enableUser(userId);
			} else {
				userService.disableUser(userId);
			}			
		} catch(Exception e){
			String message = "An unexpected exception occurred attempting to " + (enable ? "enable" : "disable") + " the user, please contact the system administrator if the problem persists.";
			response = new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
			LOG.error(message, e);			
		}
		
		return response;
	}
	
	@RequestMapping(value = "/user/remove/{userId}", method = RequestMethod.POST)
	public ResponseEntity<String> removeUser(@PathVariable Long userId) {
		
		ResponseEntity<String> response = new ResponseEntity<>("User removed successfully.", HttpStatus.OK);

		try {
			userService.removeUser(userId);
		} catch (Exception e) {
			LOG.error("Unexpected error trying to remove user " + userId, e);
			response = new ResponseEntity<>("There was an unexpected error trying to remove the user. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);			
		}

		return response;							
	}
	
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Object> getUser(@PathVariable Long userId) {
		
		ResponseEntity<Object> response = new ResponseEntity<Object>("Unable to get user " + userId, HttpStatus.INTERNAL_SERVER_ERROR);

		try {			
			response = new ResponseEntity<Object>(userService.findById(userId), HttpStatus.OK);			
		} catch (Exception e) {
			LOG.error("Unexpected error trying to get user " + userId, e);						
		}

		return response;							
	}
	
	@ResponseBody
	@RequestMapping(value = "/user/toggleRole", method = RequestMethod.POST)
	public ResponseEntity<String> toggleRole(Long userId, Role.Name roleName, boolean grant) {
		ResponseEntity<String> response = new ResponseEntity<String>("Role " + roleName.getDisplayName() + (grant ? " granted" : " revoked") + " successfully.", HttpStatus.OK);
		
		try {			
			if(grant) {
				userService.grantRole(userId, roleName);
			} else {
				userService.revokeRole(userId, roleName);
			}			
		} catch(Exception e){
			String message = "An unexpected exception occurred attempting to " + (grant ? "grant" : "revoke") + " role " + roleName.getDisplayName() + ", please contact the system administrator if the problem persists.";
			response = new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
			LOG.error(message, e);			
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping("/users/search")
	public UserSearchResult userSearch(SearchType searchType, String searchString, Pageable pageable) {
		UserSearchResult searchResult = userService.search(searchType, searchString, pageable);
		List<User> users = searchResult != null ? searchResult.getUsers() : null;
		
		if(CollectionUtils.isNotEmpty(users)) {
			for(User user : users) {
				
				user.setFirstName(ESAPI.encoder().encodeForJavaScript(user.getFirstName()));
				user.setLastName(ESAPI.encoder().encodeForJavaScript(user.getLastName()));
				user.setInstitution(ESAPI.encoder().encodeForJavaScript(user.getInstitution()));
			}
		}
		
		return searchResult;
	}
	
	@RequestMapping(value = "/emailUsers", method = RequestMethod.GET)
	public ModelAndView viewEmailUsers(HttpServletRequest request) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_GLOBAL_EMAIL_USERS);				
		modelAndView.addObject(new EmailUsersForm());		
		
		return modelAndView;
	}	
	
	@ResponseBody
	@RequestMapping(value = "/emailUsers", method = RequestMethod.POST)
	public ResponseEntity<String> emailUsers(@ModelAttribute @Valid EmailUsersForm emailUsersForm, HttpServletRequest request) throws IOException {		
		ResponseEntity<String> response = null;
		
		try {		
			userService.sendMessageToActiveUsers(emailUsersForm.getSubject(), emailUsersForm.getMessage());						
			response = new ResponseEntity<String>("Message sent successfully.", HttpStatus.OK);			
		} catch(Exception e) {
			response = new ResponseEntity<String>("An unexpected error occurred while trying to send the message, if the problem persists please contact the system administrator.", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		
		return response;
	}
	
	@RequestMapping(value = "/reports", method = RequestMethod.GET)
	public ModelAndView reports(HttpServletRequest request) throws IOException {		
		return new ModelAndView(VIEW_REPORTS);
	}
	
	@RequestMapping(value = "/report/{reportType}", method = RequestMethod.GET)
	public ModelAndView report(@PathVariable String reportType) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_REPORT);
		modelAndView.addObject(REQUEST_ATT_REPORT_TYPE, reportType);
		modelAndView.addObject(REQUEST_ATT_REPORT_TYPE_DISPLAY_NAME, WordUtils.capitalize(reportType));
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value = "/runReport/{reportType}", method = RequestMethod.GET)
	public ResponseEntity<Object> runReport(@PathVariable Report.Type reportType, @ModelAttribute ReportForm reportForm, Pageable pageable) throws IOException {
		ResponseEntity<Object> response = null;
		
		try {
			Report report = reportService.runReport(reportType, reportForm.getReportParameters(), pageable);
			response = new ResponseEntity<Object>(report, HttpStatus.OK);
		} catch(Exception e) {
			response = new ResponseEntity<Object>("There was an unexpected error running the report. If the problem persists, please contact the System Administrator", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	@RequestMapping(value = "/exportReport/{reportType}", method = RequestMethod.GET)
	public void exportReport(@PathVariable Report.Type reportType, @RequestParam Map<String, String> reportParameters, Pageable pageable, HttpServletResponse response) throws IOException {

		// pageable instantiated by Spring will have a default page size of 20, even if we send up something like 0 or -1 and we don't
		// want exported reports to have a page size so we make a pageable that will bring back all rows and only apply sorting
		Pageable sortOnlyPageable = new PageRequest(0, Integer.MAX_VALUE, pageable.getSort());
		String reportCsv = reportService.getReportCSV(reportType, reportParameters, sortOnlyPageable);
		InputStream inputStream = IOUtils.toInputStream(reportCsv, "UTF-8");		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String downloadFilename = reportType.getDisplayName() + " Report " + format.format(new Date()) + ".csv";
		response.setContentType("text/csv");
		response.addHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
		IOUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();		
	}
	
	@ResponseBody
	@RequestMapping(value = "/emailUsers/validationRules", method = RequestMethod.GET)
	public Map<String, Map<String, Map<String, Object>>> getEmailUsersValidationRules() {		
		return super.getValidationRules(EmailUsersForm.class);
	}
}