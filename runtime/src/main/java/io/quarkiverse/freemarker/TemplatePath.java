package io.quarkiverse.freemarker;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;

@Qualifier
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, METHOD })
public @interface TemplatePath {

    /**
     * @return the path relative from the base path, must not be an empty string
     */
    @Nonbinding
    String value();

    /**
     * Supports inline instantiation of this qualifier.
     */
    public static final class Literal extends AnnotationLiteral<TemplatePath> implements TemplatePath {

        private static final long serialVersionUID = 1L;

        private final String value;

        public Literal(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }

    }
}
