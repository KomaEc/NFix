package soot.coffi;

import java.io.PrintStream;
import soot.ArrayType;
import soot.RefType;
import soot.Type;

class TypeArray {
   private Type[] types;

   private TypeArray() {
   }

   public static TypeArray v(int size) {
      TypeArray newArray = new TypeArray();
      newArray.types = new Type[size];

      for(int i = 0; i < size; ++i) {
         newArray.types[i] = UnusuableType.v();
      }

      return newArray;
   }

   public Type get(int index) {
      return this.types[index];
   }

   public TypeArray set(int index, Type type) {
      TypeArray newArray = new TypeArray();
      newArray.types = (Type[])this.types.clone();
      newArray.types[index] = type;
      return newArray;
   }

   public boolean equals(Object obj) {
      if (obj instanceof TypeArray) {
         TypeArray other = (TypeArray)obj;
         if (this.types.length != other.types.length) {
            return false;
         } else {
            Type[] var3 = this.types;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Type element = var3[var5];
               if (!element.equals(element)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public TypeArray merge(TypeArray otherArray) {
      TypeArray newArray = new TypeArray();
      if (this.types.length != otherArray.types.length) {
         throw new RuntimeException("Merging of type arrays failed; unequal array length");
      } else {
         newArray.types = new Type[this.types.length];

         for(int i = 0; i < this.types.length; ++i) {
            if (this.types[i].equals(otherArray.types[i])) {
               newArray.types[i] = this.types[i];
            } else if (!(this.types[i] instanceof ArrayType) && !(this.types[i] instanceof RefType) || !(otherArray.types[i] instanceof ArrayType) && !(otherArray.types[i] instanceof RefType)) {
               newArray.types[i] = UnusuableType.v();
            } else {
               newArray.types[i] = RefType.v("java.lang.Object");
            }
         }

         return newArray;
      }
   }

   public void print(PrintStream out) {
      for(int i = 0; i < this.types.length; ++i) {
         out.println(i + ": " + this.types[i].toString());
      }

   }
}
