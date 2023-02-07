package io.quarkiverse.freemarker.runtime;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import org.jboss.logging.Logger;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateExceptionHandler;
import io.quarkus.arc.DefaultBean;

@Singleton
public class FreemarkerConfigurationProducer {

    private static final Logger LOGGER = Logger.getLogger(FreemarkerConfigurationProducer.class);

    @DefaultBean
    @Singleton
    @Produces
    public Configuration configuration(FreemarkerBuildConfigSupport freemarkerBuildConfigSupport,
            FreemarkerConfig config)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

        List<TemplateLoader> loaders = new ArrayList<>();
        LOGGER.debugf("Adding build time locations: %s", freemarkerBuildConfigSupport.getResourcePaths());
        freemarkerBuildConfigSupport.getResourcePaths().stream()
                .map(this::newClassTemplateLoader)
                .forEach(loaders::add);

        LOGGER.debugf("Adding runtime locations: %s", config.filePaths.orElse(emptyList()));
        loaders.addAll(config.filePaths.orElse(emptyList()).stream().map(this::newFileTemplateLoader).collect(toList()));

        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders.toArray(new TemplateLoader[0]));
        cfg.setTemplateLoader(mtl);

        config.defaultEncoding.ifPresent(cfg::setDefaultEncoding);
        config.templateExceptionHandler.ifPresent(s -> cfg.setTemplateExceptionHandler(getExceptionHandler(s)));
        config.logTemplateExceptions.ifPresent(cfg::setLogTemplateExceptions);
        config.wrapUncheckedExceptions.ifPresent(cfg::setWrapUncheckedExceptions);
        config.fallbackOnNullLoopVariable.ifPresent(cfg::setFallbackOnNullLoopVariable);
        config.booleanFormat.ifPresent(cfg::setBooleanFormat);
        config.numberFormat.ifPresent(cfg::setNumberFormat);

        if (config.objectWrapperExposeFields.isPresent()) {
            DefaultObjectWrapper objectWrapper = new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            objectWrapper.setExposeFields(config.objectWrapperExposeFields.get());
            cfg.setObjectWrapper(objectWrapper);
        }

        for (Map.Entry<String, String> directive : freemarkerBuildConfigSupport.getDirectives().entrySet()) {
            Class<?> directiveClass = Thread.currentThread().getContextClassLoader().loadClass(directive.getValue());
            cfg.setSharedVariable(directive.getKey(), (TemplateDirectiveModel) directiveClass.newInstance());
        }

        return cfg;
    }

    private TemplateExceptionHandler getExceptionHandler(String templateExceptionHandler) {
        if (templateExceptionHandler.equals("rethrow")) {
            return TemplateExceptionHandler.RETHROW_HANDLER;
        } else if (templateExceptionHandler.equals("debug")) {
            return TemplateExceptionHandler.DEBUG_HANDLER;
        } else if (templateExceptionHandler.equals("html-debug")) {
            return TemplateExceptionHandler.HTML_DEBUG_HANDLER;
        } else if (templateExceptionHandler.equals("ignore")) {
            return TemplateExceptionHandler.IGNORE_HANDLER;
        } else {
            return null;
        }
    }

    private ClassTemplateLoader newClassTemplateLoader(String location) {
        // assuming an absolute location (i.e. starts with a '/')
        return new ClassTemplateLoader(Thread.currentThread().getContextClassLoader(), "/" + location);
    }

    private FileTemplateLoader newFileTemplateLoader(String path) {
        try {
            return new FileTemplateLoader(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
