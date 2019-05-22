package bsh.org.objectweb.asm;

public class ClassWriter implements ClassVisitor {
   static final int CLASS = 7;
   static final int FIELD = 9;
   static final int METH = 10;
   static final int IMETH = 11;
   static final int STR = 8;
   static final int INT = 3;
   static final int FLOAT = 4;
   static final int LONG = 5;
   static final int DOUBLE = 6;
   static final int NAME_TYPE = 12;
   static final int UTF8 = 1;
   private short index = 1;
   private ByteVector pool = new ByteVector();
   private Item[] table = new Item[64];
   private int threshold;
   private int access;
   private int name;
   private int superName;
   private int interfaceCount;
   private int[] interfaces;
   private Item sourceFile;
   private int fieldCount;
   private ByteVector fields;
   private boolean computeMaxs;
   CodeWriter firstMethod;
   CodeWriter lastMethod;
   private int innerClassesCount;
   private ByteVector innerClasses;
   Item key;
   Item key2;
   Item key3;
   static final int NOARG_INSN = 0;
   static final int SBYTE_INSN = 1;
   static final int SHORT_INSN = 2;
   static final int VAR_INSN = 3;
   static final int IMPLVAR_INSN = 4;
   static final int TYPE_INSN = 5;
   static final int FIELDORMETH_INSN = 6;
   static final int ITFMETH_INSN = 7;
   static final int LABEL_INSN = 8;
   static final int LABELW_INSN = 9;
   static final int LDC_INSN = 10;
   static final int LDCW_INSN = 11;
   static final int IINC_INSN = 12;
   static final int TABL_INSN = 13;
   static final int LOOK_INSN = 14;
   static final int MANA_INSN = 15;
   static final int WIDE_INSN = 16;
   static byte[] TYPE;

   public ClassWriter(boolean var1) {
      this.threshold = (int)(0.75D * (double)this.table.length);
      this.key = new Item();
      this.key2 = new Item();
      this.key3 = new Item();
      this.computeMaxs = var1;
   }

   public void visit(int var1, String var2, String var3, String[] var4, String var5) {
      this.access = var1;
      this.name = this.newClass(var2).index;
      this.superName = var3 == null ? 0 : this.newClass(var3).index;
      if (var4 != null && var4.length > 0) {
         this.interfaceCount = var4.length;
         this.interfaces = new int[this.interfaceCount];

         for(int var6 = 0; var6 < this.interfaceCount; ++var6) {
            this.interfaces[var6] = this.newClass(var4[var6]).index;
         }
      }

      if (var5 != null) {
         this.newUTF8("SourceFile");
         this.sourceFile = this.newUTF8(var5);
      }

      if ((var1 & 131072) != 0) {
         this.newUTF8("Deprecated");
      }

   }

   public void visitInnerClass(String var1, String var2, String var3, int var4) {
      if (this.innerClasses == null) {
         this.newUTF8("InnerClasses");
         this.innerClasses = new ByteVector();
      }

      ++this.innerClassesCount;
      this.innerClasses.put2(var1 == null ? 0 : this.newClass(var1).index);
      this.innerClasses.put2(var2 == null ? 0 : this.newClass(var2).index);
      this.innerClasses.put2(var3 == null ? 0 : this.newUTF8(var3).index);
      this.innerClasses.put2(var4);
   }

   public void visitField(int var1, String var2, String var3, Object var4) {
      ++this.fieldCount;
      if (this.fields == null) {
         this.fields = new ByteVector();
      }

      this.fields.put2(var1).put2(this.newUTF8(var2).index).put2(this.newUTF8(var3).index);
      int var5 = 0;
      if (var4 != null) {
         ++var5;
      }

      if ((var1 & 65536) != 0) {
         ++var5;
      }

      if ((var1 & 131072) != 0) {
         ++var5;
      }

      this.fields.put2(var5);
      if (var4 != null) {
         this.fields.put2(this.newUTF8("ConstantValue").index);
         this.fields.put4(2).put2(this.newCst(var4).index);
      }

      if ((var1 & 65536) != 0) {
         this.fields.put2(this.newUTF8("Synthetic").index).put4(0);
      }

      if ((var1 & 131072) != 0) {
         this.fields.put2(this.newUTF8("Deprecated").index).put4(0);
      }

   }

