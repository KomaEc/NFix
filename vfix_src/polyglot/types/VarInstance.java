package polyglot.types;

public interface VarInstance extends TypeObject {
   Flags flags();

   String name();

   Type type();

   Object constantValue();

   boolean isConstant();

   void setType(Type var1);

   void setFlags(Flags var1);
}
