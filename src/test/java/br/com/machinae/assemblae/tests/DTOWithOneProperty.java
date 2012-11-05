package br.com.machinae.assemblae.tests;

import br.com.machinae.assemblae.annotation.DataTransferObject;

/**
 * DTO with one field for testing
 */
@DataTransferObject
public class DTOWithOneProperty {

    public Integer field;


    public Integer getField() {
        return field;
    }

    public void setField(Integer field) {
        this.field = field;
    }
}
