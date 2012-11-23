package br.com.machinae.assemblae;

import java.util.Collection;

/**
 * Utility to transform a model in a transfer object (DTO), and to update the model from a DTO.
 *
 * @author Welington Veiga
 * @version 1.0.0
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms978717.aspx">referência</a>
 * @see <a href="http://java.sun.com/blueprints/corej2eepatterns/Patterns/TransferObjectAssembler.html">referência</a>
 * @see <a href="http://www.inspire-software.com/en/index/view/open-source-GeDA-generic-DTO-assembler.html> Inspiração </a>
 * @since 21/10/2012
 */
public class Assemblae {

    private Assemblae(){}

    private static Assembler instance;

    /**
     * Define a Assemblae instance for exposed static assemble methods.
     * @param ae Assemblae instance
     */
    static void setInstance(Assembler ae) {
        instance = ae;
    }

    /**
     * A Assemblae instance for exposed static assemble methods.
     * @return Assemblae instance
     */
    static Assembler getInstance() {
        if(instance == null)
            instance = new AssemblerImpl();
        return instance;
    }

    public static <T> T assemble(Object model, Class<T> dtoClass) {
        Assembler<T> ae = new AssemblerImpl<T>();
        return  ae.assemble(model, dtoClass);
    }

    public static <T> Collection<T> assembleAll(Collection<Object> models, Class<T> dtoClass){
        Assembler<T> ae = new AssemblerImpl<T>();
        return ae.assembleAll(models, dtoClass);
    }

}
