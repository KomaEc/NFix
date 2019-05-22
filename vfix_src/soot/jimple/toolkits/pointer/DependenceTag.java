package soot.jimple.toolkits.pointer;

import soot.tagkit.Tag;

public class DependenceTag implements Tag {
   private static final String NAME = "DependenceTag";
   protected short read = -1;
   protected short write = -1;
   protected boolean callsNative = false;

   public boolean setCallsNative() {
      boolean ret = !this.callsNative;
      this.callsNative = true;
      return ret;
   }

   protected void setRead(short s) {
      this.read = s;
   }

   protected void setWrite(short s) {
      this.write = s;
   }

   public String getName() {
      return "DependenceTag";
   }

   public byte[] getValue() {
      byte[] ret = new byte[]{(byte)(this.read >> 8 & 255), (byte)(this.read & 255), (byte)(this.write >> 8 & 255), (byte)(this.write & 255), (byte)(this.callsNative ? 1 : 0)};
      return ret;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      if (this.callsNative) {
         buf.append("SECallsNative\n");
      }

      if (this.read >= 0) {
         buf.append("SEReads : " + this.read + "\n");
      }

      if (this.write >= 0) {
         buf.append("SEWrites: " + this.write + "\n");
      }

      return buf.toString();
   }
}
