package bsh.org.objectweb.asm;

public class Label {
   CodeWriter owner;
   boolean resolved;
   int position;
   private int referenceCount;
   private int[] srcAndRefPositions;
   int beginStackSize;
   int maxStackSize;
   Edge successors;
   Label next;
   boolean pushed;

   void put(CodeWriter var1, ByteVector var2, int var3, boolean var4) {
      if (this.resolved) {
         if (var4) {
            var2.put4(this.position - var3);
         } else {
            var2.put2(this.position - var3);
         }
      } else if (var4) {
         this.addReference(-1 - var3, var2.length);
         var2.put4(-1);
      } else {
         this.addReference(var3, var2.length);
         var2.put2(-1);
      }

   }

   private void addReference(int var1, int var2) {
      if (this.srcAndRefPositions == null) {
         this.srcAndRefPositions = new int[6];
      }

      if (this.referenceCount >= this.srcAndRefPositions.length) {
         int[] var3 = new int[this.srcAndRefPositions.length + 6];
         System.arraycopy(this.srcAndRefPositions, 0, var3, 0, this.srcAndRefPositions.length);
         this.srcAndRefPositions = var3;
      }

      this.srcAndRefPositions[this.referenceCount++] = var1;
      this.srcAndRefPositions[this.referenceCount++] = var2;
   }

   boolean resolve(CodeWriter var1, int var2, byte[] var3) {
      boolean var4 = false;
      this.resolved = true;
      this.position = var2;
      int var5 = 0;

      while(true) {
         while(var5 < this.referenceCount) {
            int var6 = this.srcAndRefPositions[var5++];
            int var7 = this.srcAndRefPositions[var5++];
            int var8;
            if (var6 >= 0) {
               var8 = var2 - var6;
               if (var8 < -32768 || var8 > 32767) {
                  int var9 = var3[var7 - 1] & 255;
                  if (var9 <= 168) {
                     var3[var7 - 1] = (byte)(var9 + 49);
                  } else {
                     var3[var7 - 1] = (byte)(var9 + 20);
                  }

                  var4 = true;
               }

               var3[var7++] = (byte)(var8 >>> 8);
               var3[var7] = (byte)var8;
            } else {
               var8 = var2 + var6 + 1;
               var3[var7++] = (byte)(var8 >>> 24);
               var3[var7++] = (byte)(var8 >>> 16);
               var3[var7++] = (byte)(var8 >>> 8);
               var3[var7] = (byte)var8;
            }
         }

         return var4;
      }
   }
}
