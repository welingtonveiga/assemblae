package br.com.machinae.assemblae;

/**
 *  Interface for transformation between types and representations of data.
 *
 *  @author Welington Veiga
 *  @since 24/10/2012
 *  @version 1.0.0
 *
 */
public interface Transformer<MODELTYPE, DTOTYPE> {

    /**
     *  Method that convert MODEL type/representation to DTO type/representation
     *
     * @param data a data representation on model
     * @return  the data converted to a DTO representation
     */
    DTOTYPE transform(MODELTYPE data);

    /**
     *  Method that convert DTO type/representation to MODEL type/representation
     *
     * @param data a data representation on dto
     * @return  the data converted to a model representation
     */
    MODELTYPE reverse(DTOTYPE data);
}
