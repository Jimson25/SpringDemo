#### 一、spring-web 
1. 静态资源访问
    - 静态资源目录
        ```text
        By default, Spring Boot serves static content from a directory called `/static` (or `/public` or `/resources` or `/META-INF/resources`) in the classpath or from the root of the `ServletContext`.
        ```
       - 当我们把静态资源放在上面的目录中时，在请求时会默认放行，即可以通过`当前项目根路径/ + 静态资源名`访问
       - 当一个请求进来时，会默认先查找controller中是否有对应的接口，如果没有就去静态资源目录中查找，再找不到就404
       - 这里可以直接访问是因为默认的静态映射是/**    
    
    -  静态资源访问前缀
        - 默认情况下请求静态资源是没有前缀的，可以使用下面配置修改
            ```yaml
            spring:
            mvc:
              static-path-pattern: /res/**
            ```
        - 前面讲到spring默认配置了静态资源路径，我们可以使用下面代码修改默认路径
            ```yaml
            spring:
            web:
              resources:
                static-locations: [classpath:/pic/]
            ```
        - 当我们修改了上面的路径后，再访问后的路径为： `localhost:8080/pic/bug.jpg`
    
    - webjar映射
        - 可以通过后台配置访问js等静态文件，添加如下依赖
            ```xml
            <dependency>
              <groupId>org.webjars</groupId>
              <artifactId>jquery</artifactId>
              <version>3.5.1</version>
            </dependency>       
            ```
        - 通过`http://localhost:8080/webjars/jquery/3.5.1/jquery.js` 访问
        - 上面路径从 `jquery/...`后面开始是根据导入的包路径决定的
   
2. 自定义欢迎页
    - spring-boot 默认欢迎页在静态资源路径下的 `index.html`
    - 自定义欢迎页可以配置静态资源路径，但是不可以配置静态资源的访问前缀

3. 自定义默认Favicon
   
    - 将静态资源`Favicon.ico` 放在静态资源目录即可
    
4. spring-boot web静态资源自动配置

    - spring-boot中web默认配置类为 `WebMVCAutoConfiguration`

      ```java
      @Configuration(proxyBeanMethods = false)	//这是一个配置类
      @ConditionalOnWebApplication(type = Type.SERVLET)	//web应用环境生效
      @ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
      @ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
      @AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
      @AutoConfigureAfter({ DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
      		ValidationAutoConfiguration.class })
      public class WebMvcAutoConfiguration {
          
      }
      ```

      在这个配置类里包含了多个内部类，其中有一个 `EnableWebMvcConfiguration`

      ```java
      @Configuration(proxyBeanMethods = false)
      @EnableConfigurationProperties(WebProperties.class)
      public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration implements ResourceLoaderAware {}
      ```
      
      在这个内部类中只存在一个构造方法如下, 这里有参构造的参数会自动从容器中查找
      
      ```java
// resourceProperties：绑定一个properties == spring.resources
      // mvcProperties：绑定一个properties = spring.mvc
// webProperties：绑定一个properties = spring.web
      // mvcRegistrationsProvider：
      // resourceHandlerRegistrationCustomizerProvider：找到 资源处理器的自定义器。=========
// beanFactory：	spring的bean工厂
public EnableWebMvcConfiguration(
      				org.springframework.boot.autoconfigure.web.ResourceProperties resourceProperties,
      				WebMvcProperties mvcProperties, WebProperties webProperties,
      				ObjectProvider<WebMvcRegistrations> mvcRegistrationsProvider,
      				ObjectProvider<ResourceHandlerRegistrationCustomizer> resourceHandlerRegistrationCustomizerProvider,
      				ListableBeanFactory beanFactory) {
      			this.resourceProperties = resourceProperties.hasBeenCustomized() ? resourceProperties
      					: webProperties.getResources();
      			this.mvcProperties = mvcProperties;
      			this.webProperties = webProperties;
      			this.mvcRegistrations = mvcRegistrationsProvider.getIfUnique();
      			this.resourceHandlerRegistrationCustomizer = resourceHandlerRegistrationCustomizerProvider.getIfAvailable();
      			this.beanFactory = beanFactory;
      }
      ```
      
      在上面的静态内部类里面有一个方法如下
      
      ```java
      @Override
      protected void addResourceHandlers(ResourceHandlerRegistry registry) {
      	super.addResourceHandlers(registry);
          // 判断WebProperties中 addMappings 属性
          // 如果属性为false就不添加任何静态资源配置
      	if (!this.resourceProperties.isAddMappings()) {
      		logger.debug("Default resource handling disabled");
      		return;
      	}
          // 设置默认的属性
      	ServletContext servletContext = getServletContext();
      	addResourceHandler(registry, "/webjars/**", "classpath:/META-INF/resources/webjars/");
      	addResourceHandler(registry, this.mvcProperties.getStaticPathPattern(), (registration) -> {
      		registration.addResourceLocations(this.resourceProperties.getStaticLocations());
      		if (servletContext != null) {
      			registration.addResourceLocations(new ServletContextResource(servletContext, SERVLET_LOCATION));
      		}
      	});
      }
      ```
      
      

