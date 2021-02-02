package io.quarkiverse.freemarker.runtime;

import java.util.List;
import java.util.Map;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class FreemarkerRecorder {
    public void initConfigurationProducer(
            BeanContainer beanContainer,
            List<String> resourcePaths,
            Map<String, String> directives) {
        FreemarkerConfigurationProducer producer = beanContainer.instance(FreemarkerConfigurationProducer.class);
        producer.initialize(resourcePaths, directives);
    }
}
