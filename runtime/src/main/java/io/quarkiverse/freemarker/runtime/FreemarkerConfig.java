package io.quarkiverse.freemarker.runtime;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import freemarker.template.TemplateExceptionHandler;
import io.quarkus.runtime.annotations.ConfigDocMapKey;
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

    /**
     * The locale used for number and date formatting, and for locale-specific template lookup.
     * The value is a language tag, such as {@code fr-FR} or {@code en-US}.
     *
     * @see freemarker.template.Configuration#setLocale(Locale)
     */
    Optional<Locale> locale();

    /**
     * The time zone used to format date and time values, such as {@code Europe/Paris} or {@code UTC}.
     *
     * @see freemarker.template.Configuration#setTimeZone(java.util.TimeZone)
     */
    Optional<String> timeZone();

    /**
     * The time zone used to format {@code java.sql.Date} and {@code java.sql.Time} values. If not set, the
     * {@code time-zone} setting is used.
     *
     * @see freemarker.template.Configuration#setSQLDateAndTimeTimeZone(java.util.TimeZone)
     */
    Optional<String> sqlDateAndTimeTimeZone();

    /**
     * The default format used to convert date-only values to strings, such as {@code dd/MM/yyyy} or {@code iso}.
     *
     * @see freemarker.template.Configuration#setDateFormat(String)
     */
    Optional<String> dateFormat();

    /**
     * The default format used to convert time-only values to strings, such as {@code HH:mm:ss} or {@code short}.
     *
     * @see freemarker.template.Configuration#setTimeFormat(String)
     */
    Optional<String> timeFormat();

    /**
     * The default format used to convert date-time values to strings, such as {@code dd/MM/yyyy HH:mm} or
     * {@code iso}.
     *
     * @see freemarker.template.Configuration#setDateTimeFormat(String)
     */
    Optional<String> datetimeFormat();

    /**
     * The charset of the output, used by URL escaping when {@code url-escaping-charset} is not set.
     *
     * @see freemarker.template.Configuration#setOutputEncoding(String)
     */
    Optional<String> outputEncoding();

    /**
     * The charset used for URL escaping, for example by the {@code ?url} built-in.
     *
     * @see freemarker.template.Configuration#setURLEscapingCharset(String)
     */
    Optional<String> urlEscapingCharset();

    /**
     * Any other FreeMarker setting, applied with {@code Configuration#setSetting(String, String)}.
     * The key is the setting name as documented in the FreeMarker manual (snake case, for example
     * {@code api_builtin_enabled}), and the value uses the same syntax as in a FreeMarker properties file.
     * These settings are applied after the dedicated properties of this extension, so they override them.
     *
     * @see freemarker.template.Configuration#setSetting(String, String)
     * @see <a href=
     *      "https://freemarker.apache.org/docs/api/freemarker/template/Configuration.html#setSetting-java.lang.String-java.lang.String-">Configuration#setSetting</a>
     */
    @ConfigDocMapKey("setting-name")
    Map<String, String> settings();

}
