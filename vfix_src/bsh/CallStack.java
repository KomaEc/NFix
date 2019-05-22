package bsh;

import java.util.Vector;

public class CallStack {
   private Vector stack = new Vector(2);

   public CallStack() {
   }

   public CallStack(NameSpace var1) {
      this.push(var1);
   }

   public void clear() {
      this.stack.removeAllElements();
   }

   public void push(NameSpace var1) {
      this.stack.insertElementAt(var1, 0);
   }

   public NameSpace top() {
      return this.get(0);
   }

   public NameSpace get(int var1) {
      return var1 >= this.depth() ? NameSpace.JAVACODE : (NameSpace)this.stack.elementAt(var1);
   }

   public void set(int var1, NameSpace var2) {
      this.stack.setElementAt(var2, var1);
   }

   public NameSpace pop() {
      if (this.depth() < 1) {
         throw new InterpreterError("pop on empty CallStack");
      } else {
         NameSpace var1 = this.top();
         this.stack.removeElementAt(0);
         return var1;
      }
   }

   public NameSpace swap(NameSpace var1) {
      NameSpace var2 = (NameSpace)this.stack.elementAt(0);
      this.stack.setElementAt(var1, 0);
      return var2;
   }

   public int depth() {
      return this.stack.size();
   }

   public NameSpace[] toArray() {
      NameSpace[] var1 = new NameSpace[this.depth()];
      this.stack.copyInto(var1);
      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("CallStack:\n");
      NameSpace[] var2 = this.toArray();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.append("\t" + var2[var3] + "\n");
      }

      return var1.toString();
   }

   public CallStack copy() {
      CallStack var1 = new CallStack();
      var1.stack = (Vector)this.stack.clone();
      return var1;
   }
}
