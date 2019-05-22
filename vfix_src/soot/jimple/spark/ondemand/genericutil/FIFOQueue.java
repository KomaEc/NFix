package soot.jimple.spark.ondemand.genericutil;

public final class FIFOQueue {
   private Object[] _buf;
   private int _top;
   private int _bottom;

   public FIFOQueue(int initialSize_) {
      this._buf = new Object[initialSize_];
   }

   public FIFOQueue() {
      this(10);
   }

   public boolean push(Object obj_) {
      return this.add(obj_);
   }

   public boolean add(Object obj_) {
      this._buf[this._bottom] = obj_;
      this._bottom = this._bottom == this._buf.length - 1 ? 0 : this._bottom + 1;
      if (this._bottom == this._top) {
         int oldLen = this._buf.length;
         int newLen = oldLen * 2;
         Object[] newBuf = new Object[newLen];
         int topToEnd = oldLen - this._top;
         int newTop = newLen - topToEnd;
         System.arraycopy(this._buf, 0, newBuf, 0, this._top);
         System.arraycopy(this._buf, this._top, newBuf, newTop, topToEnd);
         this._buf = newBuf;
         this._top = newTop;
         return true;
      } else {
         return false;
      }
   }

   public Object pop() {
      return this.remove();
   }

   public Object remove() {
      if (this._bottom == this._top) {
         return null;
      } else {
         Object ret = this._buf[this._top];
         this._top = this._top == this._buf.length - 1 ? 0 : this._top + 1;
         return ret;
      }
   }

   public boolean isEmpty() {
      return this._bottom == this._top;
   }

   public String toString() {
      return this._bottom + " " + this._top;
   }

   public void clear() {
      this._bottom = 0;
      this._top = 0;
   }
}
