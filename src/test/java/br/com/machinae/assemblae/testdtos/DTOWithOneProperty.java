package br.com.machinae.assemblae.testdtos;

import br.com.machinae.assemblae.annotation.DataTransferObject;

/**
 * DTO with one field for testing
 */
@DataTransferObject
public class DTOWithOneProperty {

    private Integer field;


    public Integer getField() {
        return field;
    }

    public void setField(Integer field) {
        this.field = field;
    }
}
