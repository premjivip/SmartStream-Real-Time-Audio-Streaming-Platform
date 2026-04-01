
@Configuration
public class Upload implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
	}

}
