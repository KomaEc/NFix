package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import soot.Type;

public interface IHierarchy {
   Collection<Type> lcas(Type var1, Type var2);

   boolean ancestor(Type var1, Type var2);
}
