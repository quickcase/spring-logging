package app.quickcase.logging.json;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;

import java.util.Map;

public class CustomJsonLayout extends JsonLayout {

    public static final String LOGGER_ATTR_NAME = "logger_name";

    public CustomJsonLayout() {
        super();
        setIncludeMDC(false);
        setIncludeContextName(false);
        setIncludeLoggerName(false);
    }

    @Override
    protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {
        // Add custom logger attribute names
        add(LOGGER_ATTR_NAME, true, event.getLoggerName(), map);
    }

}
