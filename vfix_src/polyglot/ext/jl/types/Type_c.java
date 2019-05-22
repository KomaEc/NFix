package polyglot.ext.jl.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import polyglot.types.ArrayType;
import polyglot.types.CachingResolver;
import polyglot.types.ClassType;
import polyglot.types.Named;
import polyglot.types.NullType;
import polyglot.types.Package;
import polyglot.types.PrimitiveType;
import polyglot.types.ReferenceType;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.util.TypeInputStream;

public abstract class Type_c extends TypeObject_c implements Type {
   protected Type_c() {
   }

   public Type_c(TypeSystem ts) {
      this(ts, (Position)null);
   }

   public Type_c(TypeSystem ts, Position pos) {
      super(ts, pos);
   }

   public abstract String translate(Resolver var1);

   public boolean isType() {
      return true;
   }

   public boolean isPackage() {
      return false;
   }

   public Type toType() {
      return this;
   }

   public Package toPackage() {
      return null;
   }

   public boolean isCanonical() {
      return false;
   }

   public boolean isPrimitive() {
      return false;
   }

   public boolean isNumeric() {
      return false;
   }

   public boolean isIntOrLess() {
      return false;
   }

   public boolean isLongOrLess() {
      return false;
   }

   public boolean isVoid() {
      return false;
   }

   public boolean isBoolean() {
      return false;
   }

   public boolean isChar() {
      return false;
   }

   public boolean isByte() {
      return false;
   }

   public boolean isShort() {
      return false;
   }

   public boolean isInt() {
      return false;
   }

   public boolean isLong() {
      return false;
   }

   public boolean isFloat() {
      return false;
   }

   public boolean isDouble() {
      return false;
   }

   public boolean isReference() {
      return false;
   }

   public boolean isNull() {
      return false;
   }

   public boolean isClass() {
      return false;
   }

   public boolean isArray() {
      return false;
   }

   public boolean isThrowable() {
      return false;
   }

   public boolean isUncheckedException() {
      return false;
   }

   public ClassType toClass() {
      return null;
   }

   public NullType toNull() {
      return null;
   }

   public ReferenceType toReference() {
      return null;
   }

   public PrimitiveType toPrimitive() {
      return null;
   }

   public ArrayType toArray() {
      return null;
   }

   public ArrayType arrayOf(int dims) {
      return this.ts.arrayOf(this, dims);
   }

   public ArrayType arrayOf() {
      return this.ts.arrayOf(this);
   }

   public final boolean isSubtype(Type t) {
      return this.ts.isSubtype(this, t);
   }

   public boolean isSubtypeImpl(Type t) {
      return this.ts.equals(this, t) || this.ts.descendsFrom(this, t);
   }

   public final boolean descendsFrom(Type t) {
      return this.ts.descendsFrom(this, t);
   }

   public boolean descendsFromImpl(Type t) {
      return false;
   }

   public final boolean isCastValid(Type toType) {
      return this.ts.isCastValid(this, toType);
   }

   public boolean isCastValidImpl(Type toType) {
      return false;
   }

   public final boolean isImplicitCastValid(Type toType) {
      return this.ts.isImplicitCastValid(this, toType);
   }

   public boolean isImplicitCastValidImpl(Type toType) {
      return false;
   }

   public final boolean numericConversionValid(long value) {
      return this.ts.numericConversionValid(this, value);
   }

   public boolean numericConversionValidImpl(long value) {
      return false;
   }

   public final boolean numericConversionValid(Object value) {
      return this.ts.numericConversionValid(this, value);
   }

   public boolean numericConversionValidImpl(Object value) {
      return false;
   }

   public boolean isComparable(Type t) {
      return t.typeSystem() == this.ts;
   }

   public abstract String toString();

   private void writeObject(ObjectOutputStream out) throws IOException {
      if (this instanceof Named) {
         String name = ((Named)this).fullName();
         out.writeObject(name);
         String memberName = null;
         if (name != null && this.isClass() && this.toClass().isMember()) {
            memberName = this.typeSystem().getTransformedClassName(this.toClass());
         }

         out.writeObject(memberName);
      }

      out.defaultWriteObject();
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      if (this instanceof Named) {
         String name = (String)in.readObject();
         String memberName = (String)in.readObject();
         TypeSystem ts = ((TypeInputStream)in).getTypeSystem();
         if (name != null) {
            ((CachingResolver)ts.systemResolver()).install(name, (Named)this);
         }

         if (memberName != null) {
            ((CachingResolver)ts.systemResolver()).install(memberName, (Named)this);
         }
      }

      in.defaultReadObject();
   }
}
