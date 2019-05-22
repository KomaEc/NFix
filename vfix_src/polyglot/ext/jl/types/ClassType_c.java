package polyglot.ext.jl.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import polyglot.main.Options;
import polyglot.types.ClassType;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.Named;
import polyglot.types.Package;
import polyglot.types.ReferenceType;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.Translator;

public abstract class ClassType_c extends ReferenceType_c implements ClassType {
   protected ClassType_c() {
   }

   public ClassType_c(TypeSystem ts) {
      this(ts, (Position)null);
   }

   public ClassType_c(TypeSystem ts, Position pos) {
      super(ts, pos);
   }

   public abstract ClassType.Kind kind();

   public abstract ClassType outer();

   public abstract String name();

   public ReferenceType container() {
      if (!this.isMember()) {
         throw new InternalCompilerError("Non-member classes cannot have container classes.");
      } else if (this.outer() == null) {
         throw new InternalCompilerError("Nested classes must have outer classes.");
      } else {
         return this.outer();
      }
   }

   public String fullName() {
      String name;
      if (this.isAnonymous()) {
         if (this.superType() != null) {
            name = "<anon subtype of " + this.superType().toString() + ">";
         } else {
            name = "<anon subtype of unknown>";
         }
      } else {
         name = this.name();
      }

      if (this.isTopLevel() && this.package_() != null) {
         return this.package_().fullName() + "." + name;
      } else {
         return this.isMember() && this.container() instanceof Named ? ((Named)this.container()).fullName() + "." + name : name;
      }
   }

   public boolean isTopLevel() {
      return this.kind() == ClassType.TOP_LEVEL;
   }

   public boolean isMember() {
      return this.kind() == ClassType.MEMBER;
   }

   public boolean isLocal() {
      return this.kind() == ClassType.LOCAL;
   }

   public boolean isAnonymous() {
      return this.kind() == ClassType.ANONYMOUS;
   }

   /** @deprecated */
   public final boolean isInner() {
      return this.isNested();
   }

   public boolean isNested() {
      return this.kind() == ClassType.MEMBER || this.kind() == ClassType.LOCAL || this.kind() == ClassType.ANONYMOUS;
   }

   public boolean isInnerClass() {
      return !this.flags().isInterface() && this.isNested() && !this.flags().isStatic() && !this.inStaticContext();
   }

   public boolean isCanonical() {
      return true;
   }

   public boolean isClass() {
      return true;
   }

   public ClassType toClass() {
      return this;
   }

   public abstract Package package_();

   public abstract Flags flags();

   public abstract List constructors();

   public abstract List memberClasses();

   public abstract List methods();

   public abstract List fields();

   public abstract List interfaces();

   public abstract Type superType();

   public FieldInstance fieldNamed(String name) {
      Iterator i = this.fields().iterator();

      FieldInstance fi;
      do {
         if (!i.hasNext()) {
            return null;
         }

         fi = (FieldInstance)i.next();
      } while(!fi.name().equals(name));

      return fi;
   }

   public ClassType memberClassNamed(String name) {
      Iterator i = this.memberClasses().iterator();

      ClassType t;
      do {
         if (!i.hasNext()) {
            return null;
         }

         t = (ClassType)i.next();
      } while(!t.name().equals(name));

      return t;
   }

   public boolean descendsFromImpl(Type ancestor) {
      if (!ancestor.isCanonical()) {
         return false;
      } else if (ancestor.isNull()) {
         return false;
      } else if (this.ts.equals(this, ancestor)) {
         return false;
      } else if (!ancestor.isReference()) {
         return false;
      } else if (this.ts.equals(ancestor, this.ts.Object())) {
         return true;
      } else {
         if (!this.flags().isInterface()) {
            if (this.ts.equals(this, this.ts.Object())) {
               return false;
            }

            if (this.superType() == null) {
               return false;
            }

            if (this.ts.isSubtype(this.superType(), ancestor)) {
               return true;
            }
         }

         Iterator i = this.interfaces().iterator();

         Type parentType;
         do {
            if (!i.hasNext()) {
               return false;
            }

            parentType = (Type)i.next();
         } while(!this.ts.isSubtype(parentType, ancestor));

         return true;
      }
   }

