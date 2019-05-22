package polyglot.ext.param.types;

import java.util.List;
import polyglot.types.ClassType;

public interface MuPClass extends PClass {
   void formals(List var1);

   void addFormal(Param var1);

   void clazz(ClassType var1);
}
