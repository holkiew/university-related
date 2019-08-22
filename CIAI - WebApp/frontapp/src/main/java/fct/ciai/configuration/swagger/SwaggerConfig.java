package fct.ciai.configuration.swagger;

import fct.ciai.general.security.LoggedUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.AntPathMatcher;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2WebMvc
@Import(SpringDataRestConfiguration.class)
public class SwaggerConfig {
    private static final String API_KEY_NAME = "Token /auth/signin. Add Bearer before TOKEN";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(LoggedUser.class)
                .securitySchemes(getSecuritySchemes())
                .securityContexts(getSecurityContexts())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ArrayList<ApiKey> getSecuritySchemes() {
        return newArrayList(new ApiKey(API_KEY_NAME, "Authorization", "header"));
    }

    private ArrayList<SecurityContext> getSecurityContexts() {
        return newArrayList(SecurityContext.builder()
                .securityReferences(newArrayList(getSecurityReference()))
                .forPaths(getSecuredEndpointsPredicate())
                .build());
    }

    private Predicate<String> getSecuredEndpointsPredicate() {
        return path -> {
            AntPathMatcher matcher = new AntPathMatcher();
            return !matcher.match("/api/auth/**", path) || matcher.match("/api/auth/signupUserAdmin", path) || matcher.match("/api/auth/singupPartnerAdmin", path);
        };
    }

    private SecurityReference getSecurityReference() {
        return SecurityReference.builder()
                .reference(API_KEY_NAME)
                .scopes(getAuthorizationScopes())
                .build();
    }

    private AuthorizationScope[] getAuthorizationScopes() {
        List<AuthorizationScope> authScopeList = new ArrayList<>();
        authScopeList.add(new AuthorizationScopeBuilder().scope("global").description("token access needed").build());
        return authScopeList.toArray(new AuthorizationScope[0]);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ECMA")
                .description("ECMA application").version("0.1.0").build();
    }
}
