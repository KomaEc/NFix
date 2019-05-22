package polyglot.types;

public interface LocalInstance extends VarInstance {
   LocalInstance flags(Flags var1);

   LocalInstance name(String var1);

   LocalInstance type(Type var1);

   LocalInstance constantValue(Object var1);

   void setConstantValue(Object var1);
}
