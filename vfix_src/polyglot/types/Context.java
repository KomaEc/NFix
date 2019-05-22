package polyglot.types;

import java.util.List;
import polyglot.util.Copy;

public interface Context extends Resolver, Copy {
   TypeSystem typeSystem();

   void addVariable(VarInstance var1);

   void addMethod(MethodInstance var1);

   void addNamed(Named var1);

   MethodInstance findMethod(String var1, List var2) throws SemanticException;

   VarInstance findVariable(String var1) throws SemanticException;

   VarInstance findVariableSilent(String var1);

   LocalInstance findLocal(String var1) throws SemanticException;

   FieldInstance findField(String var1) throws SemanticException;

   ClassType findFieldScope(String var1) throws SemanticException;

   ClassType findMethodScope(String var1) throws SemanticException;

   ImportTable importTable();

   Resolver outerResolver();

   Context pushSource(ImportTable var1);

   Context pushClass(ParsedClassType var1, ClassType var2);

   Context pushCode(CodeInstance var1);

   Context pushBlock();

   Context pushStatic();

   Context pop();

   boolean inCode();

   boolean isLocal(String var1);

   boolean inStaticContext();

   ClassType currentClass();

   ParsedClassType currentClassScope();

   CodeInstance currentCode();

   Package package_();
}
