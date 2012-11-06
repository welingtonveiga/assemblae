package br.com.machinae.assemblae;

import br.com.machinae.assemblae.annotation.DataTransferObject;
import br.com.machinae.assemblae.annotation.Ignore;
import br.com.machinae.assemblae.annotation.MappedProperty;
import br.com.machinae.assemblae.tests.*;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for Assembler
 *
 * @author Welington Veiga
 * @version 1.0.0
 * @since 21/10/2012
 */
public class AssemblerTest {


    private static final Class<? extends Annotation> MAPPED_PROPERTY_ANNOTATION = MappedProperty.class;
    private static final Class<? extends Annotation> IGNORE_ANNOTATION = Ignore.class;

    @Test(expected = NullPointerException.class)
    public void loadPropertyTransferParamsShouldThrowsNullPointerExceptionLoadingANullClass() {
        // Arrange
        Assembler ae = new Assembler();

        // Act
        Collection<TransferParams> transferParams = ae.loadPropertyTransferParams(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadPropertyTransferParamsShouldThrowsIllegalArgumentExceptionLoadingANotDTOClass() {
        // Arrange
        Class<? extends Object> clazz = new Object() {}.getClass();
        Assembler ae = new Assembler();

        // Input Assert
        assertFalse("The DTO class should not be a DTO", clazz.isAnnotationPresent(DataTransferObject.class));

        // Act
        ae.loadPropertyTransferParams(clazz);
    }

    @Test
    public void loadPropertyTransferParamsShouldReturnAnEmptyListForDTOWithoutProperties() {
        // Arrange
        Assembler ae = new Assembler();
        ae = spy(ae);
        doReturn(new TransferParams()).when(ae).buildParams((Field) any());

        Class<?> dtoClass = DTOWithoutProperties.class;

        // Input Assert
        assertEquals("The DTO class should have fields", 0, dtoClass.getFields().length);

        // Act
        Collection<TransferParams> transferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertTrue(transferParams.isEmpty());
    }


    @Test
    public void loadPropertyTransferParamsShouldGetAnNonIgnoredPropertiesMapped() throws Exception {
        // Arrange
        Assembler ae = new Assembler();
        Class<?> dtoClass = DTOWithOneIgnoredProperty.class;
        Field field = dtoClass.getDeclaredFields()[0];

        ae = spy(ae);

        // Input Assert
        assertEquals("The DTO class should have only one field", 1, dtoClass.getDeclaredFields().length);
        assertTrue("The DTO field isn't annotated with @Ignore", field.isAnnotationPresent(IGNORE_ANNOTATION));

        // Act
        Collection<TransferParams> transferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertTrue(transferParams.isEmpty());
        verify(ae, never()).buildParams(field);
    }

    @Test
    public void loadPropertyTransferParamsShouldReturnAPropertyWithoutAnnotations() throws Exception {
        // Arrange
        Assembler ae = new Assembler();
        Class<?> dtoClass = DTOWithOneProperty.class;
        Field field = dtoClass.getDeclaredFields()[0];

        ae = spy(ae);
        doReturn(new TransferParams()).when(ae).buildParams(field);

        // Input Assert
        assertEquals("The DTO class should have only one field", 1, dtoClass.getDeclaredFields().length);
        assertFalse("The DTO field should not be annotated with @Ignore", dtoClass.getDeclaredFields()[0].isAnnotationPresent(IGNORE_ANNOTATION));

        // Act
        Collection<TransferParams> transferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertEquals(1, transferParams.size());
        verify(ae).buildParams(field);
    }

    @Test
    public void loadPropertyTransferParamsShouldReturnAllNotIgnoredPropertiesMapped() throws Exception {
        // Arrange
        Assembler ae = new Assembler();
        Class<?> dtoClass = DTOWithOneIgnoredAndOneNotAnnotatedProperty.class;
        Field[] fields = dtoClass.getDeclaredFields();

        ae = spy(ae);

        Field notIgnoredField, ignoredField;
        if (!fields[0].isAnnotationPresent(IGNORE_ANNOTATION)) {
            notIgnoredField = fields[0];
            ignoredField = fields[1];
        } else {
            notIgnoredField = fields[1];
            ignoredField = fields[0];
        }

        doReturn(new TransferParams()).when(ae).buildParams(notIgnoredField);

        // Input Assert
        assertEquals("The DTO class should have exactly two field", 2, fields.length);
        assertTrue("The DTO class should have one ingored field and one not.",
                fields[0].isAnnotationPresent(IGNORE_ANNOTATION) && !fields[1].isAnnotationPresent(IGNORE_ANNOTATION)
                        || fields[1].isAnnotationPresent(IGNORE_ANNOTATION) && !fields[0].isAnnotationPresent(IGNORE_ANNOTATION));


        // Act
        Collection<TransferParams> transferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertEquals(1, transferParams.size());
        verify(ae).buildParams(notIgnoredField);
        verify(ae, never()).buildParams(ignoredField);
    }

    @Test
    public void loadPropertyTransferParamsShouldReturnAPropertyWithMappedPropertyAnnotation() throws Exception {
        // Arrange
        Assembler ae = new Assembler();
        Class<?> dtoClass = DTOWithOneMappedProperty.class;
        Field field = dtoClass.getDeclaredFields()[0];

        ae = spy(ae);
        doReturn(new TransferParams()).when(ae).buildParams(field);

        // Input Assert
        assertEquals("The DTO class should have only one field", 1, dtoClass.getDeclaredFields().length);
        assertTrue("The DTO field should be annotated with @MappedProperty", field.isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION));

        // Act
        Collection<TransferParams> transferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertEquals(1, transferParams.size());
        verify(ae).buildParams(field);
    }

    @Test
    public void loadPropertyTransferParamsShouldReturnAllPropertiesMappedAndNotAnnotated() throws Exception {
        // Arrange
        Assembler ae = new Assembler();
        Class<?> dtoClass = DTOWithOneMappedAndOneNotAnnotatedProperty.class;
        Field[] fields = dtoClass.getDeclaredFields();

        ae = spy(ae);
        doReturn(new TransferParams()).when(ae).buildParams(fields[1]);
        doReturn(new TransferParams("", "", null)).when(ae).buildParams(fields[0]);


        // Input Assert
        assertEquals("The DTO class should have exactly two field", 2, fields.length);
        assertTrue("The DTO class should have one ingored field and one not.",
                (fields[0].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION) && !fields[1].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION))
                        || (fields[1].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION) && !fields[0].isAnnotationPresent(MAPPED_PROPERTY_ANNOTATION)));

        // Act
        Collection<TransferParams> transferParams = ae.loadPropertyTransferParams(dtoClass);

        // Assert
        assertEquals(2, transferParams.size());
        verify(ae).buildParams(fields[0]);
        verify(ae).buildParams(fields[1]);
    }

    @Test(expected = NullPointerException.class)
    public void copyPropertiesShouldThrowsNullPointerExceptionForNullModel() {
        // Arrange
        Assembler ae = new Assembler();

        Object dto = new Object() {
            private String field = null;
        };

        TransferParams params = new TransferParams("field", "field", null);

        // Act
        ae.copyProperty(dto, null, params);

    }

    @Test(expected = NullPointerException.class)
    public void copyPropertiesShouldThrowsNullPointerExceptionForNullDTO() {
        // Arrange
        Assembler ae = new Assembler();

        Object model = new Object() {
            private String field = null;
        };

        TransferParams params = new TransferParams("field", "field", null);

        // Act
        ae.copyProperty(null, model, params);

    }

    @Test(expected = NullPointerException.class)
    public void copyPropertiesShouldThrowsNullPointerExceptionForNullParams() {
        // Arrange
        Assembler ae = new Assembler();

        Object model = new Object() {
            private String field = null;
        };

        Object dto = new Object() {
            private String field = null;
        };

        // Act
        ae.copyProperty(dto, model, null);

    }

    @Test(expected = AssemblerException.class)
    public void copyPropertiesShouldThrowsExceptionForInvalidModelPropertyName() throws Exception {
        // Arrange
        Assembler ae = new Assembler();

        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doThrow(new NoSuchMethodException()).when(ae).getPropertyValue(model, FIELD);

        ae.copyProperty(model, dto, params);

    }


    @Test(expected = AssemblerException.class)
    public void copyPropertiesShouldThrowsExceptionForPrivateModelPropertyName() throws Exception {
        // Arrange
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        Assembler ae = new Assembler();

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doReturn(new IllegalAccessException()).when(ae).getPropertyValue(model, FIELD);

        ae.copyProperty(model, dto, params);
    }

    @Test(expected = AssemblerException.class)
    public void copyPropertiesShouldThrowsExceptionForInvalidDtoPropertyName() throws Exception {
        // Arrange
        final Integer FIELD_VALUE = 1;
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        Assembler ae = new Assembler();

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doReturn(FIELD_VALUE).when(ae).getPropertyValue(model, FIELD);
        doThrow(new NoSuchMethodException()).when(ae).setPropertyValue(dto, DTO_FIELD, FIELD_VALUE);

        // Act
        ae.copyProperty(model, dto, params);

    }


    @Test(expected = AssemblerException.class)
    public void copyPropertiesShouldThrowsExceptionForInaccessibleDtoPropertyName() throws Exception {
        // Arrange
        final Integer FIELD_VALUE = 1;
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        Assembler ae = new Assembler();

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doReturn(FIELD_VALUE).when(ae).getPropertyValue(model, FIELD);
        doThrow(new IllegalAccessException()).when(ae).setPropertyValue(dto, DTO_FIELD, FIELD_VALUE);

        // Act
        ae.copyProperty(model, dto, params);
    }

    @Test
    public void copyPropertiesShouldReturnFieldCopy() throws Exception {
        // Arrange
        final Integer FIELD_VALUE = 1;
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        Assembler ae = new Assembler();
        ae = spy(ae);

        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());
        Object model = new Object() {
        };
        Object dto = new Object() {
        };

        doReturn(FIELD_VALUE).when(ae).getPropertyValue(model, FIELD);
        doNothing().when(ae).setPropertyValue(dto, DTO_FIELD, FIELD_VALUE);

        // Act
        ae.copyProperty(model, dto, params);

        // Assert
        verify(ae, times(1)).setPropertyValue(dto, DTO_FIELD, FIELD_VALUE);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void copyPropertiesShouldReturnTransformedFieldCopy() throws Exception {
        // Arrange
        final Integer FIELD_VALUE = 1;
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";
        final String TRANSFORMED_VALUE = "1";

        Assembler ae = new Assembler();
        ae = spy(ae);

        Transformer transformer = new NoTransformation();
        transformer = spy(transformer);
        doReturn(TRANSFORMED_VALUE).when(transformer).transform(FIELD_VALUE);

        TransferParams params = new TransferParams(DTO_FIELD, FIELD, transformer);
        Object model = new Object() {
        };
        Object dto = new Object() {
        };

        doReturn(FIELD_VALUE).when(ae).getPropertyValue(model, FIELD);
        doNothing().when(ae).setPropertyValue(dto, DTO_FIELD, TRANSFORMED_VALUE);

        // Act
        ae.copyProperty(model, dto, params);

        // Assert
        verify(ae, times(1)).setPropertyValue(dto, DTO_FIELD, TRANSFORMED_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void assembleShouldThrowNullPointerExceptionWhenModelIsNull(){

        // Arrange
        Object model = null;
        Assembler<DTOWithOneProperty> dto = new Assembler<DTOWithOneProperty>();
        // Act
        dto.assemble(model, DTOWithOneProperty.class);
    }

    @Test
    public void assembleShouldNotCallCopyPropertyForAnEmptyPropertyTransferParams() throws Exception{

        // Arrange
        final Class<DTOWithOneProperty> dtoClass = DTOWithOneProperty.class;

        Object model = new Object();

        Assembler<DTOWithOneProperty> ae = new Assembler<DTOWithOneProperty>();
        ae = spy(ae);
        doReturn(new ArrayList()).when(ae).loadPropertyTransferParams(dtoClass);

        // Act
        Assemblae.setInstance(ae);
        DTOWithOneProperty dto = ae.assemble(model, DTOWithOneProperty.class);

        // Assert
        assertNotNull(dto);
        verify(ae, times(1)).loadPropertyTransferParams(dtoClass);
        verify(ae, never()).copyProperty(eq(model), any(), (TransferParams) any());
    }

    @Test
    public void assembleShouldCallCopyPropertyForAPropertyTransferParams() throws Exception{

        // Arrange
        final Class<DTOWithOneProperty> dtoClass = DTOWithOneProperty.class;
        final String DTO_FIELD = "dtoField";
        final String MODEL_FIELD = "modelField";

        Object model = new Object();
        TransferParams params = new TransferParams(DTO_FIELD, MODEL_FIELD, new NoTransformation());

        Assembler<DTOWithOneProperty> ae = new Assembler<DTOWithOneProperty>();
        ae = spy(ae);
        doReturn(Arrays.asList(params)).when(ae).loadPropertyTransferParams(dtoClass);
        doNothing().when(ae).copyProperty(eq(model), any(), eq(params));

        // Act
        Assemblae.setInstance(ae);
        DTOWithOneProperty dto = ae.assemble(model, DTOWithOneProperty.class);

        // Assert
        assertNotNull(dto);
        verify(ae, times(1)).loadPropertyTransferParams(dtoClass);
        verify(ae, times(1)).copyProperty(eq(model), any(), eq(params));
    }

    @Test
    public void assembleShouldCallCopyPropertyForEachPropertyTransferParams() throws Exception{

        // Arrange
        final Class<DTOWithOneProperty> dtoClass = DTOWithOneProperty.class;
        final String DTO_FIELD_1 = "dtoField1";
        final String MODEL_FIELD_1 = "modelField1";
        final String DTO_FIELD_2 = "dtoField2";
        final String MODEL_FIELD_2 = "modelField2";

        Object model = new Object();
        TransferParams params1 = new TransferParams(DTO_FIELD_1, MODEL_FIELD_1, new NoTransformation());
        TransferParams params2 = new TransferParams(DTO_FIELD_2, MODEL_FIELD_2, new NoTransformation());

        Assembler<DTOWithOneProperty> ae = new Assembler<DTOWithOneProperty>();
        ae = spy(ae);
        doReturn(Arrays.asList(params1, params2)).when(ae).loadPropertyTransferParams(dtoClass);
        doNothing().when(ae).copyProperty(eq(model), any(), eq(params1));
        doNothing().when(ae).copyProperty(eq(model), any(), eq(params2));

        // Act
        Assemblae.setInstance(ae);
        DTOWithOneProperty dto = ae.assemble(model, DTOWithOneProperty.class);

        // Assert
        assertNotNull(dto);
        verify(ae, times(1)).loadPropertyTransferParams(dtoClass);
        verify(ae, times(1)).copyProperty(eq(model), any(), eq(params1));
        verify(ae, times(1)).copyProperty(eq(model), any(), eq(params2));
    }


    @Test
    public void assembleAllShouldNotCallCopyPropertyForAnEmptyPropertyTransferParams() throws Exception{

        // Arrange
        final Class<DTOWithOneProperty> dtoClass = DTOWithOneProperty.class;

        Object model = new Object();

        Assembler<DTOWithOneProperty> ae = new Assembler<DTOWithOneProperty>();
        ae = spy(ae);
        doReturn(new ArrayList()).when(ae).loadPropertyTransferParams(dtoClass);

        // Act
        Assemblae.setInstance(ae);
        DTOWithOneProperty dto = ae.assemble(model, DTOWithOneProperty.class);

        // Assert
        assertNotNull(dto);
        verify(ae, times(1)).loadPropertyTransferParams(dtoClass);
        verify(ae, never()).copyProperty(eq(model), any(), (TransferParams) any());
    }

}
