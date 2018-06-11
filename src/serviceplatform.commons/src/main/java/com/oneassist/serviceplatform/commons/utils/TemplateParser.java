package com.oneassist.serviceplatform.commons.utils;

import java.io.StringWriter;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TemplateParser {

    private final Logger log = Logger.getLogger(TemplateParser.class);

    private VelocityEngine getVelocityEngine(String basePath) {
        Properties props = new Properties();

        if (!StringUtils.isEmpty(basePath)) {
            props.setProperty("resource.loader", "file");
            props.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
            props.setProperty("file.resource.loader.path", basePath);
        } else {
            log.info("Using ClasspathResourceLoader");
            props.setProperty("resource.loader", "class");
            props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        }
        props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
        props.setProperty("runtime.log.logsystem.log4j.logger", "VelocityEngine");
        VelocityEngine engine = new VelocityEngine(props);
        engine.init(props);

        return engine;
    }

    public String parseTemplate(String templatePath, String templateName, Object model) {
        String parsedTemplate = null;
        VelocityEngine velocityEngine = getVelocityEngine(templatePath);
        Template template = velocityEngine.getTemplate(templateName);
        VelocityContext context = new VelocityContext();
        context.put("model", model);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        parsedTemplate = writer.toString();
        return parsedTemplate;
    }
}
