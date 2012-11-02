package br.com.machinae.assemblae.annotation;

import br.com.machinae.assemblae.NoTransformation;
import br.com.machinae.assemblae.Transformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations for mapping configuration on copy from/to model.
 *
 * @author Welington Veiga
 * @since 21/10/2012
 * @version 1.0.0
 */

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MappedProperty {
    public String to() default "";
    public Class<? extends Transformer> transformer() default NoTransformation.class;
}
