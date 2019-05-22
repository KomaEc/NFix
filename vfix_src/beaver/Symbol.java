package beaver;

public class Symbol implements Cloneable {
   private static final int COLUMN_FIELD_BITS = 12;
   private static final int COLUMN_FIELD_MASK = 4095;
   public Object value;
   protected short id;
   protected int start;
   protected int end;

   public static int makePosition(int line, int column) {
      return line << 12 | column;
   }

   public static int getLine(int position) {
      return position >>> 12;
   }

   public static int getColumn(int position) {
      return position & 4095;
   }

   public Symbol(short id) {
      this.id = id;
      this.value = null;
   }

   public Symbol(short id, Object value) {
      this.id = id;
      this.value = value;
   }

   public Symbol(short id, int start, int end) {
      this.id = id;
      this.value = null;
      this.start = start;
      this.end = end;
   }

   public Symbol(short id, int left, int right, Object value) {
      this.id = id;
      this.value = value;
      this.start = left;
      this.end = right;
   }

   public Symbol(short id, int start_line, int start_column, int length) {
      this.id = id;
      this.value = null;
      this.start = makePosition(start_line, start_column);
      this.end = makePosition(start_line, start_column + length - 1);
   }

   public Symbol(short id, int start_line, int start_column, int length, Object value) {
      this.id = id;
      this.value = value;
      this.start = makePosition(start_line, start_column);
      this.end = makePosition(start_line, start_column + length - 1);
   }

   public Symbol(Object value) {
      this.value = value;
   }

   protected Symbol() {
      this.value = this;
   }

   public short getId() {
      return this.id;
   }

   public int getStart() {
      return this.start;
   }

   public int getEnd() {
      return this.end;
   }

   public Symbol clone() throws CloneNotSupportedException {
      Symbol copy = (Symbol)super.clone();
      copy.value = copy;
      return copy;
   }
}
