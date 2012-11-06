package br.com.machinae.assemblae;


import br.com.machinae.assemblae.annotation.DataTransferObject;
import br.com.machinae.assemblae.annotation.Ignore;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Assembler is a IAssembler implementation for assembly service used by exposed static Assemblae2 utility.
 *
 * @author Welington Veiga
 * @since 05/11/2012
 *
 * @param <T> DTO type
 */
class Assembler<T> implements IAssembler<T>{

    Assembler(){}

    /**
     * Assemble a model properties in a Data Transfer Object (DTO), defined by a annotated class.
     * Each property will be copied using name convention and metadata in annotations declared on
     * DTO class, metadata will declare conversions, member access e ignored properties.
     *
     * @param model model
     * @param dtoClass dto class
     * @return dto instance
     */
    @Override
    public T assemble(Object model, Class<T> dtoClass) {
        checkNotNull(model, "Model assembled can not be null");

        T dto = instantiateDTO(dtoClass);

        Collection<PropertyTransferParams> params = loadPropertyTransferParams(dtoClass);
        for(PropertyTransferParams param : params)
            copyProperty(model, dto, param);

        return dto;
    }

    @Override
    public Collection<T> assembleAll(Collection<Object> model, Class<T> dtoClass) {
        return null;
    }


    /**
     * Iterates dto class fields and configure the copy params for assemble.
     *
     * @param dtoClass dto class
     * @return configuration for field copy
     */
    Collection<PropertyTransferParams> loadPropertyTransferParams(Class<?> dtoClass) {
        checkNotNull(dtoClass);
        checkArgument(dtoClass.isAnnotationPresent(DataTransferObject.class));

        Set<PropertyTransferParams> propertyTransferParams = new HashSet<PropertyTransferParams>();

        for (Field field : dtoClass.getDeclaredFields())
            if (!field.isAnnotationPresent(Ignore.class))
                propertyTransferParams.add(buildParams(field));

        return propertyTransferParams;
    }

    /**
     * Proxy for PropertyTransferParams builder
     * @see PropertyTransferParams#build(java.lang.reflect.Field)
     *
     * @param field Field metadata
     * @return parameters from field
     */
    PropertyTransferParams buildParams(Field field) {
        return PropertyTransferParams.build(field);
    }

    /**
     * Utility for dto instantiation.
     *
     * @param dtoClass class for desired dto
     * @return a dtoClass instance
     * @throws AssemblerException for instantiation errors
     */
    T instantiateDTO(Class<T> dtoClass) {
        T dto = null;
        try {
            dto = dtoClass.newInstance();
        } catch (InstantiationException e) {
            new AssemblerException("Failure instantiating dto", e);
        } catch (IllegalAccessException e) {
            new AssemblerException("Failure instantiating dto", e);
        }
        return dto;
    }

    /**
     * Utility for copy an property from model to DTO, applying the appropriate transformation
     *
     * @param model model from  where the property will be copied
     * @param dto   model where the property will be copied
     * @param param copy parameters
     */
    void copyProperty(Object model, Object dto, PropertyTransferParams param) {
        checkNotNull(model, "Model cant be null for copying.");
        checkNotNull(dto, "DTO cant be null for copying.");
        checkNotNull(param, "Transfer params cant be null for copying.");

        try {
            final String modelProperty = param.getModelProperty();
            final String dtoProperty = param.getDtoProperty();
            final Object value = getPropertyValue(model, modelProperty);

            setPropertyValue(dto, dtoProperty, param.getTransformer().transform(value));

        } catch (IllegalAccessException e) {
            throw new AssemblerException("Property copy error", e);
        } catch (InvocationTargetException e) {
            throw new AssemblerException("Property copy error", e);
        } catch (NoSuchMethodException e) {
            throw new AssemblerException("Property copy error", e);
        }
    }

    /**
     * Only set property value to an object using apache commons convention.
     *
     * @see {BeanUtilsBean#setProperty}
     *
     * @param dto object
     * @param property  property to set value
     * @param propertyValue  value
     *
     * @throws IllegalAccessException when property cant be accessed
     * @throws InvocationTargetException when setter throws exception
     * @throws NoSuchMethodException when property does not exists
     */
    void setPropertyValue(Object dto, String property, Object propertyValue) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        BeanUtilsBean.getInstance().getPropertyUtils().setProperty(dto, property, propertyValue);
    }

    /**
     * Only get property value from an object using apache commons convention.
     *
     * @see {BeanUtilsBean#getProperty}
     *
     * @param model object
     * @param property  property to get value
     * @return value desired
     * @throws IllegalAccessException when property cant be accessed
     * @throws InvocationTargetException when getter throws exception
     * @throws NoSuchMethodException when property does not exists
     */
    Object getPropertyValue(Object model, String property) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return BeanUtilsBean.getInstance().getPropertyUtils().getProperty(model, property);
    }

}
