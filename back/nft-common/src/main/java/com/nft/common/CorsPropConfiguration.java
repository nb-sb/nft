package com.nft.common;//package com.example.demo.configuration;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.util.pattern.PathPatternParser;
//
//import java.util.List;
//
///**
// * 该类用于设置跨域
// */
//@Configuration
//public class CorsPropConfiguration {
//
//
//    @Bean
//    public FilterRegistrationBean coreWebFilter(CorsProperties corsProperties) {
//        CorsConfiguration config = new CorsConfiguration();
//        // * 号表示匹配任意的
//        config.setAllowedMethods(corsProperties.getControlAllowMethods());
//        config.setAllowedOrigins(corsProperties.getControlAllowOrigin());
//        config.setAllowedHeaders(corsProperties.getControlAllowHeaders());
//        PathPatternParser patternParser = new PathPatternParser();
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(patternParser);
//        // ** 代表所有
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(0);
//        return bean;
//    }
//
//    @Data
//    @Configuration
//    @ConfigurationProperties(prefix = "cors")
//    public static class CorsProperties {
//        private List<String> controlAllowHeaders;
//        private List<String> controlAllowMethods;
//        private List<String> controlAllowOrigin;
//    }
//}
