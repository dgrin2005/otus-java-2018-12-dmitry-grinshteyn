package ru.otus.DBService;

import org.hibernate.cfg.Configuration;

import java.util.Arrays;
import java.util.List;

class ConfigurationBuilder {

    private String configFile;
    private List<Class> annotatedClasses;

    static ConfigurationBuilder builder() {
        return new ConfigurationBuilder();
    }

    ConfigurationBuilder configFile(String configFile) {
        this.configFile = configFile;
        return this;
    }

    ConfigurationBuilder annotatedClasses(Class... classes) {
        this.annotatedClasses = Arrays.asList(classes);
        return this;
    }

    Configuration build() {
        Configuration configuration = new Configuration().configure(configFile);
        for(Class t : annotatedClasses) {
            configuration.addAnnotatedClass(t);
        }
        return configuration;
    }
}
