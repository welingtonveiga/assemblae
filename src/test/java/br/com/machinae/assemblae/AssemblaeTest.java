package br.com.machinae.assemblae;

import br.com.machinae.assemblae.annotation.DataTransferObject;
import br.com.machinae.assemblae.annotation.Ignore;
import br.com.machinae.assemblae.annotation.MappedProperty;
import br.com.machinae.assemblae.annotation.MappedPropertyImpl;
import br.com.machinae.assemblae.testdtos.*;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Test class for Assemblae
 *
 * @author Welington Veiga
 * @since 21/10/2012
 * @version 1.0.0
 */
public class AssemblaeTest {


    private static final Class<? extends Annotation> MAPPED_PROPERTY_ANNOTATION =MappedProperty.class;
    private static final Class<? extends Annotation> IGNORE_ANNOTATION = Ignore.class;

    @Test(expected = NullPointerException.class)
    public void loadPropertyTransferParamsShouldThrowsNullPointerExceptionLoadingANullClass()
    {
        // Arrange
        Assemblae ae = new Assemblae();

        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadPropertyTransferParamsShouldThrowsIllegalArgumentExceptionLoadingANotDTOClass()
    {
        // Arrange
        Class<? extends Object> clazz = new Object(){}.getClass();
        Assemblae ae = new Assemblae();

        // Input Assert
        assertFalse("The DTO class should not be a DTO", clazz.isAnnotationPresent(DataTransferObject.class));

        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(clazz);
    }

    @Test
    public void loadPropertyTransferParamsShouldReturnAnEmptyListForDTOWithoutProperties()
    {
        // Arrange
        Assemblae ae = new Assemblae();
        Class<?> dtoClass = DTOWithoutProperties.class;

        // Input Assert
        assertEquals("The DTO class should have fields", 0, dtoClass.getFields().length);

        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertTrue(propertyTransferParams.isEmpty());
    }


    @Test
    public void loadPropertyTransferParamsShouldGetAnNonIgnoredPropertiesMapped() throws IllegalAccessException, InstantiationException {
        // Arrange
        Assemblae ae = new Assemblae();
        Class<?> dtoClass = DTOWithOneIgnoredProperty.class;

        // Input Assert
        assertEquals("The DTO class should have only one field", 1, dtoClass.getDeclaredFields().length);
        assertTrue("The DTO field isn't annotated with @Ignore", dtoClass.getDeclaredFields()[0].isAnnotationPresent(IGNORE_ANNOTATION));

        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertTrue(propertyTransferParams.isEmpty());
    }

    @Test
    public void loadPropertyTransferParamsShouldReturnAPropertyWithoutAnnotations() throws IllegalAccessException, InstantiationException {
        // Arrange
        Assemblae ae = new Assemblae();
        Class<?> dtoClass = DTOWithOneProperty.class;

        // Input Assert
        assertEquals("The DTO class should have only one field", 1, dtoClass.getDeclaredFields().length);
        assertFalse("The DTO field should not be annotated with @Ignore", dtoClass.getDeclaredFields()[0].isAnnotationPresent(IGNORE_ANNOTATION));

        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertEquals(1, propertyTransferParams.size());
    }

    @Test
    public void loadPropertyTransferParamsReturnShouldHasPropertyNameForModelAndDTOProperties() throws IllegalAccessException, InstantiationException {
        // Arrange
        Assemblae ae = new Assemblae();
        Class<?> dtoClass = DTOWithOneProperty.class;

        // Input Assert
        assertEquals("The DTO class should have only one field", 1, dtoClass.getDeclaredFields().length);
        assertFalse("The DTO field should not be annotated with @Ignore", dtoClass.getDeclaredFields()[0].isAnnotationPresent(IGNORE_ANNOTATION));
        final String propertyName =  dtoClass.getDeclaredFields()[0].getName();

        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        PropertyTransferParams returned = propertyTransferParams.iterator().next();
        assertEquals(propertyName, returned.getDtoProperty());
        assertEquals(propertyName, returned.getModelProperty());
    }

    @Test
    public void loadPropertyTransferParamsShouldReturnAllNotIgnoredPropertiesMapped() throws IllegalAccessException, InstantiationException {
        // Arrange
        Assemblae ae = new Assemblae();
        Class<?> dtoClass = DTOWithOneIgnoredAndOneNotAnnotatedProperty.class;

        // Input Assert
        Field[] fields = dtoClass.getDeclaredFields();
        assertEquals("The DTO class should have exactly two field", 2, fields.length);
        assertTrue( "The DTO class should have one ingored field and one not.",
                fields[0].isAnnotationPresent(IGNORE_ANNOTATION) && !fields[1].isAnnotationPresent(IGNORE_ANNOTATION)
                        || fields[1].isAnnotationPresent(IGNORE_ANNOTATION) && !fields[0].isAnnotationPresent(IGNORE_ANNOTATION));



        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertEquals(1, propertyTransferParams.size());
    }

    @Test
    public void loadPropertyTransferParamsShouldReturnAPropertyWithMappedPropertyAnnotation() throws IllegalAccessException, InstantiationException {
        // Arrange
        Assemblae ae = new Assemblae();
        Class<?> dtoClass = DTOWithOneMappedProperty.class;

        // Input Assert
        assertEquals("The DTO class should have only one field", 1, dtoClass.getDeclaredFields().length);
        assertTrue("The DTO field should be annotated with @MappedProperty", dtoClass.getDeclaredFields()[0].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION));

        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertEquals(1, propertyTransferParams.size());
    }


