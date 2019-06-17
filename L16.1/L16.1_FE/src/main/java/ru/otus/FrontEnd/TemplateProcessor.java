package ru.otus.frontend;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import static ru.otus.frontend.FrontEndUtilites.TEMPLATE_PATH_RESOURCE;

class TemplateProcessor {
    private final Configuration configuration;

    TemplateProcessor() {
        configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setClassForTemplateLoading(this.getClass(), TEMPLATE_PATH_RESOURCE);
        configuration.setDefaultEncoding("UTF-8");
    }

    String getPage(String filename, Map<String, Object> data) throws IOException {
        try (Writer writer = new StringWriter();) {
            Template template = configuration.getTemplate(filename);
            template.process(data, writer);
            return writer.toString();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}
