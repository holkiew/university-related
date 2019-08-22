package application.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by DZONI on 09.11.2016.
 */
@Configuration
@PropertySource("classpath:web.properties")
public class WebPropertiesLoader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${contextPath}")
    private String CONTEXT_PATH;

    @Value("${maximumTransferredAuctions}")
    private String MAX_DISPLAYED_AUCTIONS;

    @Value("${debugInserts}")
    private String DEBUG_INSERTS;

    @Bean
    public static WebPropertiesLoader getNewBean() {
        return new WebPropertiesLoader();
    }

    public String getCONTEXT_PATH() {
        return CONTEXT_PATH;
    }

    public int getMAX_DISPLAYED_AUCTIONS(){
        try{
            return Integer.parseInt(MAX_DISPLAYED_AUCTIONS);
        } catch (NumberFormatException e){
            logger.error("MAX_DISPLAYED_AUCTIONS - maximumDisplayedAuctions property, parsing error, returning default 50");
            return 50;
        }
    }

    public boolean getDEBUG_INSERTS() {
        return Boolean.parseBoolean(DEBUG_INSERTS);
    }
}
