package org.vhmml.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface FeedbackService {
	public void sendCorrections(String correctionType, List<String> categories, String corrections, String contactEmail, String hmmlProjectNumber) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
