package io.quarkiverse.freemarker.runtime;

import java.util.List;
import java.util.Optional;

import freemarker.template.TemplateExceptionHandler;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus.freemarker")
public interface FreemarkerConfig {

    /**
     * Comma-separated of file system paths where freemarker templates are located
     */
    Optional<List<String>> filePaths();

    /**
     * Set the preferred charset template files are stored in.
     */
    Optional<String> defaultEncoding();

    /**
     * Sets how errors will appear. rethrow, debug, html-debug, ignore.
     *
     * @see freemarker.template.Configuration#setTemplateExceptionHandler(TemplateExceptionHandler)
     */
    Optional<String> templateExceptionHandler();

    /**
     * If false, don't log exceptions inside FreeMarker that it will be thrown at you anyway.
     *
     * @see freemarker.template.Configuration#setLogTemplateExceptions(boolean)
     */
    Optional<Boolean> logTemplateExceptions();

    /**
     * Wrap unchecked exceptions thrown during template processing into TemplateException-s.
     *
     * @see freemarker.template.Configuration#setWrapUncheckedExceptions(boolean)
     */
    Optional<Boolean> wrapUncheckedExceptions();

    /**
     * If false, do not fall back to higher scopes when reading a null loop variable.
     *
     * @see freemarker.template.Configuration#setFallbackOnNullLoopVariable(boolean)
     */
    Optional<Boolean> fallbackOnNullLoopVariable();

    /**
     * The string value for the boolean {@code true} and {@code false} values, usually intended for human consumption (not for a
     * computer language), separated with comma.
     *
     * @see freemarker.template.Configuration#setBooleanFormat(String)
     */
    Optional<String> booleanFormat();

    /**
     * Sets the default number format used to convert numbers to strings.
     *
     * @see freemarker.template.Configuration#setNumberFormat(String)
     */
    Optional<String> numberFormat();

    /**
     * If true, the object wrapper will be configured to expose fields.
     *
     * @see freemarker.ext.beans.BeansWrapper#setExposeFields(boolean)
     */
    Optional<Boolean> objectWrapperExposeFields();

}
