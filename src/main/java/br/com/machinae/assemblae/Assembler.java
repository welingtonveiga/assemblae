package br.com.machinae.assemblae;

import java.util.Collection;

/**
 * Assembler service interface.
 *
 * @param <T>
 */
public interface Assembler<T> {

    T assemble(Object model, Class<T> dtoClass);

    Collection<T> assembleAll(Collection<Object> model, Class<T> dtoClass);
}
