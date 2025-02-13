package io.quarkiverse.freemarker.runtime;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * Comma-separated list of absolute resource paths to scan recursively for templates.
     * All tree folder from 'resource-paths' will be added as a resource.
     * Unprefixed locations or locations starting with classpath will be processed in the same way.
     * <p>
     * Defaults relevant for this option are documented on {@code quarkus.freemarker.base-path}
     *
     * @deprecated Use template set group of options instead ({@code quarkus.freemarker.<template-set-name>.base-path}
     *             {@code quarkus.freemarker.<template-set-name>.includes} and
     *             {@code quarkus.freemarker.<template-set-name>.excludes})
     */
    Optional<List<String>> resourcePaths();

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
         * The base path of this template set. Template set is a triple of {@code base-path}, {@code includes} and
         * {@code excludes} serving to select a number of templates for inclusion in the native image.
         * {@code includes} and {@code excludes} are relative to {@code base-path}.
         * <p>
         * Use slash ({@code /}) as a path separator on all platforms. The value must not start with a slash.
         * <p>
         * Do not set any {@code base-path} value if you want {@code includes} and {@code excludes} to be relative to
         * root resource path.
         * <h3>Defaults</h3>
         * <table>
         * <tr>
         * <th>Option</th>
         * <th>Default value in case none of <br>
         * {@code quarkus.freemarker.[base-path|includes|excludes|resource-paths]}<br>
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
         * <tr>
         * <td>{@code quarkus.freemarker.excludes}</td>
         * <td>not set (no files excluded)</td>
         * <td>not set (no files excluded)</td>
         * </tr>
         * </table>
         * <p>
         * The defaults described in the second column of the table are to achieve the backwards compatibility
         * with the behavior of {@code quarkus.freemarker.resource-paths} before Quarkus Freemarker 0.2.0.
         * <p>
         * <h3>Allowed combinations</h3>
         * <p>
         * Setting {@code base-path} and/or {@code excludes} but not setting {@code includes} will result in a build
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
         * quarkus.freemarker.includes = **.ftl
         * </pre>
         *
         * @since 0.2.0
         */
        Optional<List<String>> includes();

        /**
         * A comma separated list of globs <strong>not</strong> to include in the native image.
         * <p>
         * {@code excludes} are relative to {@code base-path}. Use slash ({@code /}) as a path separator on all
         * platforms. The glob syntax is documented on {@code quarkus.native.resources.includes}.
         * <p>
         * Example:
         *
         * <pre>
         * quarkus.freemarker.excludes = **&#47;unwanted*
         * </pre>
         *
         * @since 0.2.0
         */
        Optional<List<String>> excludes();

        default TemplateSet assertValid(String key) {
            if (includes().isEmpty()) {
                final String infix = key == null ? "" : "." + key;
                final String badProps = Stream
                        .of(new AbstractMap.SimpleImmutableEntry<String, Optional<?>>("base-path", basePath()),
                                new AbstractMap.SimpleImmutableEntry<String, Optional<?>>("excludes", excludes()))
                        .filter(entry -> entry.getValue().isPresent())
                        .map(AbstractMap.SimpleImmutableEntry::getKey)
                        .map(k -> "quarkus.freemarker" + infix + "." + k)
                        .collect(Collectors.joining(" and "));

                throw new IllegalStateException(
                        "If you set " + badProps + ", you must also set quarkus.freemarker" + infix
                                + ".includes;"
                                + " check your application.properties or wherever you set the named properties");
            }
            return this;
        }

        default boolean isSetByUser() {
            return basePath().isPresent() || includes().isPresent() || excludes().isPresent();
        }
    }

}
