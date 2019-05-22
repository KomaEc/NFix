package soot.asm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.objectweb.asm.tree.AbstractInsnNode;
import soot.Local;
import soot.Value;
import soot.ValueBox;

final class Operand {
   final AbstractInsnNode insn;
   final Value value;
   Local stack;
   private Object boxes;

   Operand(AbstractInsnNode insn, Value value) {
      this.insn = insn;
      this.value = value;
   }

   void removeBox(ValueBox vb) {
      if (vb != null) {
         if (this.boxes == vb) {
            this.boxes = null;
         } else if (this.boxes instanceof List) {
            List<ValueBox> list = (List)this.boxes;
            list.remove(vb);
         }

      }
   }

   void addBox(ValueBox vb) {
      if (this.boxes instanceof List) {
         List<ValueBox> list = (List)this.boxes;
         list.add(vb);
      } else if (this.boxes instanceof ValueBox) {
         ValueBox ovb = (ValueBox)this.boxes;
         List<ValueBox> list = new ArrayList();
         list.add(ovb);
         list.add(vb);
         this.boxes = list;
      } else {
         this.boxes = vb;
      }

   }

   void updateBoxes() {
      Value val = this.stackOrValue();
      if (this.boxes instanceof List) {
         Iterator var2 = ((List)this.boxes).iterator();

         while(var2.hasNext()) {
            ValueBox vb = (ValueBox)var2.next();
            vb.setValue(val);
         }
      } else if (this.boxes instanceof ValueBox) {
         ((ValueBox)this.boxes).setValue(val);
      }

   }

   <A> A value() {
      return this.value;
   }

   Value stackOrValue() {
      Local s = this.stack;
      return (Value)(s == null ? this.value : s);
   }

   boolean equivTo(Operand other) {
      return other.value == null && this.value == null ? true : this.stackOrValue().equivTo(other.stackOrValue());
   }

   public boolean equals(Object other) {
      return other instanceof Operand && this.equivTo((Operand)other);
   }
}
