package polyglot.types;

import polyglot.util.Enum;

public interface PrimitiveType extends Type, Named {
   PrimitiveType.Kind VOID = new PrimitiveType.Kind("void");
   PrimitiveType.Kind BOOLEAN = new PrimitiveType.Kind("boolean");
   PrimitiveType.Kind BYTE = new PrimitiveType.Kind("byte");
   PrimitiveType.Kind CHAR = new PrimitiveType.Kind("char");
   PrimitiveType.Kind SHORT = new PrimitiveType.Kind("short");
   PrimitiveType.Kind INT = new PrimitiveType.Kind("int");
   PrimitiveType.Kind LONG = new PrimitiveType.Kind("long");
   PrimitiveType.Kind FLOAT = new PrimitiveType.Kind("float");
   PrimitiveType.Kind DOUBLE = new PrimitiveType.Kind("double");

   PrimitiveType.Kind kind();

   String wrapperTypeString(TypeSystem var1);

   public static class Kind extends Enum {
      public Kind(String name) {
         super(name);
      }
   }
}
