package polyglot.types;

public interface LazyClassInitializer {
   boolean fromClassFile();

   void initConstructors(ParsedClassType var1);

   void initMethods(ParsedClassType var1);

   void initFields(ParsedClassType var1);

   void initMemberClasses(ParsedClassType var1);

   void initInterfaces(ParsedClassType var1);
}
