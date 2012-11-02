package br.com.machinae.assemblae;

import br.com.machinae.assemblae.annotation.DataTransferObject;
import br.com.machinae.assemblae.annotation.Ignore;
import br.com.machinae.assemblae.annotation.MappedProperty;

import java.lang.reflect.Field;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utlitário para transformar um modelo em um objeto de transferência (DTO), e para  atualizar
 * o modelo a partir de um DTO.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms978717.aspx">referência</a>
 * @see <a href="http://java.sun.com/blueprints/corej2eepatterns/Patterns/TransferObjectAssembler.html">referência</a>
 * @see <a href="http://www.inspire-software.com/en/index/view/open-source-GeDA-generic-DTO-assembler.html> Inspiração </a>
 *
 * @author Welington Veiga
 * @since 21/10/2012
 * @version 1.0.0
 *
 */
public class Assemblae {

    private final Map<String, Transformer> transformerInstances = new HashMap<String, Transformer>();

    public Collection<PropertyTransferParams> loadPropertyTransferParams(Class<?> dtoClass)
    {
        checkNotNull(dtoClass);
        checkArgument(dtoClass.isAnnotationPresent(DataTransferObject.class));

        Set<PropertyTransferParams> propertyTransferParams = new HashSet<PropertyTransferParams>();

        final Field[] fields = dtoClass.getDeclaredFields();
        for(Field field : fields)
            if(!field.isAnnotationPresent(Ignore.class))
            {
                MappedProperty map = field.getAnnotation(MappedProperty.class);

                final String dtoFieldName = field.getName();
                final String modelFieldName = getModelPropertyName(dtoFieldName, map);
                final Transformer transformer = getTransformerInstance(map);

                propertyTransferParams.add(new PropertyTransferParams(modelFieldName, dtoFieldName, transformer));
            }

        return propertyTransferParams;
    }

    /**
     *
     * @param map
     * @return
     */
    private Transformer getTransformerInstance(MappedProperty map)
    {

        Transformer transformer;

        Class<? extends Transformer> transformerClass;
        if( map != null)
            transformerClass = map.transformer();
        else
            transformerClass = NoTransformation.class;

            String transformerHash = transformerClass.getName();
            if(!transformerInstances.containsKey(transformerHash))
                transformerInstances.put(transformerHash, createInstance(transformerClass));

            transformer = transformerInstances.get(transformerHash);

        return transformer;
    }

    /**
     * Creates a instace given a class
     *
     * @param transformerClass class of instance wanted
     * @return a class instance
     * @throws AssemblerException if instance cant be created
     */
    Transformer createInstance(Class<? extends Transformer> transformerClass)
    {
        try
        {
            return transformerClass.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new AssemblerException("Transformer creation error", e);
        } catch (IllegalAccessException e)
        {
            throw new AssemblerException("Transformer creation error", e);
        }
    }

    /**
     * Resolve the model property name by MappedProperty annotation and a
     * default name.
     *
     * @param defaultName a default name, typically the dto property name
     * @param map MappedProperty annotation
     * @return model property name
     */
    String getModelPropertyName(String defaultName, MappedProperty map) {

        String fieldName = defaultName;

        if (map != null){
            String mappedName = map.to();
            if (mappedName != null && !mappedName.isEmpty())
                fieldName = mappedName;
        }

        return fieldName;
    }
}