   public boolean isThrowable() {
      return this.ts.isSubtype(this, this.ts.Throwable());
   }

   public boolean isUncheckedException() {
      if (this.isThrowable()) {
         Collection c = this.ts.uncheckedExceptions();
         Iterator i = c.iterator();

         while(i.hasNext()) {
            Type t = (Type)i.next();
            if (this.ts.isSubtype(this, t)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isImplicitCastValidImpl(Type toType) {
      return !toType.isClass() ? false : this.ts.isSubtype(this, toType);
   }

   public boolean isCastValidImpl(Type toType) {
      if (!toType.isCanonical()) {
         return false;
      } else if (!toType.isReference()) {
         return false;
      } else if (toType.isArray()) {
         return this.ts.isSubtype(toType, this);
      } else if (!toType.isClass()) {
         return false;
      } else {
         boolean fromInterface = this.flags().isInterface();
         boolean toInterface = toType.toClass().flags().isInterface();
         boolean fromFinal = this.flags().isFinal();
         boolean toFinal = toType.toClass().flags().isFinal();
         if (!fromInterface) {
            if (toInterface) {
               return fromFinal ? this.ts.isSubtype(this, toType) : true;
            } else {
               return this.ts.isSubtype(this, toType) || this.ts.isSubtype(toType, this);
            }
         } else if (!toInterface && !toFinal) {
            return true;
         } else {
            return toFinal ? this.ts.isSubtype(toType, this) : true;
         }
      }
   }

   public final boolean isEnclosed(ClassType maybe_outer) {
      return this.ts.isEnclosed(this, maybe_outer);
   }

   public final boolean hasEnclosingInstance(ClassType encl) {
      return this.ts.hasEnclosingInstance(this, encl);
   }

   public String translate(Resolver c) {
      Named x;
      if (this.isTopLevel()) {
         if (this.package_() == null) {
            return this.name();
         } else {
            if (c != null) {
               try {
                  x = c.find(this.name());
                  if (this.ts.equals(this, x)) {
                     return this.name();
                  }
               } catch (SemanticException var3) {
               }
            }

            return Options.global.cppBackend() ? Translator.cScope(this.package_().translate(c) + "." + this.name()) : this.package_().translate(c) + "." + this.name();
         }
      } else if (this.isMember()) {
         if (this.container().toClass().isAnonymous()) {
            return this.name();
         } else {
            if (c != null) {
               try {
                  x = c.find(this.name());
                  if (this.ts.equals(this, x)) {
                     return this.name();
                  }
               } catch (SemanticException var4) {
               }
            }

            return this.container().translate(c) + "." + this.name();
         }
      } else if (this.isLocal()) {
         return this.name();
      } else {
         throw new InternalCompilerError("Cannot translate an anonymous class.");
      }
   }

   public String toString() {
      if (this.isTopLevel()) {
         return this.package_() != null ? this.package_().toString() + "." + this.name() : this.name();
      } else if (this.isMember()) {
         return this.container().toString() + "." + this.name();
      } else if (this.isLocal()) {
         return this.name();
      } else {
         return this.superType() != null ? "<anon subtype of " + this.superType().toString() + ">" : "<anon subtype of unknown>";
      }
   }

   public boolean isEnclosedImpl(ClassType maybe_outer) {
      if (this.isTopLevel()) {
         return false;
      } else if (this.outer() == null) {
         throw new InternalCompilerError("Non top-level classes must have outer classes.");
      } else {
         return this.outer().equals(maybe_outer) || this.outer().isEnclosed(maybe_outer);
      }
   }

   public boolean hasEnclosingInstanceImpl(ClassType encl) {
      if (this.equals(encl)) {
         return true;
      } else {
         return this.isInnerClass() && !this.inStaticContext() ? this.outer().hasEnclosingInstance(encl) : false;
      }
   }
}