   public CodeVisitor visitMethod(int var1, String var2, String var3, String[] var4) {
      CodeWriter var5 = new CodeWriter(this, this.computeMaxs);
      var5.init(var1, var2, var3, var4);
      return var5;
   }

   public void visitEnd() {
   }

   public byte[] toByteArray() {
      int var1 = 24 + 2 * this.interfaceCount;
      if (this.fields != null) {
         var1 += this.fields.length;
      }

      int var2 = 0;

      CodeWriter var3;
      for(var3 = this.firstMethod; var3 != null; var3 = var3.next) {
         ++var2;
         var1 += var3.getSize();
      }

      var1 += this.pool.length;
      int var4 = 0;
      if (this.sourceFile != null) {
         ++var4;
         var1 += 8;
      }

      if ((this.access & 131072) != 0) {
         ++var4;
         var1 += 6;
      }

      if (this.innerClasses != null) {
         ++var4;
         var1 += 8 + this.innerClasses.length;
      }

      ByteVector var5 = new ByteVector(var1);
      var5.put4(-889275714).put2(3).put2(45);
      var5.put2(this.index).putByteArray(this.pool.data, 0, this.pool.length);
      var5.put2(this.access).put2(this.name).put2(this.superName);
      var5.put2(this.interfaceCount);

      for(int var6 = 0; var6 < this.interfaceCount; ++var6) {
         var5.put2(this.interfaces[var6]);
      }

      var5.put2(this.fieldCount);
      if (this.fields != null) {
         var5.putByteArray(this.fields.data, 0, this.fields.length);
      }

      var5.put2(var2);

      for(var3 = this.firstMethod; var3 != null; var3 = var3.next) {
         var3.put(var5);
      }

      var5.put2(var4);
      if (this.sourceFile != null) {
         var5.put2(this.newUTF8("SourceFile").index).put4(2).put2(this.sourceFile.index);
      }

      if ((this.access & 131072) != 0) {
         var5.put2(this.newUTF8("Deprecated").index).put4(0);
      }

      if (this.innerClasses != null) {
         var5.put2(this.newUTF8("InnerClasses").index);
         var5.put4(this.innerClasses.length + 2).put2(this.innerClassesCount);
         var5.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
      }

      return var5.data;
   }

   Item newCst(Object var1) {
      if (var1 instanceof Integer) {
         int var5 = (Integer)var1;
         return this.newInteger(var5);
      } else if (var1 instanceof Float) {
         float var2 = (Float)var1;
         return this.newFloat(var2);
      } else if (var1 instanceof Long) {
         long var6 = (Long)var1;
         return this.newLong(var6);
      } else if (var1 instanceof Double) {
         double var3 = (Double)var1;
         return this.newDouble(var3);
      } else if (var1 instanceof String) {
         return this.newString((String)var1);
      } else {
         throw new IllegalArgumentException("value " + var1);
      }
   }

   Item newUTF8(String var1) {
      this.key.set(1, var1, (String)null, (String)null);
      Item var2 = this.get(this.key);
      if (var2 == null) {
         this.pool.put1(1).putUTF(var1);
         short var10004 = this.index;
         this.index = (short)(var10004 + 1);
         var2 = new Item(var10004, this.key);
         this.put(var2);
      }

      return var2;
   }

   Item newClass(String var1) {
      this.key2.set(7, var1, (String)null, (String)null);
      Item var2 = this.get(this.key2);
      if (var2 == null) {
         this.pool.put12(7, this.newUTF8(var1).index);
         short var10004 = this.index;
         this.index = (short)(var10004 + 1);
         var2 = new Item(var10004, this.key2);
         this.put(var2);
      }

      return var2;
   }

   Item newField(String var1, String var2, String var3) {
      this.key3.set(9, var1, var2, var3);
      Item var4 = this.get(this.key3);
      if (var4 == null) {
         this.put122(9, this.newClass(var1).index, this.newNameType(var2, var3).index);
         short var10004 = this.index;
         this.index = (short)(var10004 + 1);
         var4 = new Item(var10004, this.key3);
         this.put(var4);
      }

      return var4;
   }

   Item newMethod(String var1, String var2, String var3) {
      this.key3.set(10, var1, var2, var3);
      Item var4 = this.get(this.key3);
      if (var4 == null) {
         this.put122(10, this.newClass(var1).index, this.newNameType(var2, var3).index);
         short var10004 = this.index;
         this.index = (short)(var10004 + 1);
         var4 = new Item(var10004, this.key3);
         this.put(var4);
      }

      return var4;
   }

