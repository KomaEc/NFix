package soot.tagkit;

import java.util.Arrays;
import soot.jimple.Constant;

public abstract class ConstantValueTag implements Tag {
   protected byte[] bytes;

   protected ConstantValueTag() {
   }

   public String getName() {
      String className = this.getClass().getName();
      return className.substring(className.lastIndexOf(46) + 1);
   }

   public byte[] getValue() {
      return this.bytes;
   }

   public abstract Constant getConstant();

   public abstract String toString();

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + Arrays.hashCode(this.bytes);
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         ConstantValueTag other = (ConstantValueTag)obj;
         return Arrays.equals(this.bytes, other.bytes);
      }
   }
}
