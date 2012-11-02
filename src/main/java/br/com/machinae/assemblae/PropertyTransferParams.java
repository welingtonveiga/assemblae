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
class PropertyTransferParams {

    private final String modelProperty;

    private final String dtoProperty;

    private final Transformer<?,?> transformer;


    static PropertyTransferParams build(final Field field)
    {
        checkNotNull(field);

        final MappedProperty map = field.getAnnotation(MappedProperty.class);

        String dtoPropName = field.getName();

        String modelPropName = null;
        Transformer transformer;

        if( map != null)
        {
            modelPropName = map.to();
            transformer = newTransformerInstance(map.transformer());
        }
        else
            transformer = newTransformerInstance(NoTransformation.class);

        return new PropertyTransferParams(dtoPropName, modelPropName, transformer);
    }

    private static Transformer newTransformerInstance(Class<? extends Transformer> clazz){
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new AssemblerException("Transformer creation error", e);
        } catch (IllegalAccessException e) {
            throw new AssemblerException("Transformer creation error", e);
        }
    }

    PropertyTransferParams( String dtoProperty, String modelProperty, Transformer<?, ?> transformer) {
        this.modelProperty = modelProperty;
        this.dtoProperty = dtoProperty;
        this.transformer = transformer;
    }

    String getModelProperty()
    {
        if(modelProperty != null && !modelProperty.isEmpty())
            return modelProperty;

        // By default, if not defined a model name we will return the dto name.
        return dtoProperty;
    }

    String getDtoProperty()
    {
        return dtoProperty;
    }

    Transformer<?,?> getTransformer() {
        return transformer;
    }
}
