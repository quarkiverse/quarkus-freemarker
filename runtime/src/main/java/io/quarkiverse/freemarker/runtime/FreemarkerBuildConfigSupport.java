package io.quarkiverse.freemarker.runtime;

import java.util.List;
import java.util.Map;

public class FreemarkerBuildConfigSupport {

    private final List<String> resourcePaths;
    private final Map<String, String> directives;

    public FreemarkerBuildConfigSupport(List<String> resourcePaths, Map<String, String> directives) {
        this.resourcePaths = resourcePaths;
        this.directives = directives;
    }

    public List<String> getResourcePaths() {
        return resourcePaths;
    }

    public Map<String, String> getDirectives() {
        return directives;
    }
}
