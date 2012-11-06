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
public class Assemblae2 {

    Assemblae2(){}

    private static Assembler instance;

    /**
     * Define a Assemblae2 instance for exposed static assemble methods.
     * @param ae Assemblae2 instance
     */
    static void setInstance(Assembler ae) {
        instance = ae;
    }

    /**
     * A Assemblae2 instance for exposed static assemble methods.
     * @return Assemblae2 instance
     */
    static Assembler getInstance() {
        if(instance == null)
            instance = new Assembler();
        return instance;
    }

    public static <T> T assemble(Object model, Class<T> dtoClass) {
        Assembler<T> ae = new Assembler<T>();
        return  ae.assemble(model, dtoClass);
    }

    public static <T> Collection<T> assembleAll(Collection<T> model, Class<T> dtoClass){
        return null;
    }

}
