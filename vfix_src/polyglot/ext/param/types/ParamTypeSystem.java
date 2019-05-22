package polyglot.ext.param.types;

import java.util.List;
import java.util.Map;
import polyglot.types.ClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;

public interface ParamTypeSystem extends TypeSystem {
   MuPClass mutablePClass(Position var1);

   ClassType instantiate(Position var1, PClass var2, List var3) throws SemanticException;

   Type subst(Type var1, Map var2);

   Type subst(Type var1, Map var2, Map var3);

   Subst subst(Map var1, Map var2);
}
