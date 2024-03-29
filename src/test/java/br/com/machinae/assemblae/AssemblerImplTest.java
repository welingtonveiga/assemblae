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
 * Test class for AssemblerImpl
 *
 * @author Welington Veiga
 * @version 1.0.0
 * @since 21/10/2012
 */
public class AssemblerImplTest {


    private static final Class<? extends Annotation> MAPPED_PROPERTY_ANNOTATION = MappedProperty.class;
    private static final Class<? extends Annotation> IGNORE_ANNOTATION = Ignore.class;

    @Test(expected = NullPointerException.class)
    public void loadPropertyTransferParamsShouldThrowsNullPointerExceptionLoadingANullClass() {
        // Arrange
        AssemblerImpl ae = new AssemblerImpl();

        // Act
        Collection<TransferParams> transferParams = ae.loadPropertyTransferParams(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadPropertyTransferParamsShouldThrowsIllegalArgumentExceptionLoadingANotDTOClass() {
        // Arrange
        Class<? extends Object> clazz = new Object() {}.getClass();
        AssemblerImpl ae = new AssemblerImpl();

        // Input Assert
        assertFalse("The DTO class should not be a DTO", clazz.isAnnotationPresent(DataTransferObject.class));

        // Act
        ae.loadPropertyTransferParams(clazz);
    }

    @Test
    public void loadPropertyTransferParamsShouldReturnAnEmptyListForDTOWithoutProperties() {
        // Arrange
        AssemblerImpl ae = new AssemblerImpl();
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
        AssemblerImpl ae = new AssemblerImpl();
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
        AssemblerImpl ae = new AssemblerImpl();
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
        AssemblerImpl ae = new AssemblerImpl();
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
        AssemblerImpl ae = new AssemblerImpl();
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
        AssemblerImpl ae = new AssemblerImpl();
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
        AssemblerImpl ae = new AssemblerImpl();

        Object dto = new Object() {
            private String field = null;
        };

        TransferParams params = new TransferParams("field", "field", null);

        // Act
        ae.copyPropertyFromModelToDTO(null, dto, params);

    }

    @Test(expected = NullPointerException.class)
    public void copyPropertiesShouldThrowsNullPointerExceptionForNullDTO() {
        // Arrange
        AssemblerImpl ae = new AssemblerImpl();

        Object model = new Object() {
            private String field = null;
        };

        TransferParams params = new TransferParams("field", "field", null);

        // Act
        ae.copyPropertyFromModelToDTO(model, null, params);

    }



    @Test(expected = AssemblerException.class)
    public void copyPropertiesShouldThrowsExceptionForInvalidModelPropertyName() throws Exception {
        // Arrange
        AssemblerImpl ae = new AssemblerImpl();

        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doThrow(new NoSuchMethodException()).when(ae).getPropertyValue(model, FIELD);

        ae.copyPropertyFromModelToDTO(model, dto, params);

    }


    @Test(expected = AssemblerException.class)
    public void copyPropertiesShouldThrowsExceptionForPrivateModelPropertyName() throws Exception {
        // Arrange
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        AssemblerImpl ae = new AssemblerImpl();

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doReturn(new IllegalAccessException()).when(ae).getPropertyValue(model, FIELD);

        ae.copyPropertyFromModelToDTO(model, dto, params);
    }

    @Test(expected = AssemblerException.class)
    public void copyPropertiesShouldThrowsExceptionForInvalidDtoPropertyName() throws Exception {
        // Arrange
        final Integer FIELD_VALUE = 1;
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        AssemblerImpl ae = new AssemblerImpl();

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doReturn(FIELD_VALUE).when(ae).getPropertyValue(model, FIELD);
        doThrow(new NoSuchMethodException()).when(ae).setPropertyValue(dto, DTO_FIELD, FIELD_VALUE);

        // Act
        ae.copyPropertyFromModelToDTO(model, dto, params);

    }


    @Test(expected = AssemblerException.class)
    public void copyPropertiesShouldThrowsExceptionForInaccessibleDtoPropertyName() throws Exception {
        // Arrange
        final Integer FIELD_VALUE = 1;
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        AssemblerImpl ae = new AssemblerImpl();

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doReturn(FIELD_VALUE).when(ae).getPropertyValue(model, FIELD);
        doThrow(new IllegalAccessException()).when(ae).setPropertyValue(dto, DTO_FIELD, FIELD_VALUE);

        // Act
        ae.copyPropertyFromModelToDTO(model, dto, params);
    }

    @Test
    public void copyPropertiesShouldReturnFieldCopy() throws Exception {
        // Arrange
        final Integer FIELD_VALUE = 1;
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        AssemblerImpl ae = new AssemblerImpl();
        ae = spy(ae);

        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());
        Object model = new Object() {
        };
        Object dto = new Object() {
        };

        doReturn(FIELD_VALUE).when(ae).getPropertyValue(model, FIELD);
        doNothing().when(ae).setPropertyValue(dto, DTO_FIELD, FIELD_VALUE);

        // Act
        ae.copyPropertyFromModelToDTO(model, dto, params);

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

        AssemblerImpl ae = new AssemblerImpl();
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
        ae.copyPropertyFromModelToDTO(model, dto, params);

        // Assert
        verify(ae, times(1)).setPropertyValue(dto, DTO_FIELD, TRANSFORMED_VALUE);
    }


    @Test(expected = NullPointerException.class)
    public void copyPropertyFromModelToDTOShouldThrowsNullPointerExceptionForNullParams() {
        // Arrange
        AssemblerImpl ae = new AssemblerImpl();

        Object model = new Object() {
            private String field = null;
        };

        Object dto = new Object() {
            private String field = null;
        };

        // Act
        ae.copyPropertyFromModelToDTO(model, dto, null);
    }

    @Test(expected = NullPointerException.class)
    public void copyPropertiesFromDTOToModelShouldThrowsNullPointerExceptionForNullModel() {
        // Arrange
        AssemblerImpl ae = new AssemblerImpl();

        Object dto = new Object() {
            private String field = null;
        };

        TransferParams params = new TransferParams("field", "field", null);

        // Act
        ae.copyPropertyFromDTOToModel(dto, null, params);

    }

    @Test(expected = AssemblerException.class)
    public void copyPropertiesFromDTOToModelShouldThrowsExceptionForInvalidModelPropertyName() throws Exception {
        // Arrange
        AssemblerImpl ae = new AssemblerImpl();

        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doThrow(new NoSuchMethodException()).when(ae).getPropertyValue(model, FIELD);

        ae.copyPropertyFromDTOToModel(dto, model, params);

    }


    @Test(expected = AssemblerException.class)
    public void copyPropertiesFromDTOToModelShouldThrowsExceptionForPrivateModelPropertyName() throws Exception {
        // Arrange
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        AssemblerImpl ae = new AssemblerImpl();

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doReturn(new IllegalAccessException()).when(ae).getPropertyValue(model, FIELD);

        ae.copyPropertyFromDTOToModel(dto, model, params);
    }

    @Test(expected = AssemblerException.class)
    public void copyPropertiesFromDTOToModelShouldThrowsExceptionForInvalidDtoPropertyName() throws Exception {
        // Arrange
        final Integer FIELD_VALUE = 1;
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        AssemblerImpl ae = new AssemblerImpl();

        Object model = new Object() {
        };
        Object dto = new Object() {
        };
        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());

        ae = spy(ae);
        doReturn(FIELD_VALUE).when(ae).getPropertyValue(model, FIELD);
        doThrow(new NoSuchMethodException()).when(ae).setPropertyValue(dto, DTO_FIELD, FIELD_VALUE);

        // Act
        ae.copyPropertyFromDTOToModel(dto, model, params);

    }

