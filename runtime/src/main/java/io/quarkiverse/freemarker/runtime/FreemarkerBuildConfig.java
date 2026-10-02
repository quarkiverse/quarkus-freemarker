package io.quarkiverse.freemarker.runtime;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import freemarker.template.TemplateModel;
import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithParentName;

@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
@ConfigMapping(prefix = "quarkus.freemarker")
public interface FreemarkerBuildConfig {

    /**
     * List of directives to register with format name=classname
     *
     * @see freemarker.template.Configuration#setSharedVariable(String, TemplateModel)
     */
    Map<String, String> directives();

    /**
     * The default template set
     */
    @WithParentName
    TemplateSet defaultTemplateSet();

    /**
     * Additional named template sets.
     */
    @ConfigDocSection
    @ConfigDocMapKey("template-set-name")
    @WithParentName
    Map<String, TemplateSet> namedTemplateSets();

    @ConfigGroup
    public interface TemplateSet {
        /**
         * The base path of this template set. Template set is a pair of {@code base-path} and {@code includes}
         * serving to select a number of templates for inclusion in the native image.
         * {@code includes} are relative to {@code base-path}.
         * <p>
         * Use slash ({@code /}) as a path separator on all platforms. The value must not start with a slash.
         * <p>
         * Do not set any {@code base-path} value if you want {@code includes} to be relative to root resource path.
         * <h3>Defaults</h3>
         * <table>
         * <tr>
         * <th>Option</th>
         * <th>Default value in case none of <br>
         * {@code quarkus.freemarker.[base-path|includes]}<br>
         * is set</th>
         * <th>Default value otherwise</th>
         * </tr>
         * <tr>
         * <td>{@code quarkus.freemarker.base-path}</td>
         * <td>{@code freemarker/templates}</td>
         * <td>not set (interpreted as root resource path folder)</td>
         * </tr>
         * <tr>
         * <td>{@code quarkus.freemarker.includes}</td>
         * <td>{@code **}</td>
         * <td>not set (no files included)</td>
         * </tr>
         * </table>
         * <p>
         * <h3>Allowed combinations</h3>
         * <p>
         * Setting {@code base-path} but not setting {@code includes} will result in a build
         * time error. We have chosen this behavior (rather than using {@code **} as a default for includes) to avoid
         * including all resources inadvertently and thus bloating your native image.
         *
         * @since 0.2.0
         */
        Optional<String> basePath();

        /**
         * A comma separated list of globs to select FreeMarker templates for inclusion in the native image.
         * <p>
         * {@code includes} are relative to {@code base-path}. Use slash ({@code /}) as a path separator on all
         * platforms. The glob syntax is documented on {@code quarkus.native.resources.includes}.
         * <p>
         * Example:
         *
         * <pre>
         * quarkus.freemarker.includes = **&#47;*.ftl
         * </pre>
         *
         * @since 0.2.0
         */
        Optional<List<String>> includes();

        default TemplateSet assertValid(String key) {
            if (includes().isEmpty()) {
                final String infix = key == null ? "" : "." + key;
                throw new IllegalStateException(
                        "If you set quarkus.freemarker" + infix + ".base-path, you must also set quarkus.freemarker" + infix
                                + ".includes;"
                                + " check your application.properties or wherever you set the named properties");
            }
            return this;
        }

        default boolean isSetByUser() {
            return basePath().isPresent() || includes().isPresent();
        }
    }

}
