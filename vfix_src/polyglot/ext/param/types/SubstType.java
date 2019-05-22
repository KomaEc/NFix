package polyglot.ext.param.types;

import java.util.Iterator;
import polyglot.types.Type;

public interface SubstType extends Type {
   Type base();

   Subst subst();

   Iterator entries();
}
