package br.com.machinae.assemblae;

/**
 * Dummy trasnformer for No Transformation.
 *
 * @author Welington Veiga
 * @since 02/11/2012
 * @version 1.0.0
 */
public class NoTransformation implements Transformer<Object, Object> {

    @Override
    public Object transform(Object data) {
        return data;
    }

    @Override
    public Object reverse(Object data) {
        return data;
    }
}
