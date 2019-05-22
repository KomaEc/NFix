package org.codehaus.groovy.groovydoc;

public interface GroovyClassDoc extends GroovyType, GroovyProgramElementDoc {
   GroovyConstructorDoc[] constructors();

   GroovyConstructorDoc[] constructors(boolean var1);

   boolean definesSerializableFields();

   GroovyFieldDoc[] enumConstants();

   GroovyFieldDoc[] fields();

   GroovyFieldDoc[] properties();

   GroovyFieldDoc[] fields(boolean var1);

   GroovyClassDoc findClass(String var1);

   GroovyClassDoc[] importedClasses();

   GroovyPackageDoc[] importedPackages();

   GroovyClassDoc[] innerClasses();

   GroovyClassDoc[] innerClasses(boolean var1);

   GroovyClassDoc[] interfaces();

   GroovyType[] interfaceTypes();

   boolean isAbstract();

   boolean isExternalizable();

   boolean isSerializable();

   GroovyMethodDoc[] methods();

   GroovyMethodDoc[] methods(boolean var1);

   GroovyFieldDoc[] serializableFields();

   GroovyMethodDoc[] serializationMethods();

   boolean subclassOf(GroovyClassDoc var1);

   GroovyClassDoc superclass();

   GroovyType superclassType();

   String getFullPathName();

   String getRelativeRootPath();
}
