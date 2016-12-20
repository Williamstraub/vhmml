package org.vhmml.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.vhmml.service.ApplicationConfigService;
import org.vhmml.service.ApplicationConfigService.Property;

@Component
public class PagingUtil {

	@Autowired
	private ApplicationConfigService configService;
	
	public PagingUtil() {
		super();
	}

	public Pageable getDefaultPageable(String[] sortPropertyNames) {
		List<Sort.Order> sortOrders = new ArrayList<Sort.Order>();
		Integer defaultPageSize = configService.getIntValue(Property.DEFAULT_PAGE_SIZE);
		
		
		if(sortPropertyNames != null) {
			for(String propertyName : sortPropertyNames) {
				sortOrders.add(new Sort.Order(Direction.ASC, propertyName));			
			}
		}			
		
		return new PageRequest(0, defaultPageSize, new Sort(sortOrders));
	}
	
	public String getSortString(Pageable pageable) {
		StringBuilder sort = new StringBuilder();
		
		if(pageable.getSort() != null) {
			List<Sort.Order> sortOrders = IteratorUtils.toList(pageable.getSort().iterator());
			
			// a limitation of Spring pagination is that it can only deserialize 1 sort request parameter with a specified sort order
			// because it parses the "sort by" fields as a comma separated list in the form of "field1, field2, field3, sort direction".
			// Only the last value is inspected to see if it's a sort order, so things like "author asc, title desc, date asc" don't 
			// work because in order for Spring to deserialize the sort attribute of a pageable, the sort string can only have the sort
			// direction on the value of the sort parameter - the sort direction is automatically appended to each field as "ASC". 
			// We could get around this by writing a custom PageableHandlerMethodArgumentResolver that is more intelligent about 
			// parsing the sort request parameters if this limitation becomes too much of an issue in the future.
			for(int i = 0; i < sortOrders.size(); i++) {
				Sort.Order sortOrder = sortOrders.get(i);
				if(i > 0) {
					sort.append(",");
				}			
				
				sort.append(sortOrder.getProperty());
								
				if(i == sortOrders.size() - 1) {
					sort.append(",").append(sortOrder.getDirection().toString());
				}
			}			
		}
		
		return sort.toString();
	}
}
