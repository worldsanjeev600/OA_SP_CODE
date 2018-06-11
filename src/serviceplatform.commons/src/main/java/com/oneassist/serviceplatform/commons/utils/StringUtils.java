package com.oneassist.serviceplatform.commons.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.constants.Constants;

@Component
public class StringUtils {

	public static String getEmptyIfNull(Object input) {

		return Objects.toString(input, "");
	}

	public static String concatenate(String[] strTokens, String delimiter) {

		String finalString = "";

		if (strTokens != null) {
			for (int index = 0; index < strTokens.length; index++) {
				String strToken = strTokens[index];
				if (strToken != null && !strToken.trim().equals("") && delimiter != null) {
					finalString = finalString + strToken.trim();
					if (index != strTokens.length - 1) {
						finalString = finalString + delimiter;
					}
				}
			}
		}

		return finalString;
	}

	public static String convertListToString(List list, String delimiter) {
		String str = null;

		if (CollectionUtils.isNotEmpty(list)) {
			str = concatenate((String[]) list.toArray(new String[0]), delimiter);
		}

		return str;

	}

	public static List<String> getListFromString(String str) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(str)) {
			return Arrays.asList(str.split(Constants.COMMA));
		} else
			return null;
	}

	public static String getAfterReplaceByString(String current, String replaceByName, String replaceByValue) {

		return current.replace(replaceByName, replaceByValue);

	}

}
