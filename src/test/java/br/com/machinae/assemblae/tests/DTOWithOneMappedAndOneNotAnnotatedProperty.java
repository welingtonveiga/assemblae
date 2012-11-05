package br.com.machinae.assemblae.tests;

import br.com.machinae.assemblae.annotation.DataTransferObject;
import br.com.machinae.assemblae.annotation.MappedProperty;

/**
 * DTO with one property ignored and a not annotated property for testing.
 */

@DataTransferObject
public class DTOWithOneMappedAndOneNotAnnotatedProperty {

    private Integer field;

    @MappedProperty
    private Integer mapped;


    public Integer getField() {
        return field;
    }

    public void setField(Integer fieldB) {
        this.field = fieldB;
    }


    public Integer getMapped() {
        return mapped;
    }

    public void setMapped(Integer mapped) {
        this.mapped = mapped;
    }
}
