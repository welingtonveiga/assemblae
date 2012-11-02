package br.com.machinae.assemblae;


import br.com.machinae.assemblae.annotation.Ignore;
import br.com.machinae.assemblae.annotation.MappedProperty;
import br.com.machinae.assemblae.testdtos.DTOWithOneNamedMappedProperty;
import br.com.machinae.assemblae.testdtos.DTOWithOneProperty;
import br.com.machinae.assemblae.testdtos.DTOWithTransformerMappedProperty;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class PropertyTransferParamsTest {

    private static final Class<? extends Annotation> IGNORE_ANNOTATION = Ignore.class;

    private static final Class<? extends Annotation> MAPPED_PROPERTY_ANNOTATION =MappedProperty.class;

    @Test
    public void getModelPropertyNameShouldReturnsDTONameWhenNull() throws Exception
    {
        // Arrange
        String expectedPropertyName = "defaultDefined";
        PropertyTransferParams params = new PropertyTransferParams(expectedPropertyName, null, null);

        // Act
        String propertyName = params.getModelProperty();

        // Assert
        assertEquals(expectedPropertyName, propertyName);

    }

    @Test
    public void getModelPropertyNameShouldReturnsDTONameWhenEmpty() throws Exception
    {
        // Arrange
        String expectedPropertyName = "defaultDefined";
        PropertyTransferParams params = new PropertyTransferParams(expectedPropertyName, "", null);

        // Act
        String propertyName = params.getModelProperty();

        // Assert
        assertEquals(expectedPropertyName, propertyName);

    }

    @Test
    public void getModelPropertyNameShouldReturnsValidValue() throws Exception
    {
        // Arrange
        String defaultPropertyName  = "defaultDefined";
        String expectedPropertyName = "definedDefined";
        PropertyTransferParams params = new PropertyTransferParams(defaultPropertyName, expectedPropertyName, null);

        // Act
        String propertyName = params.getModelProperty();

        // Assert
        assertEquals(expectedPropertyName, propertyName);

    }

    @Test(expected = NullPointerException.class)
    public void buildShouldThrowsNullPointerExceptionWhenFieldIsNull() throws IllegalAccessException, InstantiationException {
        // Act
        PropertyTransferParams.build(null);
    }

    @Test
    public void buildReturnShouldHasNamesForModelAndDTOProperties() throws IllegalAccessException, InstantiationException {
        // Arrange
        Class<?> dtoClass = DTOWithOneProperty.class;
        final Field field = dtoClass.getDeclaredFields()[0];

        // Input Assert
        assertFalse(field.isAnnotationPresent(IGNORE_ANNOTATION));
        final String propertyName =  field.getName();

        // Act
        PropertyTransferParams params = PropertyTransferParams.build(field);

        // Assert
        assertEquals(propertyName, params.getDtoProperty());
        assertEquals(propertyName, params.getModelProperty());
    }

    @Test
    public void buildReturnShouldHasMappedValueForModelProperty() throws IllegalAccessException, InstantiationException {

        // Arrange
        Field field = DTOWithOneNamedMappedProperty.class.getDeclaredFields()[0];
        final String dtoPropertyName   = field.getName();
        final String modelPropertyName =  field.getAnnotation(MappedProperty.class).to();

        // Input Assert

        assertNotNull(modelPropertyName);

        // Act
        PropertyTransferParams params = PropertyTransferParams.build(field);

        // Assert
        assertEquals(dtoPropertyName, params.getDtoProperty());
        assertEquals(modelPropertyName, params.getModelProperty());
    }

    @Test
    public void buildReturnShouldHasNoTransformationForNonAnnotatedField() throws IllegalAccessException, InstantiationException {

        // Arrange
        Class<?> dtoClass = DTOWithOneProperty.class;
        final Field field = dtoClass.getDeclaredFields()[0];

        // Input Assert
        assertFalse(field.isAnnotationPresent(IGNORE_ANNOTATION));

        // Act
        PropertyTransferParams params = PropertyTransferParams.build(field);

        // Assert
        assertTrue(params.getTransformer() instanceof NoTransformation);
    }

    @Test
    public void buildReturnShouldHasNoTransformationForNonDefinedFormatter() throws IllegalAccessException, InstantiationException {

        // Arrange
        Field field = DTOWithOneNamedMappedProperty.class.getDeclaredFields()[0];
        final Class definedTransformerClass = field.getAnnotation(MappedProperty.class).transformer();

        // Input Assert
        assertEquals(definedTransformerClass, NoTransformation.class);

        // Act
        PropertyTransferParams params = PropertyTransferParams.build(field);

        // Assert
        assertTrue(params.getTransformer() instanceof NoTransformation);
    }


    @Test
    public void buildReturnShouldReturnDefinedTransformation() throws Exception {

        // Arrange
        Field field = DTOWithTransformerMappedProperty.class.getDeclaredFields()[0];
        final Class definedTransformerClass = field.getAnnotation(MappedProperty.class).transformer();

        // Input Assert
        assertFalse(definedTransformerClass.isAssignableFrom(NoTransformation.class));

        // Act
        PropertyTransferParams params = PropertyTransferParams.build(field);

        // Assert
        assertTrue( definedTransformerClass.isInstance(params.getTransformer()));
    }


}
