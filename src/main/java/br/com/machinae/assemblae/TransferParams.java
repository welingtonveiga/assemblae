package br.com.machinae.assemblae;

import br.com.machinae.assemblae.annotation.MappedProperty;

import java.lang.reflect.Field;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Model that retain all property map configurations for a single Property.
 *
 * @author Welington Veiga
 * @version 1.0.0
 */
class TransferParams {

    private final String modelProperty;

    private final String dtoProperty;

    private final Transformer<Object, Object> transformer;


    static TransferParams build(final Field field) {
        checkNotNull(field);

        final MappedProperty map = field.getAnnotation(MappedProperty.class);

        String dtoPropName = field.getName();

        String modelPropName = null;
        Transformer<Object, Object> transformer;

        if (map != null) {
            modelPropName = map.to();
            transformer = newTransformerInstance(map.transformer());
        } else
            transformer = newTransformerInstance(NoTransformation.class);

        return new TransferParams(dtoPropName, modelPropName,  transformer);
    }

    @SuppressWarnings("unchecked")
    private static Transformer<Object, Object> newTransformerInstance(Class<? extends Transformer> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new AssemblerException("Transformer creation error", e);
        } catch (IllegalAccessException e) {
            throw new AssemblerException("Transformer creation error", e);
        }
    }

    TransferParams(String dtoProperty, String modelProperty, Transformer<Object, Object> transformer) {
        this.modelProperty = modelProperty;
        this.dtoProperty = dtoProperty;
        this.transformer = transformer;
    }

    TransferParams() {
        this.modelProperty = null;
        this.dtoProperty = null;
        this.transformer = null;
    }

    String getModelProperty() {
        if (modelProperty != null && !modelProperty.isEmpty())
            return modelProperty;

        // By default, if not defined a model name we will return the dto name.
        return dtoProperty;
    }

    String getDtoProperty() {
        return dtoProperty;
    }

    Transformer<Object, Object> getTransformer() {
        return transformer;
    }
}
