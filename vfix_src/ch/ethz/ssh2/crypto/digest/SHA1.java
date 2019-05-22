package ch.ethz.ssh2.crypto.digest;

public final class SHA1 implements Digest {
   private int H0;
   private int H1;
   private int H2;
   private int H3;
   private int H4;
   private final byte[] msg = new byte[64];
   private final int[] w = new int[80];
   private int currentPos;
   private long currentLen;

   public SHA1() {
      this.reset();
   }

   public final int getDigestLength() {
      return 20;
   }

   public final void reset() {
      this.H0 = 1732584193;
      this.H1 = -271733879;
      this.H2 = -1732584194;
      this.H3 = 271733878;
      this.H4 = -1009589776;
      this.currentPos = 0;
      this.currentLen = 0L;
   }

   public final void update(byte[] b, int off, int len) {
      for(int i = off; i < off + len; ++i) {
         this.update(b[i]);
      }

   }

   public final void update(byte[] b) {
      for(int i = 0; i < b.length; ++i) {
         this.update(b[i]);
      }

   }

   public final void update(byte b) {
      this.msg[this.currentPos++] = b;
      this.currentLen += 8L;
      if (this.currentPos == 64) {
         this.perform();
         this.currentPos = 0;
      }

   }

   private static final String toHexString(byte[] b) {
      String hexChar = "0123456789ABCDEF";
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < b.length; ++i) {
         sb.append("0123456789ABCDEF".charAt(b[i] >> 4 & 15));
         sb.append("0123456789ABCDEF".charAt(b[i] & 15));
      }

      return sb.toString();
   }

   private final void putInt(byte[] b, int pos, int val) {
      b[pos] = (byte)(val >> 24);
      b[pos + 1] = (byte)(val >> 16);
      b[pos + 2] = (byte)(val >> 8);
      b[pos + 3] = (byte)val;
   }

   public final void digest(byte[] out) {
      this.digest(out, 0);
   }

   public final void digest(byte[] out, int off) {
      long l = this.currentLen;
      this.update((byte)-128);

      while(this.currentPos != 56) {
         this.update((byte)0);
      }

      this.update((byte)((int)(l >> 56)));
      this.update((byte)((int)(l >> 48)));
      this.update((byte)((int)(l >> 40)));
      this.update((byte)((int)(l >> 32)));
      this.update((byte)((int)(l >> 24)));
      this.update((byte)((int)(l >> 16)));
      this.update((byte)((int)(l >> 8)));
      this.update((byte)((int)l));
      this.putInt(out, off, this.H0);
      this.putInt(out, off + 4, this.H1);
      this.putInt(out, off + 8, this.H2);
      this.putInt(out, off + 12, this.H3);
      this.putInt(out, off + 16, this.H4);
      this.reset();
   }

   private final void perform() {
      int A;
      for(A = 0; A < 16; ++A) {
         this.w[A] = (this.msg[A * 4] & 255) << 24 | (this.msg[A * 4 + 1] & 255) << 16 | (this.msg[A * 4 + 2] & 255) << 8 | this.msg[A * 4 + 3] & 255;
      }

      int B;
      for(A = 16; A < 80; ++A) {
         B = this.w[A - 3] ^ this.w[A - 8] ^ this.w[A - 14] ^ this.w[A - 16];
         this.w[A] = B << 1 | B >>> 31;
      }

      A = this.H0;
      B = this.H1;
      int C = this.H2;
      int D = this.H3;
      int E = this.H4;

      int t;
      int T;
      for(t = 0; t <= 19; ++t) {
         T = (A << 5 | A >>> 27) + (B & C | ~B & D) + E + this.w[t] + 1518500249;
         E = D;
         D = C;
         C = B << 30 | B >>> 2;
         B = A;
         A = T;
      }

      for(t = 20; t <= 39; ++t) {
         T = (A << 5 | A >>> 27) + (B ^ C ^ D) + E + this.w[t] + 1859775393;
         E = D;
         D = C;
         C = B << 30 | B >>> 2;
         B = A;
         A = T;
      }

      for(t = 40; t <= 59; ++t) {
         T = (A << 5 | A >>> 27) + (B & C | B & D | C & D) + E + this.w[t] + -1894007588;
         E = D;
         D = C;
         C = B << 30 | B >>> 2;
         B = A;
         A = T;
      }

      for(t = 60; t <= 79; ++t) {
         T = (A << 5 | A >>> 27) + (B ^ C ^ D) + E + this.w[t] + -899497514;
         E = D;
         D = C;
         C = B << 30 | B >>> 2;
         B = A;
         A = T;
      }

      this.H0 += A;
      this.H1 += B;
      this.H2 += C;
      this.H3 += D;
      this.H4 += E;
   }

   public static void main(String[] args) {
      SHA1 sha = new SHA1();
      byte[] dig1 = new byte[20];
      byte[] dig2 = new byte[20];
      byte[] dig3 = new byte[20];
      sha.update("abc".getBytes());
      sha.digest(dig1);
      sha.update("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes());
      sha.digest(dig2);

      for(int i = 0; i < 1000000; ++i) {
         sha.update((byte)97);
      }

      sha.digest(dig3);
      String dig1_res = toHexString(dig1);
      String dig2_res = toHexString(dig2);
      String dig3_res = toHexString(dig3);
      String dig1_ref = "A9993E364706816ABA3E25717850C26C9CD0D89D";
      String dig2_ref = "84983E441C3BD26EBAAE4AA1F95129E5E54670F1";
      String dig3_ref = "34AA973CD4C4DAA4F61EEB2BDBAD27316534016F";
      if (dig1_res.equals(dig1_ref)) {
         System.out.println("SHA-1 Test 1 OK.");
      } else {
         System.out.println("SHA-1 Test 1 FAILED.");
      }

      if (dig2_res.equals(dig2_ref)) {
         System.out.println("SHA-1 Test 2 OK.");
      } else {
         System.out.println("SHA-1 Test 2 FAILED.");
      }

      if (dig3_res.equals(dig3_ref)) {
         System.out.println("SHA-1 Test 3 OK.");
      } else {
         System.out.println("SHA-1 Test 3 FAILED.");
      }

   }
}
