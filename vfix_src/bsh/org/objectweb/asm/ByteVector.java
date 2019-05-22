package bsh.org.objectweb.asm;

final class ByteVector {
   byte[] data;
   int length;

   public ByteVector() {
      this.data = new byte[64];
   }

   public ByteVector(int var1) {
      this.data = new byte[var1];
   }

   public ByteVector put1(int var1) {
      int var2 = this.length;
      if (var2 + 1 > this.data.length) {
         this.enlarge(1);
      }

      this.data[var2++] = (byte)var1;
      this.length = var2;
      return this;
   }

   public ByteVector put11(int var1, int var2) {
      int var3 = this.length;
      if (var3 + 2 > this.data.length) {
         this.enlarge(2);
      }

      byte[] var4 = this.data;
      var4[var3++] = (byte)var1;
      var4[var3++] = (byte)var2;
      this.length = var3;
      return this;
   }

   public ByteVector put2(int var1) {
      int var2 = this.length;
      if (var2 + 2 > this.data.length) {
         this.enlarge(2);
      }

      byte[] var3 = this.data;
      var3[var2++] = (byte)(var1 >>> 8);
      var3[var2++] = (byte)var1;
      this.length = var2;
      return this;
   }

   public ByteVector put12(int var1, int var2) {
      int var3 = this.length;
      if (var3 + 3 > this.data.length) {
         this.enlarge(3);
      }

      byte[] var4 = this.data;
      var4[var3++] = (byte)var1;
      var4[var3++] = (byte)(var2 >>> 8);
      var4[var3++] = (byte)var2;
      this.length = var3;
      return this;
   }

   public ByteVector put4(int var1) {
      int var2 = this.length;
      if (var2 + 4 > this.data.length) {
         this.enlarge(4);
      }

      byte[] var3 = this.data;
      var3[var2++] = (byte)(var1 >>> 24);
      var3[var2++] = (byte)(var1 >>> 16);
      var3[var2++] = (byte)(var1 >>> 8);
      var3[var2++] = (byte)var1;
      this.length = var2;
      return this;
   }

   public ByteVector put8(long var1) {
      int var3 = this.length;
      if (var3 + 8 > this.data.length) {
         this.enlarge(8);
      }

      byte[] var4 = this.data;
      int var5 = (int)(var1 >>> 32);
      var4[var3++] = (byte)(var5 >>> 24);
      var4[var3++] = (byte)(var5 >>> 16);
      var4[var3++] = (byte)(var5 >>> 8);
      var4[var3++] = (byte)var5;
      var5 = (int)var1;
      var4[var3++] = (byte)(var5 >>> 24);
      var4[var3++] = (byte)(var5 >>> 16);
      var4[var3++] = (byte)(var5 >>> 8);
      var4[var3++] = (byte)var5;
      this.length = var3;
      return this;
   }

   public ByteVector putUTF(String var1) {
      int var2 = var1.length();
      int var3 = 0;

      for(int var4 = 0; var4 < var2; ++var4) {
         char var5 = var1.charAt(var4);
         if (var5 >= 1 && var5 <= 127) {
            ++var3;
         } else if (var5 > 2047) {
            var3 += 3;
         } else {
            var3 += 2;
         }
      }

      if (var3 > 65535) {
         throw new IllegalArgumentException();
      } else {
         int var9 = this.length;
         if (var9 + 2 + var3 > this.data.length) {
            this.enlarge(2 + var3);
         }

         byte[] var6 = this.data;
         var6[var9++] = (byte)(var3 >>> 8);
         var6[var9++] = (byte)var3;

         for(int var7 = 0; var7 < var2; ++var7) {
            char var8 = var1.charAt(var7);
            if (var8 >= 1 && var8 <= 127) {
               var6[var9++] = (byte)var8;
            } else if (var8 > 2047) {
               var6[var9++] = (byte)(224 | var8 >> 12 & 15);
               var6[var9++] = (byte)(128 | var8 >> 6 & 63);
               var6[var9++] = (byte)(128 | var8 & 63);
            } else {
               var6[var9++] = (byte)(192 | var8 >> 6 & 31);
               var6[var9++] = (byte)(128 | var8 & 63);
            }
         }

         this.length = var9;
         return this;
      }
   }

   public ByteVector putByteArray(byte[] var1, int var2, int var3) {
      if (this.length + var3 > this.data.length) {
         this.enlarge(var3);
      }

      if (var1 != null) {
         System.arraycopy(var1, var2, this.data, this.length, var3);
      }

      this.length += var3;
      return this;
   }

   private void enlarge(int var1) {
      byte[] var2 = new byte[Math.max(2 * this.data.length, this.length + var1)];
      System.arraycopy(this.data, 0, var2, 0, this.length);
      this.data = var2;
   }
}
