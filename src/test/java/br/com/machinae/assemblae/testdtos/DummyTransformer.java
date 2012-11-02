package br.com.machinae.assemblae.testdtos;

import br.com.machinae.assemblae.Transformer;

/**
* Created with IntelliJ IDEA.
* User: welingtonveiga
* Date: 02/11/12
* Time: 21:03
* To change this template use File | Settings | File Templates.
*/
public class DummyTransformer implements Transformer<Object,Object>
{
    @Override
    public Object transform(Object data) { return null;}

    @Override
    public Object reverse(Object data) { return null;}
}
