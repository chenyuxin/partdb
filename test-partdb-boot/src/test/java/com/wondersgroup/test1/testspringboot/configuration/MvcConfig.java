package com.wondersgroup.test1.testspringboot.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.wondersgroup.test1.testspringboot.interceptor.LoginInterceptor;


//@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@SuppressWarnings("deprecation")
	@Bean(name="fastjson")
	public FastJsonHttpMessageConverter proFastJson(){
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		fastJsonHttpMessageConverter.setFeatures(SerializerFeature.WriteMapNullValue,SerializerFeature.WriteDateUseDateFormat);
		return fastJsonHttpMessageConverter;
	}
	
	
	/**
	<!-- 权限的拦截	 
	 -->
	<mvc:interceptors>
		<mvc:interceptor>
			<mvc:mapping path="/**"/>
			<!-- <mvc:exclude-mapping path="/images/**"/> -->
			<mvc:exclude-mapping path="/login/**"/>
			<mvc:exclude-mapping path="/test/**"/>
			<mvc:exclude-mapping path="/util_controller/**"/>
			<!-- 
			 -->
			<mvc:exclude-mapping path="/js/**"/>
			<mvc:exclude-mapping path="/image/**"/>
			<bean class="cn.ittx.interceptor.LoginInterceptor"/>
		</mvc:interceptor>
	</mvc:interceptors>
	*/
	@Bean
	public LoginInterceptor getLoginInterceptor(){
		return new LoginInterceptor();
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
		List<String> patterns = new ArrayList<>();
		patterns.add("/js/**");
		patterns.add("/image/**");
		patterns.add("/login/**");
		patterns.add("/util_controller/**");
		patterns.add("/error/**");
        registry.addInterceptor(this.getLoginInterceptor()).addPathPatterns("/**").excludePathPatterns(patterns);
        //super.addInterceptors(registry);
        this.addInterceptors(registry);
	}
	
	/*视图处理器
	<bean class="org.springframework.web.servlet.view.ContentNegotiatingViewResolver" id="viewResolver">
		<property name="viewResolvers">
			<list>
				<bean id="htmlViewResolver" class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
					<property name="order" value="0"></property>
					<property name="prefix" value="/WEB-INF/admin/"></property>
					<property name="suffix" value=".html"></property>
					<property name="contentType" value="text/html;charset=UTF-8"></property>
					<property name="viewNames" value="*.html"></property>
				</bean>			
				<bean id="jspViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
					<property name="order" value="1"></property>
					<property name="prefix" value="/WEB-INF/admin/"></property>
					<property name="suffix" value=".jsp"></property>
				</bean>	
			</list>
		</property>
	</bean>
	 */
	@Bean(name="htmlViewResolver")
	public FreeMarkerViewResolver freeMarkerViewResolver(){
		FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
		freeMarkerViewResolver.setOrder(0);
		freeMarkerViewResolver.setPrefix("/views/");
		freeMarkerViewResolver.setSuffix(".html");
		freeMarkerViewResolver.setContentType("text/html;charset=UTF-8");
		freeMarkerViewResolver.setViewNames("*.html");
		return freeMarkerViewResolver;
	}
	
	@Bean(name="jspViewResolver")
	public InternalResourceViewResolver internalResourceViewResolver(){
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setOrder(1);
		internalResourceViewResolver.setPrefix("/views/");
		internalResourceViewResolver.setSuffix(".jsp");
		return internalResourceViewResolver;
	}
	
	@Bean(name="viewResolver")
	@ConditionalOnBean({ViewResolver.class})
    @ConditionalOnMissingBean(
        name = {"viewResolver"},
        value = {ContentNegotiatingViewResolver.class}
    )
	public ContentNegotiatingViewResolver contentNegotiatingViewResolver(BeanFactory beanFactory){
		ContentNegotiatingViewResolver contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();
		contentNegotiatingViewResolver.setContentNegotiationManager((ContentNegotiationManager)beanFactory.getBean(ContentNegotiationManager.class));
		
//		List<View> defaultViews = new ArrayList<View>();
//		//defaultViews.add();
//		contentNegotiatingViewResolver.setDefaultViews(defaultViews);
		
		List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();
		viewResolvers.add(this.freeMarkerViewResolver());
		viewResolvers.add(this.internalResourceViewResolver());
		contentNegotiatingViewResolver.setViewResolvers(viewResolvers);
		
		return contentNegotiatingViewResolver;
	}
	
    
}


