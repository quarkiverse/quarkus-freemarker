package io.quarkiverse.freemarker.deployment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Singleton;

import org.jboss.logging.Logger;

import freemarker.ext.jython.JythonModel;
import freemarker.ext.jython.JythonWrapper;
import io.quarkiverse.freemarker.TemplatePath;
import io.quarkiverse.freemarker.runtime.FreemarkerBuildConfig;
import io.quarkiverse.freemarker.runtime.FreemarkerBuildConfig.TemplateSet;
import io.quarkiverse.freemarker.runtime.FreemarkerBuildConfigSupport;
import io.quarkiverse.freemarker.runtime.FreemarkerConfigurationProducer;
import io.quarkiverse.freemarker.runtime.FreemarkerRecorder;
import io.quarkiverse.freemarker.runtime.FreemarkerTemplateProducer;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourcePatternsBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourcePatternsBuildItem.Builder;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedClassBuildItem;

public class FreemarkerProcessor {

    private static final Logger LOGGER = Logger.getLogger(FreemarkerProcessor.class);

    private static final String FEATURE = "freemarker";

    private static final String CLASSPATH_PROTOCOL = "classpath";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void runtimeInit(BuildProducer<RuntimeInitializedClassBuildItem> runtimeInitialized) {

        Stream.of(JythonWrapper.class, JythonModel.class)
                .map(Class::getName)
                .map(RuntimeInitializedClassBuildItem::new)
                .forEach(runtimeInitialized::produce);
    }

    @SuppressWarnings("deprecation")
    @BuildStep
    void discoverTemplates(BuildProducer<TemplateSetBuildItem> templateSets, FreemarkerBuildConfig config) {

        if (config.resourcePaths.isPresent()) {
            for (String basePath : config.resourcePaths.get()) {
                // Strip any 'classpath:' protocol prefixes because they are assumed
                // but not recognized by ClassLoader.getResources()
                if (basePath.startsWith(CLASSPATH_PROTOCOL + ':')) {
                    basePath = basePath.substring(CLASSPATH_PROTOCOL.length() + 1);
                }
                templateSets.produce(TemplateSetBuildItem.builder().basePath(basePath).includeGlob("**").build());
            }
        }

        if (!config.resourcePaths.isPresent() && !config.defaultTemplateSet.isSetByUser()) {
            /* produce the default */
            templateSets.produce(TemplateSetBuildItem.builder().basePath("freemarker/templates").includeGlob("**").build());
        }

        if (config.defaultTemplateSet.isSetByUser()) {
            templateSets.produce(toBuildItem(config.defaultTemplateSet.assertValid(null)));
        }

        for (Map.Entry<String, TemplateSet> entry : config.namedTemplateSets.entrySet()) {
            templateSets.produce(toBuildItem(entry.getValue().assertValid(entry.getKey())));
        }
    }

    static TemplateSetBuildItem toBuildItem(TemplateSet templateSet) {
        TemplateSetBuildItem.Builder builder = TemplateSetBuildItem.builder();

        templateSet.basePath.ifPresent(builder::basePath);
        templateSet.includes.ifPresent(builder::includeGlobs);
        templateSet.excludes.ifPresent(builder::excludeGlobs);

        return builder.build();
    }

    @BuildStep
    void nativeResources(
            List<TemplateSetBuildItem> templateSets,
            BuildProducer<NativeImageResourcePatternsBuildItem> nativeImageResources) {
        for (TemplateSetBuildItem templateSet : templateSets) {
            final Builder builder = NativeImageResourcePatternsBuildItem.builder();
            templateSet.getIncludeGlobs().stream()
                    .map(templateSet::resolve)
                    .forEach(builder::includeGlob);
            templateSet.getExcludeGlobs().stream()
                    .map(templateSet::resolve)
                    .forEach(builder::excludeGlob);
            nativeImageResources.produce(builder.build());
        }
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBeans() {
        return AdditionalBeanBuildItem.builder()
                .setUnremovable()
                .addBeanClasses(FreemarkerConfigurationProducer.class, TemplatePath.class, FreemarkerTemplateProducer.class)
                .build();
    }

    @BuildStep
    public void reflection(BuildProducer<ReflectiveClassBuildItem> reflectiveClassBuildItemProducer,
            FreemarkerBuildConfig config) {

        LOGGER.info("Adding directives " + config.directive.values());
        config.directive.values().stream()
                .map(classname -> new ReflectiveClassBuildItem(false, false, classname))
                .forEach(reflectiveClassBuildItemProducer::produce);
    }

    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    SyntheticBeanBuildItem pushConfigurationBean(FreemarkerRecorder recorder,
            List<TemplateSetBuildItem> templateSets,
            FreemarkerBuildConfig buildConfig) {
        final List<String> resourcePaths = templateSets.stream()
                .map(TemplateSetBuildItem::getBasePath)
                .map(basePath -> basePath.orElse(""))
                .collect(Collectors.toList());

        return SyntheticBeanBuildItem.configure(FreemarkerBuildConfigSupport.class)
                .scope(Singleton.class)
                .supplier(recorder.freemarkerBuildConfigSupport(resourcePaths, buildConfig.directive))
                .done();
    }
}
