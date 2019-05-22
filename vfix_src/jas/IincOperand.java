package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class IincOperand extends InsnOperand implements RuntimeConstants {
   int vindex;
   int constt;

   IincOperand(int vindex, int constt) {
      this.vindex = vindex;
      this.constt = constt;
   }

   int size(ClassEnv ce, CodeAttr code) {
      return this.vindex <= 255 && this.constt <= 127 && this.constt >= -128 ? 2 : 5;
   }

   void resolve(ClassEnv e) {
   }

   void writePrefix(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
      if (this.vindex > 255 || this.constt > 127 || this.constt < -128) {
         out.writeByte(-60);
      }

   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
      if (this.vindex <= 255 && this.constt <= 127 && this.constt >= -128) {
         out.writeByte((byte)(255 & this.vindex));
         out.writeByte((byte)(255 & this.constt));
      } else {
         out.writeShort((short)('\uffff' & this.vindex));
         out.writeShort((short)('\uffff' & this.constt));
      }

   }
}
