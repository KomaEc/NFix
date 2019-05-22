package polyglot.types;

import java.util.List;

public interface MethodInstance extends ProcedureInstance {
   Type returnType();

   MethodInstance returnType(Type var1);

   String name();

   MethodInstance name(String var1);

   MethodInstance flags(Flags var1);

   MethodInstance formalTypes(List var1);

   MethodInstance throwTypes(List var1);

   MethodInstance container(ReferenceType var1);

   List overrides();

   boolean canOverride(MethodInstance var1);

   void checkOverride(MethodInstance var1) throws SemanticException;

   List implemented();

   boolean isSameMethod(MethodInstance var1);

   boolean methodCallValid(String var1, List var2);

   List overridesImpl();

   boolean canOverrideImpl(MethodInstance var1, boolean var2) throws SemanticException;

   List implementedImpl(ReferenceType var1);

   boolean isSameMethodImpl(MethodInstance var1);

   boolean methodCallValidImpl(String var1, List var2);
}
