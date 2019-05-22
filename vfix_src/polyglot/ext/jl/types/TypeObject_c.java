package polyglot.ext.jl.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.TypeInputStream;

public abstract class TypeObject_c implements TypeObject {
   protected transient TypeSystem ts;
   protected Position position;

   protected TypeObject_c() {
   }

   public TypeObject_c(TypeSystem ts) {
      this(ts, (Position)null);
   }

   public TypeObject_c(TypeSystem ts, Position pos) {
      this.ts = ts;
      this.position = pos;
   }

   public Object copy() {
      try {
         return (TypeObject_c)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalCompilerError("Java clone() weirdness.");
      }
   }

   public TypeSystem typeSystem() {
      return this.ts;
   }

   public Position position() {
      return this.position;
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      if (in instanceof TypeInputStream) {
         this.ts = ((TypeInputStream)in).getTypeSystem();
      }

      in.defaultReadObject();
   }

   public final boolean equals(Object o) {
      return o instanceof TypeObject && this.ts.equals(this, (TypeObject)o);
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equalsImpl(TypeObject t) {
      return t == this;
   }

   void equalsImpl(Object o) {
   }
}
