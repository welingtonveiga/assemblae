package br.com.machinae.assemblae;


import br.com.machinae.assemblae.annotation.Ignore;
import br.com.machinae.assemblae.annotation.MappedProperty;
import br.com.machinae.assemblae.tests.DTOWithOneNamedMappedProperty;
import br.com.machinae.assemblae.tests.DTOWithOneProperty;
import br.com.machinae.assemblae.tests.DTOWithTransformerMappedProperty;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class TransferParamsTest {

    private static final Class<? extends Annotation> IGNORE_ANNOTATION = Ignore.class;

    private static final Class<? extends Annotation> MAPPED_PROPERTY_ANNOTATION = MappedProperty.class;

    @Test
    public void getModelPropertyNameShouldReturnsDTONameWhenNull() throws Exception {
        // Arrange
        String expectedPropertyName = "defaultDefined";
        TransferParams params = new TransferParams(expectedPropertyName, null, null);

        // Act
        String propertyName = params.getModelProperty();

        // Assert
        assertEquals(expectedPropertyName, propertyName);

    }

    @Test
    public void getModelPropertyNameShouldReturnsDTONameWhenEmpty() throws Exception {
        // Arrange
        String expectedPropertyName = "defaultDefined";
        TransferParams params = new TransferParams(expectedPropertyName, "", null);

        // Act
        String propertyName = params.getModelProperty();

        // Assert
        assertEquals(expectedPropertyName, propertyName);

    }

    @Test
    public void getModelPropertyNameShouldReturnsValidValue() throws Exception {
        // Arrange
        String defaultPropertyName = "defaultDefined";
        String expectedPropertyName = "definedDefined";
        TransferParams params = new TransferParams(defaultPropertyName, expectedPropertyName, null);

        // Act
        String propertyName = params.getModelProperty();

        // Assert
        assertEquals(expectedPropertyName, propertyName);

    }

    @Test(expected = NullPointerException.class)
    public void buildShouldThrowsNullPointerExceptionWhenFieldIsNull() throws IllegalAccessException, InstantiationException {
        // Act
        TransferParams.build(null);
    }

    @Test
    public void buildReturnShouldHasNamesForModelAndDTOProperties() throws IllegalAccessException, InstantiationException {
        // Arrange
        Class<?> dtoClass = DTOWithOneProperty.class;
        final Field field = dtoClass.getDeclaredFields()[0];

        // Input Assert
        assertFalse(field.isAnnotationPresent(IGNORE_ANNOTATION));
        final String propertyName = field.getName();

        // Act
        TransferParams params = TransferParams.build(field);

        // Assert
        assertEquals(propertyName, params.getDtoProperty());
        assertEquals(propertyName, params.getModelProperty());
    }

    @Test
    public void buildReturnShouldHasMappedValueForModelProperty() throws IllegalAccessException, InstantiationException {

        // Arrange
        Field field = DTOWithOneNamedMappedProperty.class.getDeclaredFields()[0];
        final String dtoPropertyName = field.getName();
        final String modelPropertyName = field.getAnnotation(MappedProperty.class).to();

        // Input Assert

        assertNotNull(modelPropertyName);

        // Act
        TransferParams params = TransferParams.build(field);

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
        TransferParams params = TransferParams.build(field);

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
        TransferParams params = TransferParams.build(field);

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
        TransferParams params = TransferParams.build(field);

        // Assert
        assertTrue(definedTransformerClass.isInstance(params.getTransformer()));
    }


}
