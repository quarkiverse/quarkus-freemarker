package io.quarkiverse.freemarker.runtime;

import java.io.IOException;
import java.lang.annotation.Annotation;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.quarkiverse.freemarker.TemplatePath;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class FreemarkerTemplateProducer {

    @Inject
    Configuration configuration;

    @Produces
    @TemplatePath("ignored")
    Template getTemplate(InjectionPoint injectionPoint) throws IOException {
        TemplatePath path = null;
        for (Annotation qualifier : injectionPoint.getQualifiers()) {
            if (qualifier.annotationType().equals(TemplatePath.class)) {
                path = (TemplatePath) qualifier;
                break;
            }
        }
        if (path == null || path.value().isEmpty()) {
            throw new IllegalStateException("No template resource path specified");
        }
        return configuration.getTemplate(path.value());
    }
}
