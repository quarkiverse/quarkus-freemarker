package io.quarkiverse.freemarker.runtime;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class FreemarkerRecorder {

    public Supplier<FreemarkerBuildConfigSupport> freemarkerBuildConfigSupport(List<String> resourcePaths,
            Map<String, String> directives) {
        return new Supplier<FreemarkerBuildConfigSupport>() {
            @Override
            public FreemarkerBuildConfigSupport get() {
                return new FreemarkerBuildConfigSupport(resourcePaths, directives);
            }
        };
    }
}
