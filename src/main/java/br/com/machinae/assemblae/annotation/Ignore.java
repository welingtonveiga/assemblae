package br.com.machinae.assemblae.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checks a property to be ignored on assemble process in all directions.
 *
 * @author Welington Veiga
 * @since 20/10/2012
 * @version 1.0.0
 */

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
}
