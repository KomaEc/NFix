package soot.coffi;

import java.util.Enumeration;

public class Utf8_Enumeration implements Enumeration {
   public int c;
   private short curindex;
   private short length;
   private byte[] bytes;

   public Utf8_Enumeration() {
   }

   public Utf8_Enumeration(byte[] b) {
      this.bytes = b;
      this.curindex = 2;
      this.length = (short)(((this.bytes[0] & 255) << 8) + (this.bytes[1] & 255) + 2);
   }

   public void reset(byte[] b) {
      this.bytes = b;
      this.curindex = 2;
      this.length = (short)(((this.bytes[0] & 255) << 8) + (this.bytes[1] & 255) + 2);
   }

   public boolean hasMoreElements() {
      return this.curindex < this.length;
   }

   public Object nextElement() {
      byte[] var10000 = this.bytes;
      short var10003 = this.curindex;
      this.curindex = (short)(var10003 + 1);
      byte b = var10000[var10003];
      if ((b & -128) == 0) {
         this.c = b;
      } else if ((b & -32) == 192) {
         this.c = (b & 31) << 6;
         var10000 = this.bytes;
         var10003 = this.curindex;
         this.curindex = (short)(var10003 + 1);
         b = var10000[var10003];
         this.c |= b & 63;
      } else {
         this.c = (b & 15) << 12;
         var10000 = this.bytes;
         var10003 = this.curindex;
         this.curindex = (short)(var10003 + 1);
         b = var10000[var10003];
         this.c |= (b & 63) << 6;
         var10000 = this.bytes;
         var10003 = this.curindex;
         this.curindex = (short)(var10003 + 1);
         b = var10000[var10003];
         this.c |= b & 63;
      }

      return this;
   }
}
