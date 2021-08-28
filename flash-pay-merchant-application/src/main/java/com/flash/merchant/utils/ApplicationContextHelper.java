package com.flash.merchant.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public ApplicationContextHelper() {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHelper.applicationContext = applicationContext;
	}

	public static Object getBean(String beanName) {
		return applicationContext != null ? applicationContext.getBean(beanName) : null;
	}

	public static <T> T getBean(Class<T> c){
		return applicationContext != null ? applicationContext.getBean(c) : null;
	}
}

