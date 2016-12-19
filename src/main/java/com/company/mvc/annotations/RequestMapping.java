package com.company.mvc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.company.mvc.enums.ContentType;
import com.company.mvc.enums.RequestMethod;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

	String pattern() default "";

	RequestMethod method() default RequestMethod.GET;

	Class<?> payLoad() default String.class;

	ContentType contentType() default ContentType.TEXT_HTML;

}
