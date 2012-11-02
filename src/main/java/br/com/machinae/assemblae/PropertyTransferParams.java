package br.com.machinae.assemblae;

/**
 * Model that retain all property map configurations for a single Property.
 *
 * @author Welington Veiga
 * @version 1.0.0
 */
class PropertyTransferParams {

    private final String modelProperty;

    private final String dtoProperty;

    private Transformer<?,?> transformer;

    PropertyTransferParams(final String modelProperty, final String dtoProperty, final Transformer<?,?> transformer)
    {
        this.modelProperty = modelProperty;
        this.dtoProperty = dtoProperty;
        this.transformer = transformer;
    }


    String getModelProperty()
    {
        return modelProperty;
    }

    String getDtoProperty()
    {
        return dtoProperty;
    }

    public Transformer<?,?> getTransformer() {
        return transformer;
    }
}
