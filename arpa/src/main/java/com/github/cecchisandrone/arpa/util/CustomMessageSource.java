package com.github.cecchisandrone.arpa.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class CustomMessageSource extends ReloadableResourceBundleMessageSource {

	public Map<String, String> getPropertiesWithPrefix(Locale locale, String prefix) {

		Map<String, String> props = new HashMap<String, String>();
		PropertiesHolder propertiesHolder = getMergedProperties(locale);
		Properties properties = propertiesHolder.getProperties();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			if (((String) entry.getKey()).startsWith(prefix)) {
				props.put(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		return props;
	}
}
