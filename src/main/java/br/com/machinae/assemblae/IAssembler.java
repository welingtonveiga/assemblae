package br.com.machinae.assemblae;

import java.util.Collection;

/**
 * Assembler service interface.
 *
 * @param <T>
 */
public interface IAssembler<T> {

    T assemble(Object model, Class<T> dtoClass);

    Collection<T> assembleAll(Collection<Object> model, Class<T> dtoClass);
}
