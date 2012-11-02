package br.com.machinae.assemblae.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to Identify Data Transfer Objects.
 * Only anntated DTOs can be assembled by Assemblae
 *
 * @author Welington Veiga
 * @version 1.0.0
 * @since 21/10/2012
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTransferObject {
}
