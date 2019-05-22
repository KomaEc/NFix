package polyglot.ext.jl.types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.types.MethodInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;

public abstract class ReferenceType_c extends Type_c implements ReferenceType {
   protected ReferenceType_c() {
   }

   public ReferenceType_c(TypeSystem ts) {
      this(ts, (Position)null);
   }

   public ReferenceType_c(TypeSystem ts, Position pos) {
      super(ts, pos);
   }

   public boolean isReference() {
      return true;
   }

   public ReferenceType toReference() {
      return this;
   }

   public abstract List methods();

   public abstract List fields();

   public abstract Type superType();

   public abstract List interfaces();

   public final boolean hasMethod(MethodInstance mi) {
      return this.ts.hasMethod(this, mi);
   }

   public boolean hasMethodImpl(MethodInstance mi) {
      Iterator j = this.methods().iterator();

      MethodInstance mj;
      do {
         if (!j.hasNext()) {
            return false;
         }

         mj = (MethodInstance)j.next();
      } while(!this.ts.isSameMethod(mi, mj));

      return true;
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

   public boolean isImplicitCastValidImpl(Type toType) {
      return this.ts.isSubtype(this, toType);
   }

   public List methodsNamed(String name) {
      List l = new LinkedList();
      Iterator i = this.methods().iterator();

      while(i.hasNext()) {
         MethodInstance mi = (MethodInstance)i.next();
         if (mi.name().equals(name)) {
            l.add(mi);
         }
      }

      return l;
   }

   public List methods(String name, List argTypes) {
      List l = new LinkedList();
      Iterator i = this.methodsNamed(name).iterator();

      while(i.hasNext()) {
         MethodInstance mi = (MethodInstance)i.next();
         if (mi.hasFormals(argTypes)) {
            l.add(mi);
         }
      }

      return l;
   }

   public boolean isCastValidImpl(Type toType) {
      if (!toType.isReference()) {
         return false;
      } else {
         return this.ts.isSubtype(this, toType) || this.ts.isSubtype(toType, this);
      }
   }
}
