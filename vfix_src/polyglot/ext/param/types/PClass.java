package polyglot.ext.param.types;

import java.util.List;
import polyglot.types.ClassType;
import polyglot.types.Importable;
import polyglot.types.SemanticException;
import polyglot.util.Position;

public interface PClass extends Importable {
   List formals();

   ClassType clazz();

   ClassType instantiate(Position var1, List var2) throws SemanticException;
}