    @Test
    public void loadPropertyTransferParamsShouldReturnAllPropertiesMappedAndNotAnnotated() throws IllegalAccessException, InstantiationException {
        // Arrange
        Assemblae ae = new Assemblae();
        Class<?> dtoClass = DTOWithOneMappedAndOneNotAnnotatedProperty.class;

        // Input Assert
        Field[] fields = dtoClass.getDeclaredFields();
        assertEquals("The DTO class should have exactly two field", 2, fields.length);
        assertTrue( "The DTO class should have one ingored field and one not.",
                (fields[0].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION) && !fields[1].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION))
                        || (fields[1].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION) && !fields[0].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION)));

        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertEquals(2, propertyTransferParams.size());
    }


    @Test
    public void loadPropertyTransferParamsReturnShouldHasMappedValueForModelProperty() throws IllegalAccessException, InstantiationException {
        // Arrange
        Assemblae ae = new Assemblae();
        Class<?> dtoClass = DTOWithOneNamedMappedProperty.class;

        // Input Assert
        assertEquals("The DTO class should have only one field", 1, dtoClass.getDeclaredFields().length);
        assertTrue("The DTO field should be annotated with @MappedProperty", dtoClass.getDeclaredFields()[0].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION));
        final String dtoPropertyName   = dtoClass.getDeclaredFields()[0].getName();
        final String modelPropertyName =  dtoClass.getDeclaredFields()[0].getAnnotation(MappedProperty.class).to();

        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        PropertyTransferParams returned = propertyTransferParams.iterator().next();
        assertEquals(dtoPropertyName, returned.getDtoProperty());
        assertEquals(modelPropertyName, returned.getModelProperty());
    }


    @Test
    public void loadPropertyTransferParamsReturnShouldHasNoTransformerFormatterForMapNotDefinedFormatter() throws IllegalAccessException, InstantiationException {
        // Arrange
        Assemblae ae = new Assemblae();
        Class<?> dtoClass = DTOWithOneNamedMappedProperty.class;

        // Input Assert
        assertEquals("The DTO class should have only one field", 1, dtoClass.getDeclaredFields().length);
        assertTrue("The DTO field should be annotated with @MappedProperty", dtoClass.getDeclaredFields()[0].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION));

        // Act
        Collection<PropertyTransferParams> propertyTransferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        PropertyTransferParams returned = propertyTransferParams.iterator().next();
        assertTrue(returned.getTransformer() instanceof NoTransformation);
    }

    public void getModelPropertyNameShouldReturnsDefaultNameForNullMappedProperties() throws Exception
    {
        // Arrange
        Assemblae ae = new Assemblae();
        String expectedPropertyName = "defaultDefined";

        // Act
        String propertyName = ae.getModelPropertyName(expectedPropertyName, null);

        // Assert
        assertEquals(expectedPropertyName, propertyName);

    }

    @Test
    public void getModelPropertyNameShouldReturnsDefaultNameForNonDefinedMappedName() throws Exception
    {
        // Arrange
        Assemblae ae = new Assemblae();
        MappedProperty mappedProperty = new MappedPropertyImpl(null);
        String expectedPropertyName = "defaultDefined";

        // Input Assert
        assertNull(mappedProperty.to());

        // Act
        String propertyName = ae.getModelPropertyName(expectedPropertyName, mappedProperty);

        // Assert
        assertEquals(expectedPropertyName, propertyName);

    }
}
