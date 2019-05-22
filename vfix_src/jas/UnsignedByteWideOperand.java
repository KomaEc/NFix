package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class UnsignedByteWideOperand extends InsnOperand implements RuntimeConstants {
   int val;

   UnsignedByteWideOperand(int n) {
      this.val = n;
   }

   int size(ClassEnv ce, CodeAttr code) {
      return this.val >= 256 ? 3 : 1;
   }

   void writePrefix(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
      if (this.val > 255) {
         out.writeByte(-60);
      }

   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
      if (this.val > 255) {
         out.writeShort((short)('\uffff' & this.val));
      } else {
         out.writeByte((byte)(this.val & 255));
      }

   }

   void resolve(ClassEnv e) {
   }
}
