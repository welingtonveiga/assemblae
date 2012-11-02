package br.com.machinae.assemblae.testdtos;

import br.com.machinae.assemblae.annotation.DataTransferObject;
import br.com.machinae.assemblae.annotation.MappedProperty;

/**
 * DTO with one field for testing
 */
@DataTransferObject
public class DTOWithOneNamedMappedProperty {

    @MappedProperty(to = "anotherField")
    private Integer field;


    public Integer getField() {
        return field;
    }

    public void setField(Integer field) {
        this.field = field;
    }
}
