package br.com.machinae.assemblae;

import br.com.machinae.assemblae.annotation.DataTransferObject;
import br.com.machinae.assemblae.annotation.Ignore;

import java.lang.reflect.Field;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utlitário para transformar um modelo em um objeto de transferência (DTO), e para  atualizar
 * o modelo a partir de um DTO.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms978717.aspx">referência</a>
 * @see <a href="http://java.sun.com/blueprints/corej2eepatterns/Patterns/TransferObjectAssembler.html">referência</a>
 * @see <a href="http://www.inspire-software.com/en/index/view/open-source-GeDA-generic-DTO-assembler.html> Inspiração </a>
 *
 * @author Welington Veiga
 * @since 21/10/2012
 * @version 1.0.0
 *
 */
public class Assemblae {

    private final Map<String, Transformer> transformerInstances = new HashMap<String, Transformer>();

    public Collection<PropertyTransferParams> loadPropertyTransferParams(Class<?> dtoClass)
    {
        checkNotNull(dtoClass);
        checkArgument(dtoClass.isAnnotationPresent(DataTransferObject.class));

        Set<PropertyTransferParams> propertyTransferParams = new HashSet<PropertyTransferParams>();

        final Field[] fields = dtoClass.getDeclaredFields();
        for(Field field : fields)
            if(!field.isAnnotationPresent(Ignore.class))
                propertyTransferParams.add(PropertyTransferParams.build(field));

        return propertyTransferParams;
    }

}
