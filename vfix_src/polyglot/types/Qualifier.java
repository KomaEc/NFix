package polyglot.types;

public interface Qualifier extends TypeObject {
   boolean isPackage();

   Package toPackage();

   boolean isType();

   Type toType();
}
