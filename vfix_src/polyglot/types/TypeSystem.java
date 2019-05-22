package polyglot.types;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Source;
import polyglot.util.Position;

public interface TypeSystem {
   void initialize(LoadedClassResolver var1, ExtensionInfo var2) throws SemanticException;

   TopLevelResolver systemResolver();

   TableResolver parsedResolver();

   LoadedClassResolver loadedResolver();

   ImportTable importTable(String var1, Package var2);

   ImportTable importTable(Package var1);

   List defaultPackageImports();

   boolean packageExists(String var1);

   Named forName(String var1) throws SemanticException;

   Type typeForName(String var1) throws SemanticException;

   InitializerInstance initializerInstance(Position var1, ClassType var2, Flags var3);

   ConstructorInstance constructorInstance(Position var1, ClassType var2, Flags var3, List var4, List var5);

   MethodInstance methodInstance(Position var1, ReferenceType var2, Flags var3, Type var4, String var5, List var6, List var7);

   FieldInstance fieldInstance(Position var1, ReferenceType var2, Flags var3, Type var4, String var5);

   LocalInstance localInstance(Position var1, Flags var2, Type var3, String var4);

   ConstructorInstance defaultConstructor(Position var1, ClassType var2);

   UnknownType unknownType(Position var1);

   UnknownPackage unknownPackage(Position var1);

   UnknownQualifier unknownQualifier(Position var1);

   boolean isSubtype(Type var1, Type var2);

   boolean descendsFrom(Type var1, Type var2);

   boolean isCastValid(Type var1, Type var2);

   boolean isImplicitCastValid(Type var1, Type var2);

   boolean equals(TypeObject var1, TypeObject var2);

   boolean numericConversionValid(Type var1, long var2);

   boolean numericConversionValid(Type var1, Object var2);

   Type leastCommonAncestor(Type var1, Type var2) throws SemanticException;

   boolean isCanonical(Type var1);

   boolean isAccessible(MemberInstance var1, Context var2);

   boolean classAccessible(ClassType var1, Context var2);

   boolean classAccessibleFromPackage(ClassType var1, Package var2);

   boolean isEnclosed(ClassType var1, ClassType var2);

   boolean hasEnclosingInstance(ClassType var1, ClassType var2);

   boolean canCoerceToString(Type var1, Context var2);

   boolean isThrowable(Type var1);

   boolean isUncheckedException(Type var1);

   Collection uncheckedExceptions();

   PrimitiveType promote(Type var1) throws SemanticException;

   PrimitiveType promote(Type var1, Type var2) throws SemanticException;

   /** @deprecated */
   FieldInstance findField(ReferenceType var1, String var2, Context var3) throws SemanticException;

   FieldInstance findField(ReferenceType var1, String var2, ClassType var3) throws SemanticException;

   FieldInstance findField(ReferenceType var1, String var2) throws SemanticException;

   MethodInstance findMethod(ReferenceType var1, String var2, List var3, ClassType var4) throws SemanticException;

   /** @deprecated */
   MethodInstance findMethod(ReferenceType var1, String var2, List var3, Context var4) throws SemanticException;

   ConstructorInstance findConstructor(ClassType var1, List var2, ClassType var3) throws SemanticException;

   /** @deprecated */
   ConstructorInstance findConstructor(ClassType var1, List var2, Context var3) throws SemanticException;

   ClassType findMemberClass(ClassType var1, String var2, ClassType var3) throws SemanticException;

   /** @deprecated */
   ClassType findMemberClass(ClassType var1, String var2, Context var3) throws SemanticException;

   ClassType findMemberClass(ClassType var1, String var2) throws SemanticException;

   Type superType(ReferenceType var1);

   List interfaces(ReferenceType var1);

   boolean throwsSubset(ProcedureInstance var1, ProcedureInstance var2);

   boolean hasMethod(ReferenceType var1, MethodInstance var2);

   boolean hasMethodNamed(ReferenceType var1, String var2);