    @Test
    public void copyPropertiesFromDTOToModelShouldReturnFieldCopy() throws Exception {
        // Arrange
        final Integer FIELD_VALUE = 1;
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";

        AssemblerImpl ae = new AssemblerImpl();
        ae = spy(ae);

        TransferParams params = new TransferParams(DTO_FIELD, FIELD, new NoTransformation());
        Object model = new Object() {
        };
        Object dto = new Object() {
        };

        doReturn(FIELD_VALUE).when(ae).getPropertyValue(dto, DTO_FIELD);
        doNothing().when(ae).setPropertyValue(model, FIELD, FIELD_VALUE);

        // Act
        ae.copyPropertyFromDTOToModel(dto, model, params);

        // Assert
        verify(ae, times(1)).setPropertyValue(model, FIELD, FIELD_VALUE);
    }


    @Test(expected = NullPointerException.class)
    public void copyPropertyFromDTOToModelShouldThrowsNullPointerExceptionForNullParams() {
        // Arrange
        AssemblerImpl ae = new AssemblerImpl();

        Object model = new Object() {
            private String field = null;
        };

        Object dto = new Object() {
            private String field = null;
        };

        // Act
        ae.copyPropertyFromDTOToModel(dto, model, null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void copyPropertiesFromDTOToModelShouldReturnTransformedFieldCopy() throws Exception {
        // Arrange
        final Integer FIELD_VALUE = 1;
        final String FIELD = "field";
        final String DTO_FIELD = "dtoField";
        final String TRANSFORMED_VALUE = "1";

        AssemblerImpl ae = new AssemblerImpl();
        ae = spy(ae);

        Transformer transformer = new NoTransformation();
        transformer = spy(transformer);
        doReturn(TRANSFORMED_VALUE).when(transformer).reverse(FIELD_VALUE);

        TransferParams params = new TransferParams(DTO_FIELD, FIELD, transformer);
        Object model = new Object() {};
        Object dto = new Object() {};

        doReturn(FIELD_VALUE).when(ae).getPropertyValue(dto, DTO_FIELD);
        doNothing().when(ae).setPropertyValue(model, FIELD, TRANSFORMED_VALUE);


        // Act
        ae.copyPropertyFromDTOToModel(dto, model, params);

        // Assert
        verify(ae, times(1)).setPropertyValue(model, FIELD, TRANSFORMED_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void copyPropertiesFromDTOToModelShouldThrowsNullPointerExceptionForNullDTO() {
        // Arrange
        AssemblerImpl ae = new AssemblerImpl();

        Object model = new Object() {
            private String field = null;
        };

        TransferParams params = new TransferParams("field", "field", null);

        // Act
        ae.copyPropertyFromDTOToModel(null, model, params);

    }


    @Test(expected = NullPointerException.class)
    public void assembleShouldThrowNullPointerExceptionWhenModelIsNull(){

        // Arrange
        Object model = null;
        AssemblerImpl<DTOWithOneProperty> dto = new AssemblerImpl<DTOWithOneProperty>();
        // Act
        dto.assemble(model, DTOWithOneProperty.class);
    }

    @Test
    public void assembleShouldNotCallCopyPropertyForAnEmptyPropertyTransferParams() throws Exception{

        // Arrange
        final Class<DTOWithOneProperty> dtoClass = DTOWithOneProperty.class;

        Object model = new Object();

        AssemblerImpl<DTOWithOneProperty> ae = new AssemblerImpl<DTOWithOneProperty>();
        ae = spy(ae);
        doReturn(new ArrayList()).when(ae).loadPropertyTransferParams(dtoClass);

        // Act
        DTOWithOneProperty dto = ae.assemble(model, DTOWithOneProperty.class);

        // Assert
        assertNotNull(dto);
        verify(ae, times(1)).loadPropertyTransferParams(dtoClass);
        verify(ae, never()).copyPropertyFromModelToDTO(eq(model), any(), (TransferParams) any());
    }

    @Test
    public void assembleShouldCallCopyPropertyForAPropertyTransferParams() throws Exception{

        // Arrange
        final Class<DTOWithOneProperty> dtoClass = DTOWithOneProperty.class;
        final String DTO_FIELD = "dtoField";
        final String MODEL_FIELD = "modelField";

        Object model = new Object();
        TransferParams params = new TransferParams(DTO_FIELD, MODEL_FIELD, new NoTransformation());

        AssemblerImpl<DTOWithOneProperty> ae = new AssemblerImpl<DTOWithOneProperty>();
        ae = spy(ae);
        doReturn(Arrays.asList(params)).when(ae).loadPropertyTransferParams(dtoClass);
        doNothing().when(ae).copyPropertyFromModelToDTO(eq(model), any(), eq(params));

        // Act
        DTOWithOneProperty dto = ae.assemble(model, DTOWithOneProperty.class);

        // Assert
        assertNotNull(dto);
        verify(ae, times(1)).loadPropertyTransferParams(dtoClass);
        verify(ae, times(1)).copyPropertyFromModelToDTO(eq(model), any(), eq(params));
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

        AssemblerImpl<DTOWithOneProperty> ae = new AssemblerImpl<DTOWithOneProperty>();
        ae = spy(ae);
        doReturn(Arrays.asList(params1, params2)).when(ae).loadPropertyTransferParams(dtoClass);
        doNothing().when(ae).copyPropertyFromModelToDTO(eq(model), any(), eq(params1));
        doNothing().when(ae).copyPropertyFromModelToDTO(eq(model), any(), eq(params2));

        // Act
        DTOWithOneProperty dto = ae.assemble(model, DTOWithOneProperty.class);

        // Assert
        assertNotNull(dto);
        verify(ae, times(1)).loadPropertyTransferParams(dtoClass);
        verify(ae, times(1)).copyPropertyFromModelToDTO(eq(model), any(), eq(params1));
        verify(ae, times(1)).copyPropertyFromModelToDTO(eq(model), any(), eq(params2));
    }


    @Test
    public void assembleAllShouldNotCallCopyPropertyForAnEmptyPropertyTransferParams() throws Exception{

        // Arrange
        final Class<DTOWithOneProperty> dtoClass = DTOWithOneProperty.class;

        Object model = new Object();

        AssemblerImpl<DTOWithOneProperty> ae = new AssemblerImpl<DTOWithOneProperty>();
        ae = spy(ae);
        doReturn(new ArrayList()).when(ae).loadPropertyTransferParams(dtoClass);

        // Act
        DTOWithOneProperty dto = ae.assemble(model, DTOWithOneProperty.class);

        // Assert
        assertNotNull(dto);
        verify(ae, times(1)).loadPropertyTransferParams(dtoClass);
        verify(ae, never()).copyPropertyFromModelToDTO(eq(model), any(), (TransferParams) any());
    }


    @Test(expected = NullPointerException.class)
    public void assembleAllShouldThrowNullPointerExceptionWhenModelListIsNull(){

        // Arrange
        AssemblerImpl<DTOWithOneProperty> ae = new AssemblerImpl<DTOWithOneProperty>();

        // Act
        ae.assembleAll(null, DTOWithOneProperty.class);
    }

    @Test
    public void assembleAllShouldCallAssembleForEachModel(){

        // Arrange
        final Object model1 = new Object(){};
        final Object model2 = new Object(){};

        AssemblerImpl<DTOWithOneProperty> ae = new AssemblerImpl<DTOWithOneProperty>();
        Collection<Object> models = Arrays.asList( model1, model2);

        ae = spy(ae);
        doReturn(new DTOWithOneProperty()).when(ae).assemble(any(), eq(DTOWithOneProperty.class));

        // Act
        Collection<DTOWithOneProperty> dtos = ae.assembleAll(models, DTOWithOneProperty.class);

        // Assert
        assertEquals(2, dtos.size());
        verify(ae, times(1)).assemble(model1, DTOWithOneProperty.class);
        verify(ae, times(1)).assemble(model2, DTOWithOneProperty.class);
    }

}
