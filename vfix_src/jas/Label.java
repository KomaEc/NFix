package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class Label extends Insn implements RuntimeConstants {
   String id;

   public Label(String tag) {
      this.id = tag.intern();
      this.opc = -1;
      this.operand = null;
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) {
   }

   int size(ClassEnv e, CodeAttr ce) {
      return 0;
   }

   void writeOffset(CodeAttr ce, Insn source, DataOutputStream out) throws jasError, IOException {
      int pc = ce.getPc(this);
      int tpc;
      if (source == null) {
         tpc = 0;
      } else {
         tpc = ce.getPc(source);
      }

      short offset = (short)(pc - tpc);
      out.writeShort(offset);
   }

   void writeWideOffset(CodeAttr ce, Insn source, DataOutputStream out) throws IOException, jasError {
      int pc = ce.getPc(this);
      int tpc;
      if (source == null) {
         tpc = 0;
      } else {
         tpc = ce.getPc(source);
      }

      out.writeInt(pc - tpc);
   }

   public String toString() {
      return "Label: " + this.id;
   }
}