   boolean isSameMethod(MethodInstance var1, MethodInstance var2);

   boolean moreSpecific(ProcedureInstance var1, ProcedureInstance var2);

   boolean hasFormals(ProcedureInstance var1, List var2);

   NullType Null();

   PrimitiveType Void();

   PrimitiveType Boolean();

   PrimitiveType Char();

   PrimitiveType Byte();

   PrimitiveType Short();

   PrimitiveType Int();

   PrimitiveType Long();

   PrimitiveType Float();

   PrimitiveType Double();

   ClassType Object();

   ClassType String();

   ClassType Class();

   ClassType Throwable();

   ClassType Error();

   ClassType Exception();

   ClassType RuntimeException();

   ClassType Cloneable();

   ClassType Serializable();

   ClassType NullPointerException();

   ClassType ClassCastException();

   ClassType OutOfBoundsException();

   ClassType ArrayStoreException();

   ClassType ArithmeticException();

   ArrayType arrayOf(Type var1);

   ArrayType arrayOf(Position var1, Type var2);

   ArrayType arrayOf(Type var1, int var2);

   ArrayType arrayOf(Position var1, Type var2, int var3);

   Package packageForName(String var1) throws SemanticException;

   Package packageForName(Package var1, String var2) throws SemanticException;

   Package createPackage(String var1);

   Package createPackage(Package var1, String var2);

   Context createContext();

   Resolver packageContextResolver(Resolver var1, Package var2);

   Resolver classContextResolver(ClassType var1);

   LazyClassInitializer defaultClassInitializer();

   ParsedClassType createClassType(LazyClassInitializer var1);

   ParsedClassType createClassType();

   ParsedClassType createClassType(LazyClassInitializer var1, Source var2);

   ParsedClassType createClassType(Source var1);

   Set getTypeEncoderRootSet(Type var1);

   String getTransformedClassName(ClassType var1);

   Object placeHolder(TypeObject var1, Set var2);

   Object placeHolder(TypeObject var1);

   String translatePackage(Resolver var1, Package var2);

   String translatePrimitive(Resolver var1, PrimitiveType var2);

   String translateArray(Resolver var1, ArrayType var2);

   String translateClass(Resolver var1, ClassType var2);

   String wrapperTypeString(PrimitiveType var1);

   boolean methodCallValid(MethodInstance var1, String var2, List var3);

   boolean callValid(ProcedureInstance var1, List var2);

   List overrides(MethodInstance var1);

   boolean canOverride(MethodInstance var1, MethodInstance var2);

   void checkOverride(MethodInstance var1, MethodInstance var2) throws SemanticException;

   List implemented(MethodInstance var1);

   PrimitiveType primitiveForName(String var1) throws SemanticException;

   void checkMethodFlags(Flags var1) throws SemanticException;

   void checkLocalFlags(Flags var1) throws SemanticException;

   void checkFieldFlags(Flags var1) throws SemanticException;

   void checkConstructorFlags(Flags var1) throws SemanticException;

   void checkInitializerFlags(Flags var1) throws SemanticException;

   void checkTopLevelClassFlags(Flags var1) throws SemanticException;

   void checkMemberClassFlags(Flags var1) throws SemanticException;

   void checkLocalClassFlags(Flags var1) throws SemanticException;

   void checkAccessFlags(Flags var1) throws SemanticException;

   void checkCycles(ReferenceType var1) throws SemanticException;

   void checkClassConformance(ClassType var1) throws SemanticException;

   Type staticTarget(Type var1);

   Flags flagsForBits(int var1);

   Flags createNewFlag(String var1, Flags var2);

   Flags NoFlags();

   Flags Public();

   Flags Protected();

   Flags Private();

   Flags Static();

   Flags Final();

   Flags Synchronized();

   Flags Transient();

   Flags Native();

   Flags Interface();

   Flags Abstract();

   Flags Volatile();

   Flags StrictFP();
}
