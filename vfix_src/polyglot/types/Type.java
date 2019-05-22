package polyglot.types;

public interface Type extends Qualifier {
   String translate(Resolver var1);

   ArrayType arrayOf();

   ArrayType arrayOf(int var1);

   ClassType toClass();

   NullType toNull();

   ReferenceType toReference();

   PrimitiveType toPrimitive();

   ArrayType toArray();

   boolean isSubtype(Type var1);

   boolean descendsFrom(Type var1);

   boolean isCastValid(Type var1);

   boolean isImplicitCastValid(Type var1);

   boolean numericConversionValid(Object var1);

   boolean numericConversionValid(long var1);

   boolean isSubtypeImpl(Type var1);

   boolean descendsFromImpl(Type var1);

   boolean isCastValidImpl(Type var1);

   boolean isImplicitCastValidImpl(Type var1);

   boolean numericConversionValidImpl(Object var1);

   boolean numericConversionValidImpl(long var1);

   boolean isPrimitive();

   boolean isVoid();

   boolean isBoolean();

   boolean isChar();

   boolean isByte();

   boolean isShort();

   boolean isInt();

   boolean isLong();

   boolean isFloat();

   boolean isDouble();

   boolean isIntOrLess();

   boolean isLongOrLess();

   boolean isNumeric();

   boolean isReference();

   boolean isNull();

   boolean isArray();

   boolean isClass();

   boolean isThrowable();

   boolean isUncheckedException();

   boolean isComparable(Type var1);

   String toString();
}
