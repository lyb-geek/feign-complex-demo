package com.github.geek.lyb.feign.spring.registrar;


import com.github.geek.lyb.feign.spring.annotation.CustomFeignClient;
import com.github.geek.lyb.feign.spring.annotation.EnableCustomFeignClients;
import com.github.geek.lyb.feign.spring.bean.FeignClientFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class FeignClientScannerRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {
	private static Logger logger = LoggerFactory.getLogger(FeignClientScannerRegistrar.class);


	private ResourceLoader resourceLoader;

	private ClassLoader classLoader;

	private Environment environment;

	public FeignClientScannerRegistrar() {
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}


  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

	  Set<String> basePackages = getBasePackages(importingClassMetadata);
	  ClassPathScanningCandidateComponentProvider scanner = getScanner();
	  scanner.setResourceLoader(this.resourceLoader);
	  scanner.addIncludeFilter(new AnnotationTypeFilter(CustomFeignClient.class));
    

	  for (String basePackage : basePackages) {
		  Set<BeanDefinition> candidateComponents = scanner
				  .findCandidateComponents(basePackage);
		  for (BeanDefinition candidateComponent : candidateComponents) {
			  if (candidateComponent instanceof AnnotatedBeanDefinition) {
				  AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
				  AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
				  registerFeignClient(registry,annotationMetadata);
			  }
		  }
	  }

  }
  
  
  protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
		Map<String, Object> attributes = importingClassMetadata
				.getAnnotationAttributes(EnableCustomFeignClients.class.getCanonicalName());

		Set<String> basePackages = new HashSet<>();
		
		for (String pkg : (String[]) attributes.get("basePackages")) {
			if (StringUtils.hasText(pkg)) {
				basePackages.add(pkg);
			}
		}
		for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
			basePackages.add(ClassUtils.getPackageName(clazz));
		}

		if (basePackages.isEmpty()) {
			basePackages.add(
					ClassUtils.getPackageName(importingClassMetadata.getClassName()));
		}
		return basePackages;
	}


	protected ClassPathScanningCandidateComponentProvider getScanner() {
		return new ClassPathScanningCandidateComponentProvider(false, this.environment) {

			@Override
			protected boolean isCandidateComponent(
					AnnotatedBeanDefinition beanDefinition) {
				if (beanDefinition.getMetadata().isIndependent()) {
					if (beanDefinition.getMetadata().isInterface()
							&& beanDefinition.getMetadata()
							.getInterfaceNames().length == 1
							&& Annotation.class.getName().equals(beanDefinition
							.getMetadata().getInterfaceNames()[0])) {
						try {
							Class<?> target = ClassUtils.forName(
									beanDefinition.getMetadata().getClassName(),
									FeignClientScannerRegistrar.this.classLoader);
							return !target.isAnnotation();
						}
						catch (Exception ex) {
							this.logger.error(
									"Could not load target class: "
											+ beanDefinition.getMetadata().getClassName(),
									ex);

						}
					}
					return true;
				}
				return false;

			}
		};
	}



	private void registerFeignClient(BeanDefinitionRegistry registry,
                                    AnnotationMetadata annotationMetadata) {
		try {
			String className = annotationMetadata.getClassName();
			Class<?> beanClazz = Class.forName(className);
			if(!beanClazz.isAnnotationPresent(CustomFeignClient.class)){
				throw new RuntimeException("CustomFeignClient is required!");
			}
			CustomFeignClient customFeignClient = beanClazz.getAnnotation(CustomFeignClient.class);
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
			GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
			definition.getPropertyValues().add("feignClzType", beanClazz);
			definition.getPropertyValues().add("url", customFeignClient.url());
			definition.setBeanClass(FeignClientFactoryBean.class);
			definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
			registry.registerBeanDefinition(beanClazz.getSimpleName(), definition);
			;
		} catch (ClassNotFoundException e) {
			this.logger.error(
					"Could not register target class: "
							+ annotationMetadata.getClassName(),
					e);
		}
	}





}
