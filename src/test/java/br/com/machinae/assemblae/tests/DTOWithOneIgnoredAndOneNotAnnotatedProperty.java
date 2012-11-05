package br.com.machinae.assemblae.tests;

import br.com.machinae.assemblae.annotation.DataTransferObject;
import br.com.machinae.assemblae.annotation.Ignore;

/**
 * DTO with one property ignored and a not annotated property for testing.
 */

@DataTransferObject
public class DTOWithOneIgnoredAndOneNotAnnotatedProperty {

    private Integer field;

    @Ignore
    private Integer ignored;


    public Integer getField() {
        return field;
    }

    public void setField(Integer fieldB) {
        this.field = fieldB;
    }

    public Integer getIgnored() {
        return ignored;
    }

    public void setIgnored(Integer ignored) {
        this.ignored = ignored;
    }
}
