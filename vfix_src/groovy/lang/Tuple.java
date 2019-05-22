package groovy.lang;

import java.util.AbstractList;
import java.util.List;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class Tuple extends AbstractList {
   private Object[] contents;
   private int hashCode;

   public Tuple(Object[] contents) {
      this.contents = contents;
   }

   public Object get(int index) {
      return this.contents[index];
   }

   public int size() {
      return this.contents.length;
   }

   public boolean equals(Object that) {
      return that instanceof Tuple ? this.equals((Tuple)that) : false;
   }

   public boolean equals(Tuple that) {
      if (this.contents.length == that.contents.length) {
         for(int i = 0; i < this.contents.length; ++i) {
            if (!DefaultTypeTransformation.compareEqual(this.contents[i], that.contents[i])) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         for(int i = 0; i < this.contents.length; ++i) {
            Object value = this.contents[i];
            int hash = value != null ? value.hashCode() : '몾';
            this.hashCode ^= hash;
         }

         if (this.hashCode == 0) {
            this.hashCode = 47806;
         }
      }

      return this.hashCode;
   }

   public List subList(int fromIndex, int toIndex) {
      int size = toIndex - fromIndex;
      Object[] newContent = new Object[size];
      System.arraycopy(this.contents, fromIndex, newContent, 0, size);
      return new Tuple(newContent);
   }
}
