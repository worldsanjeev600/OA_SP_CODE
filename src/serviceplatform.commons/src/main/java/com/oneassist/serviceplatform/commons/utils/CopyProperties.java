package com.oneassist.serviceplatform.commons.utils;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PartnerStageStatus;

@Component
public class CopyProperties {

	public static void copyNonNullProperties(Object source, Object destination){
		BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String[] getNullPropertyNames (Object source) {

		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set emptyNames = new HashSet();
		if(source instanceof PartnerStageStatus){}
		else{
			for(java.beans.PropertyDescriptor pd : pds) {
				//check if value of this property is null then add it to the collection which will not get copied..
				Object srcValue = src.getPropertyValue(pd.getName());
				if (srcValue == null || "".equals(srcValue.toString().trim()) || srcValue.toString().equalsIgnoreCase("null")) emptyNames.add(pd.getName());
			} 
		}
		
		String[] result = new String[emptyNames.size()];
	
		return (String[]) emptyNames.toArray(result);
	}
}