   Item newItfMethod(String var1, String var2, String var3) {
      this.key3.set(11, var1, var2, var3);
      Item var4 = this.get(this.key3);
      if (var4 == null) {
         this.put122(11, this.newClass(var1).index, this.newNameType(var2, var3).index);
         short var10004 = this.index;
         this.index = (short)(var10004 + 1);
         var4 = new Item(var10004, this.key3);
         this.put(var4);
      }

      return var4;
   }

   private Item newInteger(int var1) {
      this.key.set(var1);
      Item var2 = this.get(this.key);
      if (var2 == null) {
         this.pool.put1(3).put4(var1);
         short var10004 = this.index;
         this.index = (short)(var10004 + 1);
         var2 = new Item(var10004, this.key);
         this.put(var2);
      }

      return var2;
   }

   private Item newFloat(float var1) {
      this.key.set(var1);
      Item var2 = this.get(this.key);
      if (var2 == null) {
         this.pool.put1(4).put4(Float.floatToIntBits(var1));
         short var10004 = this.index;
         this.index = (short)(var10004 + 1);
         var2 = new Item(var10004, this.key);
         this.put(var2);
      }

      return var2;
   }

   private Item newLong(long var1) {
      this.key.set(var1);
      Item var3 = this.get(this.key);
      if (var3 == null) {
         this.pool.put1(5).put8(var1);
         var3 = new Item(this.index, this.key);
         this.put(var3);
         this.index = (short)(this.index + 2);
      }

      return var3;
   }

   private Item newDouble(double var1) {
      this.key.set(var1);
      Item var3 = this.get(this.key);
      if (var3 == null) {
         this.pool.put1(6).put8(Double.doubleToLongBits(var1));
         var3 = new Item(this.index, this.key);
         this.put(var3);
         this.index = (short)(this.index + 2);
      }

      return var3;
   }

   private Item newString(String var1) {
      this.key2.set(8, var1, (String)null, (String)null);
      Item var2 = this.get(this.key2);
      if (var2 == null) {
         this.pool.put12(8, this.newUTF8(var1).index);
         short var10004 = this.index;
         this.index = (short)(var10004 + 1);
         var2 = new Item(var10004, this.key2);
         this.put(var2);
      }

      return var2;
   }

   private Item newNameType(String var1, String var2) {
      this.key2.set(12, var1, var2, (String)null);
      Item var3 = this.get(this.key2);
      if (var3 == null) {
         this.put122(12, this.newUTF8(var1).index, this.newUTF8(var2).index);
         short var10004 = this.index;
         this.index = (short)(var10004 + 1);
         var3 = new Item(var10004, this.key2);
         this.put(var3);
      }

      return var3;
   }

   private Item get(Item var1) {
      Item[] var2 = this.table;
      int var3 = var1.hashCode;
      int var4 = (var3 & Integer.MAX_VALUE) % var2.length;

      for(Item var5 = var2[var4]; var5 != null; var5 = var5.next) {
         if (var5.hashCode == var3 && var1.isEqualTo(var5)) {
            return var5;
         }
      }

      return null;
   }

   private void put(Item var1) {
      int var2;
      if (this.index > this.threshold) {
         var2 = this.table.length;
         Item[] var3 = this.table;
         int var4 = var2 * 2 + 1;
         Item[] var5 = new Item[var4];
         this.threshold = (int)((double)var4 * 0.75D);
         this.table = var5;
         int var6 = var2;

         Item var8;
         int var9;
         while(var6-- > 0) {
            for(Item var7 = var3[var6]; var7 != null; var5[var9] = var8) {
               var8 = var7;
               var7 = var7.next;
               var9 = (var8.hashCode & Integer.MAX_VALUE) % var4;
               var8.next = var5[var9];
            }
         }
      }

      var2 = (var1.hashCode & Integer.MAX_VALUE) % this.table.length;
      var1.next = this.table[var2];
      this.table[var2] = var1;
   }

   private void put122(int var1, int var2, int var3) {
      this.pool.put12(var1, var2).put2(var3);
   }

   static {
      byte[] var0 = new byte[220];
      String var1 = "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHAFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII";

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var0[var2] = (byte)(var1.charAt(var2) - 65);
      }

      TYPE = var0;
   }
}
