package br.com.machinae.assemblae;

import org.junit.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static java.lang.String.format;

/**
 * Test utility with help methods, project specific assertions and soon.
 *
 * @author Welington Veiga
 * @version 1.0.0
 * @since 20/10/2012
 */
public class TestUtil {

    /**
     * Assert the received field is annotated with received class.
     *
     * @param message         message for failing
     * @param field           field that will be tested
     * @param annotationClass annotation
     */
    public static void assertIsAnnotatedWith(String message, Field field, Class<? extends Annotation> annotationClass) {
        for (Annotation annotation : field.getDeclaredAnnotations())
            if (annotationClass.isInstance(annotation))
                return;

        final String defaultMessage = format("The %s field isn't annotated with %s.", field.getName(), annotationClass.getName());

        if (message == null)
            message = defaultMessage;
        else
            message = format("%s %s", message, defaultMessage);

        Assert.fail(message);
    }

    /**
     * @param field
     * @param annotationClass
     * @see TestUtil#assertIsAnnotatedWith(String, Field, Class<? extends Annotation>);
     */
    public static void assertIsAnnotatedWith(Field field, Class<? extends Annotation> annotationClass) {
        assertIsAnnotatedWith(null, field, annotationClass);
    }
}
