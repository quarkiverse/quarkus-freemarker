package io.quarkiverse.freemarker.deployment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import io.quarkus.builder.item.MultiBuildItem;
import io.quarkus.deployment.pkg.NativeConfig;

/**
 * A build item that
 * <ol>
 * <li>Embeds the set of class paths templates defined by a base path and a set of include and exclude globs
 * in the native image;
 * <li>Adds the base path to the default {@link Configuration} via a {@link ClassTemplateLoader}
 * <p>
 * See {@link NativeConfig.ResourcesConfig#includes} for the supported glob syntax.
 */
public final class TemplateSetBuildItem extends MultiBuildItem {

    private final Optional<String> basePath;
    private final List<String> excludeGlobs;
    private final List<String> includeGlobs;

    private TemplateSetBuildItem(Optional<String> basePath, List<String> includeGlobs, List<String> excludeGlobs) {
        this.basePath = basePath;
        this.includeGlobs = includeGlobs;
        this.excludeGlobs = excludeGlobs;
    }

    /**
     * An empty {@link Optional} means that {@link #getIncludeGlobs()} and {@link #getExcludeGlobs()} should be
     * resolved against the root path
     *
     * @return the base path against which {@link #getIncludeGlobs()} and {@link #getExcludeGlobs()} should be resolved
     */
    public Optional<String> getBasePath() {
        return basePath;
    }

    public List<String> getExcludeGlobs() {
        return excludeGlobs;
    }

    public List<String> getIncludeGlobs() {
        return includeGlobs;
    }

    /**
     * @param relativePath the relative path to resolve
     * @return the given {@code relativePath} resolved against {@link #basePath}
     */
    public String resolve(String relativePath) {
        return basePath.map(bPath -> bPath + "/" + relativePath).orElse(relativePath);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Optional<String> basePath = Optional.empty();
        private List<String> excludeGlobs = new ArrayList<>();
        private List<String> includeGlobs = new ArrayList<>();

        public TemplateSetBuildItem build() {
            final List<String> incl = includeGlobs;
            includeGlobs = null;
            final List<String> excl = excludeGlobs;
            excludeGlobs = null;
            return new TemplateSetBuildItem(
                    basePath,
                    Collections.unmodifiableList(incl),
                    Collections.unmodifiableList(excl));
        }

        public Builder basePath(String basePath) {
            this.basePath = basePath == null || basePath.isEmpty() ? Optional.empty() : Optional.of(basePath);
            return this;
        }

        /**
         * Add a glob pattern for matching resource paths that should <strong>not</strong> be added to the native image.
         * <p>
         * Use slash ({@code /}) as a path separator on all platforms. Globs must not start with slash. See
         * {@link NativeConfig.ResourcesConfig#includes} for the supported glob syntax.
         *
         * @param glob the glob pattern to add to the list of patterns to exclude
         * @return this {@link Builder}
         */
        public Builder excludeGlob(String glob) {
            excludeGlobs.add(glob);
            return this;
        }

        /**
         * Add a collection of glob patterns for matching resource paths that should <strong>not</strong> be added to the
         * native image.
         * <p>
         * Use slash ({@code /}) as a path separator on all platforms. Globs must not start with slash. See
         * {@link NativeConfig.ResourcesConfig#includes} for the supported glob syntax.
         *
         * @param globs the glob patterns to add to the list of patterns to exclude
         * @return this {@link Builder}
         */
        public Builder excludeGlobs(Collection<String> globs) {
            excludeGlobs.addAll(globs);
            return this;
        }

        /**
         * Add an array of glob patterns for matching resource paths that should <strong>not</strong> be added to the
         * native image.
         * <p>
         * Use slash ({@code /}) as a path separator on all platforms. Globs must not start with slash. See
         * {@link NativeConfig.ResourcesConfig#includes} for the supported glob syntax.
         *
         * @param globs the glob patterns to add to the list of patterns to exclude
         * @return this {@link Builder}
         */
        public Builder excludeGlobs(String... globs) {
            Stream.of(globs).forEach(excludeGlobs::add);
            return this;
        }

        /**
         * Add a glob pattern for matching resource paths that should be added to the native image.
         * <p>
         * Use slash ({@code /}) as a path separator on all platforms. Globs must not start with slash. See
         * {@link NativeConfig.ResourcesConfig#includes} for the supported glob syntax.
         *
         * @param glob the glob pattern to add
         * @return this {@link Builder}
         */
        public Builder includeGlob(String glob) {
            includeGlobs.add(glob);
            return this;
        }

        /**
         * Add a collection of glob patterns for matching resource paths that should be added to the native image.
         * <p>
         * Use slash ({@code /}) as a path separator on all platforms. Globs must not start with slash. See
         * {@link NativeConfig.ResourcesConfig#includes} for the supported glob syntax.
         *
         * @param globs the glob patterns to add
         * @return this {@link Builder}
         */
        public Builder includeGlobs(Collection<String> globs) {
            includeGlobs.addAll(globs);
            return this;
        }

        /**
         * Add an array of glob patterns for matching resource paths that should be added to the native image.
         * <p>
         * Use slash ({@code /}) as a path separator on all platforms. Globs must not start with slash. See
         * {@link NativeConfig.ResourcesConfig#includes} for the supported glob syntax.
         *
         * @param globs the glob patterns to add
         * @return this {@link Builder}
         */
        public Builder includeGlobs(String... patterns) {
            Stream.of(patterns).forEach(includeGlobs::add);
            return this;
        }

    }

}
