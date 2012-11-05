package br.com.machinae.assemblae.annotation;

import br.com.machinae.assemblae.Transformer;

import java.lang.annotation.Annotation;

public class MappedPropertyImpl implements Annotation, MappedProperty {
    private String to;

    public MappedPropertyImpl(String to) {
        this.to = to;
    }

    @Override
    public String to() {
        return to;
    }

    @Override
    public Class<? extends Transformer> transformer() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return MappedProperty.class;
    }
}