package polyglot.types;

import polyglot.frontend.Source;
import polyglot.util.Position;

public interface ParsedClassType extends ClassType {
   void position(Position var1);

   Source fromSource();

   void package_(Package var1);

   void superType(Type var1);

   void addInterface(Type var1);

   void addField(FieldInstance var1);

   void addMethod(MethodInstance var1);

   void addConstructor(ConstructorInstance var1);

   void addMemberClass(ClassType var1);

   void flags(Flags var1);

   void outer(ClassType var1);

   void name(String var1);

   void kind(ClassType.Kind var1);

   void inStaticContext(boolean var1);
}
