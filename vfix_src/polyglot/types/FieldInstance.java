package polyglot.types;

public interface FieldInstance extends VarInstance, MemberInstance {
   FieldInstance flags(Flags var1);

   FieldInstance name(String var1);

   FieldInstance type(Type var1);

   FieldInstance container(ReferenceType var1);

   FieldInstance constantValue(Object var1);

   void setConstantValue(Object var1);
}
