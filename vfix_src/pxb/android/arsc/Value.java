package pxb.android.arsc;

public class Value {
   public final int data;
   public String raw;
   public final int type;

   public Value(int type, int data, String raw) {
      this.type = type;
      this.data = data;
      this.raw = raw;
   }

   public String toString() {
      return this.type == 3 ? this.raw : String.format("{t=0x%02x d=0x%08x}", this.type, this.data);
   }
}
