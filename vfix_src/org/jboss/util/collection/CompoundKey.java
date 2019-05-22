package org.jboss.util.collection;

import java.io.Serializable;
import org.jboss.util.HashCode;
import org.jboss.util.NullArgumentException;
import org.jboss.util.Objects;
import org.jboss.util.Strings;

public class CompoundKey implements Serializable, Cloneable {
   private static final long serialVersionUID = -1181463173922935047L;
   private final Object[] elements;

   public CompoundKey(Object[] elements) {
      if (elements == null) {
         throw new NullArgumentException("elements");
      } else {
         this.elements = elements;
      }
   }

   public CompoundKey(Object a, Object b) {
      this(new Object[]{a, b});
   }

   public CompoundKey(Object a, Object b, Object c) {
      this(new Object[]{a, b, c});
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         CompoundKey key = (CompoundKey)obj;
         return Objects.equals(key.elements, this.elements);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return HashCode.generate(this.elements);
   }

   public String toString() {
      return super.toString() + Strings.join(this.elements, "[", ",", "]");
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}
