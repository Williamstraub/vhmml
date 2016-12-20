package org.vhmml.service;

import java.util.List;
import java.util.Map;

import org.vhmml.entity.ConfigurationValue;

public interface ApplicationConfigService {
	
	public enum Property {
		DEFAULT_PAGE_SIZE("default.page.size"),
		ZOTERO_USER_ID("zotero.user.id"),
		ZOTERO_AUTH_KEY("zotero.auth.key"),
		WEB_SERVICE_PROXY_SERVER("web.service.proxy.server"),
		WEB_SERVICE_PROXY_PORT("web.service.proxy.port"),
		SYSTEMS_LIB_EMAIL_LIST("vhmml.systems.librarian.email"),
		USER_MESSAGE_ELASTIC_SEARCH_UNAVAILABLE("user.message.elastic.search.unavailable"),
		USER_MESSAGE_IMAGE_SERVER_UNAVAILABLE("user.message.image.server.unavailable"),
		ADMIN_EMAIL_ELASTIC_SEARCH_UNAVAILABLE("admin.email.elastic.search.unavailable"),
		ADMIN_EMAIL_IMAGE_SERVER_UNAVAILABLE("admin.email.image.server.unavailable"),
		VHMML_EMAIL_SIGNATURE("vhmml.email.signature"),		
		MAX_THUMBNAIL_HEIGHT("max.thumbnail.height"),		
		PERMALINK_URL("permalink.url");		
		
		Property(String key) {
			this.key = key;
		}
		
		private String key; 
		
		public String getKey() {
			return this.key;
		}
	};
	
	public List<ConfigurationValue> getAll();
	public Map<String, ConfigurationValue> getAllAsMap();
	public String getValue(Property key); 
	public String getValue(Property property, Object... arguments);
	public List<String> getValues(Property key);
	public Integer getIntValue(Property key);
    public void setValue(String key, String value);
    public void setValues(List<ConfigurationValue> configValues);
}
