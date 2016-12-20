package org.vhmml.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vhmml.entity.ConfigurationValue;
import org.vhmml.repository.ApplicationConfigurationRepository;

import com.google.common.collect.Lists;

@Service
public class ApplicationConfigServiceImpl implements ApplicationConfigService {
 
    @Autowired
    private ApplicationConfigurationRepository configurationRepository;
    
    public List<ConfigurationValue> getAll() {
    	return Lists.newArrayList(configurationRepository.findAll());
    }
    
    public Map<String, ConfigurationValue> getAllAsMap() {
    	Map<String, ConfigurationValue> configValuesMap = new HashMap<String, ConfigurationValue>();
    	List<ConfigurationValue> configValues = getAll();    	
		
		for(ConfigurationValue configValue : configValues) {
			configValuesMap.put(configValue.getKey(), configValue);
		}
		
		return configValuesMap;
    }
    
    public String getValue(Property property) {
    	String value = null;
    	ConfigurationValue configEntry = configurationRepository.findOne(property.getKey());
    	
    	if(configEntry != null) {
    		value = configEntry.getValue();
    	}
    	
    	return value;
    }
    
    public String getValue(Property property, Object... arguments) {
    	String value = getValue(property);
    	
    	if(value != null) {
    		value = MessageFormat.format(value, arguments);
    	}
    	
    	return value;
    }
    
    public Integer getIntValue(Property property) {
    	Integer value = null;
    	String stringValue = getValue(property);
    	
    	if(StringUtils.isNumeric(stringValue)) {
    		value = Integer.parseInt(stringValue);
    	}
    	
    	return value;
    }
    
    public List<String> getValues(Property property) {
    	List<String> values = new ArrayList<String>();
    	String stringValue = getValue(property);
    	
    	if(stringValue != null) {
    		values = Arrays.asList(stringValue.split(","));
    	}
    	
    	return values;
    }
    
    public void setValue(String key, String value) {
    	ConfigurationValue configEntry = configurationRepository.findOne(key);
    	
    	if(configEntry != null) {
    		configEntry.setValue(value);
    		configurationRepository.save(configEntry);
    	}    	
    }
    
    public void setValues(List<ConfigurationValue> configValues) {    	
    	configurationRepository.save(configValues);    	
    }
}