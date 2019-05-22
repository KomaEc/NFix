package polyglot.types;

public interface ArrayType extends ReferenceType {
   Type base();

   ArrayType base(Type var1);

   Type ultimateBase();

   FieldInstance lengthField();

   MethodInstance cloneMethod();

   int dims();
}
