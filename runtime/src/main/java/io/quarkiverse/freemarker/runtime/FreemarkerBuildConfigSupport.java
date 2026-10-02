package io.quarkiverse.freemarker.runtime;

import java.util.List;
import java.util.Map;

public class FreemarkerBuildConfigSupport {

    private final List<String> basePaths;
    private final Map<String, String> directives;

    public FreemarkerBuildConfigSupport(List<String> basePaths, Map<String, String> directives) {
        this.basePaths = basePaths;
        this.directives = directives;
    }

    public List<String> getBasePaths() {
        return basePaths;
    }

    public Map<String, String> getDirectives() {
        return directives;
    }
}
