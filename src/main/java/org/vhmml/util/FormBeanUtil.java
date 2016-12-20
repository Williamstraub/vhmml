package org.vhmml.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;

public class FormBeanUtil {
	
	private static final Logger LOG = Logger.getLogger(FormBeanUtil.class);
	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();
	
	/**
	 * This method is used to filter out lists of objects that only contain elements with all null
	 * attributes. We get these type of lists from a form post because our form bean has lists of objects
	 * whose attributes get bound to separate form elements by Spring automatically. This saves a ton of 
	 * work in most cases, but when the user doesn't select anything for a list we end up with Spring
	 * binding a list with one object containing all empty attributes. For example, the ReadingRoomObjectForm
	 * contains a list of Subjects. To populate this list with one or more subjects, the user enters values for the
	 * various subject attributes into different fields. The html looks like this:
	 * 
	 * <code>
	 * 	<input id="subjects0.name" name="subjects[0].name" type="text">
	 * 	<input id="subjects0.authorityUriLC" name="subjects[0].authorityUriLC" type="text">
	 * 	<input id="subjects0.authorityUriVIAF" name="subjects[0].authorityUriVIAF" type="text">
	 * 
	 * 	<input id="subjects1.name" name="subjects[1].name" type="text">
	 * 	<input id="subjects1.authorityUriLC" name="subjects[1].authorityUriLC" type="text">
	 * 	<input id="subjects1.authorityUriVIAF" name="subjects[1].authorityUriVIAF" type="text">
	 * </code>
	 * 
	 * The user can add as many subjects as they want using add/remove buttons that generate this html.
	 * When the user fills out these fields and submits the form, Spring will automatically populate our 
	 * List<Subject> with subjects representing the user's choices.  However, if the user doesn't enter anything,
	 * the form is still submitted with empty values for subject[0].name, subject[0].authorityUriLC, and 
	 * subject[0].authorityUriVIAF. This makes spring instantiate an empty List and populate it with a Subject 
	 * that has null values for name, authorityUriLC and authorityUriVIAF. So we have this convenience method 
	 * that will check if a list contains only objects with empty attributes and return null if that is 
	 * the case so we're not trying to insert these objects into the database.
	 * 
	 * @param list
	 * @return
	 */
	public static List<?> getPopulatedList(List<?> list) {
		List<Object> populatedList = new ArrayList<Object>();		
		
		if(CollectionUtils.isNotEmpty(list)) {
			for(Object element : list) {				
				if(element == null) {
					continue;
				}
				
				if(!allFieldsAreNull(element)) {
					populatedList.add(element);
				}				
			}			
		}
				
		return populatedList;
	}
	
	private static boolean allFieldsAreNull(Object o) {
		boolean allFieldsAreNull = true;
		Field[] fields = FieldUtils.getAllFields(o.getClass());
		
		if(fields != null && fields.length > 0) {
			for (Field f : fields) {
				f.setAccessible(true);
				
				try {
					if(!isFieldEmpty(f.get(o))) {
						allFieldsAreNull = false;
						break;
					}			
				} catch (Exception e) {
					LOG.error("Error attempting to check if object has all null field values", e);
					throw new RuntimeException("Error attempting to check if object has all null field values");
				}
			}
		}		
		
		return allFieldsAreNull;
	}	

	public static boolean isFieldEmpty(Object fieldValue) {
		boolean isEmpty = fieldValue == null;
		
		if(!isEmpty) {
			Class<?> fieldClass = fieldValue.getClass();
			// if it's not a primitive wrapper, string or enum we need to check if it's an empty array or collection or an object with all null fields
			isEmpty = (!isWrapperOrString(fieldClass) && !fieldClass.isEnum()) && 
					(isFieldEmptyCollection(fieldClass, fieldValue) || allFieldsAreNull(fieldValue)); 
		}
		
		return isEmpty;
	}
	public static boolean isFieldEmptyCollection(Class<?> fieldClass, Object fieldValue) {
		return (fieldClass.isArray() || Collection.class.isAssignableFrom(fieldClass)) && org.springframework.util.ObjectUtils.isEmpty(fieldValue);
	}
	
    public static boolean isWrapperOrString(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class);
        return ret;
    }
}
