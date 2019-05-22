package polyglot.types;

import java.util.List;

public interface ConstructorInstance extends ProcedureInstance {
   ConstructorInstance flags(Flags var1);

   ConstructorInstance formalTypes(List var1);

   ConstructorInstance throwTypes(List var1);

   ConstructorInstance container(ClassType var1);
}
