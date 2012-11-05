package br.com.machinae.assemblae.tests;

import br.com.machinae.assemblae.annotation.DataTransferObject;
import br.com.machinae.assemblae.annotation.Ignore;

/**
 * DTO with one property ignored for testing.
 */

@DataTransferObject
public class DTOWithOneIgnoredProperty {

    @Ignore
    private Integer field;


    public Integer getField() {
        return field;
    }

    public void setField(Integer fieldB) {
        this.field = fieldB;
    }
}
