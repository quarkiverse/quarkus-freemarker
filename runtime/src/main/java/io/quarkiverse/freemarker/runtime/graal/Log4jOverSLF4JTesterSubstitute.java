package io.quarkiverse.freemarker.runtime.graal;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

import freemarker.log._Log4jOverSLF4JTester;

/**
 * avoid a class not found on org.apache.log4j.MDC
 */
@TargetClass(_Log4jOverSLF4JTester.class)
final class Log4jOverSLF4JTesterSubstitute {

    @Substitute
    public static final boolean test() {
        return false;
    }
}
