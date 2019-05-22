package polyglot.ext.param.types;

import java.util.List;
import polyglot.types.Type;

public interface InstType extends Type {
   PClass instantiatedFrom();

   List actuals();
}
