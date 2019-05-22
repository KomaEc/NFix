package soot.coffi;

import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.RefType;
import soot.Type;

class TypeStack {
   private static final Logger logger = LoggerFactory.getLogger(TypeStack.class);
   private Type[] types;

   private TypeStack() {
   }

   public Object clone() {
      TypeStack newTypeStack = new TypeStack();
      newTypeStack.types = (Type[])this.types.clone();
      return newTypeStack;
   }

   public static TypeStack v() {
      TypeStack typeStack = new TypeStack();
      typeStack.types = new Type[0];
      return typeStack;
   }

   public TypeStack pop() {
      TypeStack newStack = new TypeStack();
      newStack.types = new Type[this.types.length - 1];
      System.arraycopy(this.types, 0, newStack.types, 0, this.types.length - 1);
      return newStack;
   }

   public TypeStack push(Type type) {
      TypeStack newStack = new TypeStack();
      newStack.types = new Type[this.types.length + 1];
      System.arraycopy(this.types, 0, newStack.types, 0, this.types.length);
      newStack.types[this.types.length] = type;
      return newStack;
   }

   public Type get(int index) {
      return this.types[index];
   }

   public int topIndex() {
      return this.types.length - 1;
   }

   public Type top() {
      if (this.types.length == 0) {
         throw new RuntimeException("TypeStack is empty");
      } else {
         return this.types[this.types.length - 1];
      }
   }

   public boolean equals(Object object) {
      if (object instanceof TypeStack) {
         TypeStack otherStack = (TypeStack)object;
         if (otherStack.types.length != this.types.length) {
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

   public TypeStack merge(TypeStack other) {
      if (this.types.length != other.types.length) {
         throw new RuntimeException("TypeStack merging failed; unequal stack lengths: " + this.types.length + " and " + other.types.length);
      } else {
         TypeStack newStack = new TypeStack();
         newStack.types = new Type[other.types.length];

         for(int i = 0; i < this.types.length; ++i) {
            if (this.types[i].equals(other.types[i])) {
               newStack.types[i] = this.types[i];
            } else {
               if (!(this.types[i] instanceof ArrayType) && !(this.types[i] instanceof RefType) || !(other.types[i] instanceof RefType) && !(other.types[i] instanceof ArrayType)) {
                  throw new RuntimeException("TypeStack merging failed; incompatible types " + this.types[i] + " and " + other.types[i]);
               }

               newStack.types[i] = RefType.v("java.lang.Object");
            }
         }

         return newStack;
      }
   }

   public void print(PrintStream out) {
      for(int i = this.types.length - 1; i >= 0; --i) {
         out.println(i + ": " + this.types[i].toString());
      }

      if (this.types.length == 0) {
         out.println("<empty>");
      }

   }
}
