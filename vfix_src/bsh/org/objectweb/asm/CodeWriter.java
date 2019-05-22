package bsh.org.objectweb.asm;

public class CodeWriter implements CodeVisitor {
   static final boolean CHECK = false;
   CodeWriter next;
   private ClassWriter cw;
   private Item name;
   private Item desc;
   private int access;
   private int maxStack;
   private int maxLocals;
   private ByteVector code = new ByteVector();
   private int catchCount;
   private ByteVector catchTable;
   private int exceptionCount;
   private int[] exceptions;
   private int localVarCount;
   private ByteVector localVar;
   private int lineNumberCount;
   private ByteVector lineNumber;
   private boolean resize;
   private final boolean computeMaxs;
   private int stackSize;
   private int maxStackSize;
   private Label currentBlock;
   private Label blockStack;
   private static final int[] SIZE;
   private Edge head;
   private Edge tail;
   private static Edge pool;

   protected CodeWriter(ClassWriter var1, boolean var2) {
      if (var1.firstMethod == null) {
         var1.firstMethod = this;
         var1.lastMethod = this;
      } else {
         var1.lastMethod.next = this;
         var1.lastMethod = this;
      }

      this.cw = var1;
      this.computeMaxs = var2;
      if (var2) {
         this.currentBlock = new Label();
         this.currentBlock.pushed = true;
         this.blockStack = this.currentBlock;
      }

   }

   protected void init(int var1, String var2, String var3, String[] var4) {
      this.access = var1;
      this.name = this.cw.newUTF8(var2);
      this.desc = this.cw.newUTF8(var3);
      int var5;
      if (var4 != null && var4.length > 0) {
         this.exceptionCount = var4.length;
         this.exceptions = new int[this.exceptionCount];

         for(var5 = 0; var5 < this.exceptionCount; ++var5) {
            this.exceptions[var5] = this.cw.newClass(var4[var5]).index;
         }
      }

      if (this.computeMaxs) {
         var5 = getArgumentsAndReturnSizes(var3) >> 2;
         if ((var1 & 8) != 0) {
            --var5;
         }

         if (var5 > this.maxLocals) {
            this.maxLocals = var5;
         }
      }

   }

   public void visitInsn(int var1) {
      if (this.computeMaxs) {
         int var2 = this.stackSize + SIZE[var1];
         if (var2 > this.maxStackSize) {
            this.maxStackSize = var2;
         }

         this.stackSize = var2;
         if ((var1 >= 172 && var1 <= 177 || var1 == 191) && this.currentBlock != null) {
            this.currentBlock.maxStackSize = this.maxStackSize;
            this.currentBlock = null;
         }
      }

      this.code.put1(var1);
   }

   public void visitIntInsn(int var1, int var2) {
      if (this.computeMaxs && var1 != 188) {
         int var3 = this.stackSize + 1;
         if (var3 > this.maxStackSize) {
            this.maxStackSize = var3;
         }

         this.stackSize = var3;
      }

      if (var1 == 17) {
         this.code.put12(var1, var2);
      } else {
         this.code.put11(var1, var2);
      }

   }

   public void visitVarInsn(int var1, int var2) {
      int var3;
      if (this.computeMaxs) {
         if (var1 == 169) {
            if (this.currentBlock != null) {
               this.currentBlock.maxStackSize = this.maxStackSize;
               this.currentBlock = null;
            }
         } else {
            var3 = this.stackSize + SIZE[var1];
            if (var3 > this.maxStackSize) {
               this.maxStackSize = var3;
            }

            this.stackSize = var3;
         }

         if (var1 != 22 && var1 != 24 && var1 != 55 && var1 != 57) {
            var3 = var2 + 1;
         } else {
            var3 = var2 + 2;
         }

         if (var3 > this.maxLocals) {
            this.maxLocals = var3;
         }
      }

      if (var2 < 4 && var1 != 169) {
         if (var1 < 54) {
            var3 = 26 + (var1 - 21 << 2) + var2;
         } else {
            var3 = 59 + (var1 - 54 << 2) + var2;
         }

         this.code.put1(var3);
      } else if (var2 >= 256) {
         this.code.put1(196).put12(var1, var2);
      } else {
         this.code.put11(var1, var2);
      }

   }

   public void visitTypeInsn(int var1, String var2) {
      if (this.computeMaxs && var1 == 187) {
         int var3 = this.stackSize + 1;
         if (var3 > this.maxStackSize) {
            this.maxStackSize = var3;
         }

         this.stackSize = var3;
      }

      this.code.put12(var1, this.cw.newClass(var2).index);
   }

   public void visitFieldInsn(int var1, String var2, String var3, String var4) {
      if (this.computeMaxs) {
         int var6;
         label70: {
            char var5 = var4.charAt(0);
            switch(var1) {
            case 178:
               var6 = this.stackSize + (var5 != 'D' && var5 != 'J' ? 1 : 2);
               break label70;
            case 179:
               var6 = this.stackSize + (var5 != 'D' && var5 != 'J' ? -1 : -2);
               break label70;
            case 180:
               var6 = this.stackSize + (var5 != 'D' && var5 != 'J' ? 0 : 1);
               break label70;
            }

            var6 = this.stackSize + (var5 != 'D' && var5 != 'J' ? -2 : -3);
         }

         if (var6 > this.maxStackSize) {
            this.maxStackSize = var6;
         }

         this.stackSize = var6;
      }

      this.code.put12(var1, this.cw.newField(var2, var3, var4).index);
   }

   public void visitMethodInsn(int var1, String var2, String var3, String var4) {
      Item var5;
      if (var1 == 185) {
         var5 = this.cw.newItfMethod(var2, var3, var4);
      } else {
         var5 = this.cw.newMethod(var2, var3, var4);
      }

      int var6 = var5.intVal;
      if (this.computeMaxs) {
         if (var6 == 0) {
            var6 = getArgumentsAndReturnSizes(var4);
            var5.intVal = var6;
         }

         int var7;
         if (var1 == 184) {
            var7 = this.stackSize - (var6 >> 2) + (var6 & 3) + 1;
         } else {
            var7 = this.stackSize - (var6 >> 2) + (var6 & 3);
         }

         if (var7 > this.maxStackSize) {
            this.maxStackSize = var7;
         }

         this.stackSize = var7;
      }

      if (var1 == 185) {
         if (!this.computeMaxs && var6 == 0) {
            var6 = getArgumentsAndReturnSizes(var4);
            var5.intVal = var6;
         }

         this.code.put12(185, var5.index).put11(var6 >> 2, 0);
      } else {
         this.code.put12(var1, var5.index);
      }

   }

   public void visitJumpInsn(int var1, Label var2) {
      if (this.computeMaxs) {
         if (var1 == 167) {
            if (this.currentBlock != null) {
               this.currentBlock.maxStackSize = this.maxStackSize;
               this.addSuccessor(this.stackSize, var2);
               this.currentBlock = null;
            }
         } else if (var1 == 168) {
            if (this.currentBlock != null) {
               this.addSuccessor(this.stackSize + 1, var2);
            }
         } else {
            this.stackSize += SIZE[var1];
            if (this.currentBlock != null) {
               this.addSuccessor(this.stackSize, var2);
            }
         }
      }

      if (var2.resolved && var2.position - this.code.length < -32768) {
         if (var1 == 167) {
            this.code.put1(200);
         } else if (var1 == 168) {
            this.code.put1(201);
         } else {
            this.code.put1(var1 <= 166 ? (var1 + 1 ^ 1) - 1 : var1 ^ 1);
            this.code.put2(8);
            this.code.put1(200);
         }

         var2.put(this, this.code, this.code.length - 1, true);
      } else {
         this.code.put1(var1);
         var2.put(this, this.code, this.code.length - 1, false);
      }

   }

   public void visitLabel(Label var1) {
      if (this.computeMaxs) {
         if (this.currentBlock != null) {
            this.currentBlock.maxStackSize = this.maxStackSize;
            this.addSuccessor(this.stackSize, var1);
         }

         this.currentBlock = var1;
         this.stackSize = 0;
         this.maxStackSize = 0;
      }

      this.resize |= var1.resolve(this, this.code.length, this.code.data);
   }

   public void visitLdcInsn(Object var1) {
      Item var2 = this.cw.newCst(var1);
      if (this.computeMaxs) {
         int var3;
         if (var2.type != 5 && var2.type != 6) {
            var3 = this.stackSize + 1;
         } else {
            var3 = this.stackSize + 2;
         }

         if (var3 > this.maxStackSize) {
            this.maxStackSize = var3;
         }

         this.stackSize = var3;
      }

      short var4 = var2.index;
      if (var2.type != 5 && var2.type != 6) {
         if (var4 >= 256) {
            this.code.put12(19, var4);
         } else {
            this.code.put11(18, var4);
         }
      } else {
         this.code.put12(20, var4);
      }

   }

   public void visitIincInsn(int var1, int var2) {
      if (this.computeMaxs) {
         int var3 = var1 + 1;
         if (var3 > this.maxLocals) {
            this.maxLocals = var3;
         }
      }

      if (var1 <= 255 && var2 <= 127 && var2 >= -128) {
         this.code.put1(132).put11(var1, var2);
      } else {
         this.code.put1(196).put12(132, var1).put2(var2);
      }

   }

   public void visitTableSwitchInsn(int var1, int var2, Label var3, Label[] var4) {
      int var5;
      if (this.computeMaxs) {
         --this.stackSize;
         if (this.currentBlock != null) {
            this.currentBlock.maxStackSize = this.maxStackSize;
            this.addSuccessor(this.stackSize, var3);

            for(var5 = 0; var5 < var4.length; ++var5) {
               this.addSuccessor(this.stackSize, var4[var5]);
            }

            this.currentBlock = null;
         }
      }

      var5 = this.code.length;
      this.code.put1(170);

      while(this.code.length % 4 != 0) {
         this.code.put1(0);
      }

      var3.put(this, this.code, var5, true);
      this.code.put4(var1).put4(var2);

      for(int var6 = 0; var6 < var4.length; ++var6) {
         var4[var6].put(this, this.code, var5, true);
      }

   }

   public void visitLookupSwitchInsn(Label var1, int[] var2, Label[] var3) {
      int var4;
      if (this.computeMaxs) {
         --this.stackSize;
         if (this.currentBlock != null) {
            this.currentBlock.maxStackSize = this.maxStackSize;
            this.addSuccessor(this.stackSize, var1);

            for(var4 = 0; var4 < var3.length; ++var4) {
               this.addSuccessor(this.stackSize, var3[var4]);
            }

            this.currentBlock = null;
         }
      }

      var4 = this.code.length;
      this.code.put1(171);

      while(this.code.length % 4 != 0) {
         this.code.put1(0);
      }

      var1.put(this, this.code, var4, true);
      this.code.put4(var3.length);

      for(int var5 = 0; var5 < var3.length; ++var5) {
         this.code.put4(var2[var5]);
         var3[var5].put(this, this.code, var4, true);
      }

   }

   public void visitMultiANewArrayInsn(String var1, int var2) {
      if (this.computeMaxs) {
         this.stackSize += 1 - var2;
      }

      Item var3 = this.cw.newClass(var1);
      this.code.put12(197, var3.index).put1(var2);
   }

   public void visitTryCatchBlock(Label var1, Label var2, Label var3, String var4) {
      if (this.computeMaxs && !var3.pushed) {
         var3.beginStackSize = 1;
         var3.pushed = true;
         var3.next = this.blockStack;
         this.blockStack = var3;
      }

      ++this.catchCount;
      if (this.catchTable == null) {
         this.catchTable = new ByteVector();
      }

      this.catchTable.put2(var1.position);
      this.catchTable.put2(var2.position);
      this.catchTable.put2(var3.position);
      this.catchTable.put2(var4 != null ? this.cw.newClass(var4).index : 0);
   }

   public void visitMaxs(int var1, int var2) {
      if (this.computeMaxs) {
         int var3 = 0;
         Label var4 = this.blockStack;

         while(var4 != null) {
            Label var5 = var4;
            var4 = var4.next;
            int var6 = var5.beginStackSize;
            int var7 = var6 + var5.maxStackSize;
            if (var7 > var3) {
               var3 = var7;
            }

            for(Edge var8 = var5.successors; var8 != null; var8 = var8.next) {
               var5 = var8.successor;
               if (!var5.pushed) {
                  var5.beginStackSize = var6 + var8.stackSize;
                  var5.pushed = true;
                  var5.next = var4;
                  var4 = var5;
               }
            }
         }

         this.maxStack = var3;
         synchronized(SIZE) {
            if (this.tail != null) {
               this.tail.poolNext = pool;
               pool = this.head;
            }
         }
      } else {
         this.maxStack = var1;
         this.maxLocals = var2;
      }

   }

   public void visitLocalVariable(String var1, String var2, Label var3, Label var4, int var5) {
      if (this.localVar == null) {
         this.cw.newUTF8("LocalVariableTable");
         this.localVar = new ByteVector();
      }

      ++this.localVarCount;
      this.localVar.put2(var3.position);
      this.localVar.put2(var4.position - var3.position);
      this.localVar.put2(this.cw.newUTF8(var1).index);
      this.localVar.put2(this.cw.newUTF8(var2).index);
      this.localVar.put2(var5);
   }

   public void visitLineNumber(int var1, Label var2) {
      if (this.lineNumber == null) {
         this.cw.newUTF8("LineNumberTable");
         this.lineNumber = new ByteVector();
      }

      ++this.lineNumberCount;
      this.lineNumber.put2(var2.position);
      this.lineNumber.put2(var1);
   }

   private static int getArgumentsAndReturnSizes(String var0) {
      int var1 = 1;
      int var2 = 1;

      while(true) {
         while(true) {
            char var3 = var0.charAt(var2++);
            if (var3 == ')') {
               var3 = var0.charAt(var2);
               return var1 << 2 | (var3 == 'V' ? 0 : (var3 != 'D' && var3 != 'J' ? 1 : 2));
            }

            if (var3 == 'L') {
               while(var0.charAt(var2++) != ';') {
               }

               ++var1;
            } else if (var3 != '[') {
               if (var3 != 'D' && var3 != 'J') {
                  ++var1;
               } else {
                  var1 += 2;
               }
            } else {
               while((var3 = var0.charAt(var2)) == '[') {
                  ++var2;
               }

               if (var3 == 'D' || var3 == 'J') {
                  --var1;
               }
            }
         }
      }
   }

   private void addSuccessor(int var1, Label var2) {
      Edge var4;
      synchronized(SIZE) {
         if (pool == null) {
            var4 = new Edge();
         } else {
            var4 = pool;
            pool = pool.poolNext;
         }
      }

      if (this.tail == null) {
         this.tail = var4;
      }

      var4.poolNext = this.head;
      this.head = var4;
      var4.stackSize = var1;
      var4.successor = var2;
      var4.next = this.currentBlock.successors;
      this.currentBlock.successors = var4;
   }

   final int getSize() {
      if (this.resize) {
         this.resizeInstructions(new int[0], new int[0], 0);
      }

      int var1 = 8;
      if (this.code.length > 0) {
         this.cw.newUTF8("Code");
         var1 += 18 + this.code.length + 8 * this.catchCount;
         if (this.localVar != null) {
            var1 += 8 + this.localVar.length;
         }

         if (this.lineNumber != null) {
            var1 += 8 + this.lineNumber.length;
         }
      }

      if (this.exceptionCount > 0) {
         this.cw.newUTF8("Exceptions");
         var1 += 8 + 2 * this.exceptionCount;
      }

      if ((this.access & 65536) != 0) {
         this.cw.newUTF8("Synthetic");
         var1 += 6;
      }

      if ((this.access & 131072) != 0) {
         this.cw.newUTF8("Deprecated");
         var1 += 6;
      }

      return var1;
   }

   final void put(ByteVector var1) {
      var1.put2(this.access).put2(this.name.index).put2(this.desc.index);
      int var2 = 0;
      if (this.code.length > 0) {
         ++var2;
      }

      if (this.exceptionCount > 0) {
         ++var2;
      }

      if ((this.access & 65536) != 0) {
         ++var2;
      }

      if ((this.access & 131072) != 0) {
         ++var2;
      }

      var1.put2(var2);
      int var3;
      if (this.code.length > 0) {
         var3 = 12 + this.code.length + 8 * this.catchCount;
         if (this.localVar != null) {
            var3 += 8 + this.localVar.length;
         }

         if (this.lineNumber != null) {
            var3 += 8 + this.lineNumber.length;
         }

         var1.put2(this.cw.newUTF8("Code").index).put4(var3);
         var1.put2(this.maxStack).put2(this.maxLocals);
         var1.put4(this.code.length).putByteArray(this.code.data, 0, this.code.length);
         var1.put2(this.catchCount);
         if (this.catchCount > 0) {
            var1.putByteArray(this.catchTable.data, 0, this.catchTable.length);
         }

         var2 = 0;
         if (this.localVar != null) {
            ++var2;
         }

         if (this.lineNumber != null) {
            ++var2;
         }

         var1.put2(var2);
         if (this.localVar != null) {
            var1.put2(this.cw.newUTF8("LocalVariableTable").index);
            var1.put4(this.localVar.length + 2).put2(this.localVarCount);
            var1.putByteArray(this.localVar.data, 0, this.localVar.length);
         }

         if (this.lineNumber != null) {
            var1.put2(this.cw.newUTF8("LineNumberTable").index);
            var1.put4(this.lineNumber.length + 2).put2(this.lineNumberCount);
            var1.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
         }
      }

      if (this.exceptionCount > 0) {
         var1.put2(this.cw.newUTF8("Exceptions").index).put4(2 * this.exceptionCount + 2);
         var1.put2(this.exceptionCount);

         for(var3 = 0; var3 < this.exceptionCount; ++var3) {
            var1.put2(this.exceptions[var3]);
         }
      }

      if ((this.access & 65536) != 0) {
         var1.put2(this.cw.newUTF8("Synthetic").index).put4(0);
      }

      if ((this.access & 131072) != 0) {
         var1.put2(this.cw.newUTF8("Deprecated").index).put4(0);
      }

   }

   protected int[] resizeInstructions(int[] var1, int[] var2, int var3) {
      byte[] var4 = this.code.data;
      int[] var5 = new int[var3];
      int[] var6 = new int[var3];
      System.arraycopy(var1, 0, var5, 0, var3);
      System.arraycopy(var2, 0, var6, 0, var3);
      boolean[] var7 = new boolean[this.code.length];
      int var8 = 3;

      int var9;
      int var11;
      int var12;
      int var13;
      do {
         if (var8 == 3) {
            var8 = 2;
         }

         var9 = 0;

         while(var9 < var4.length) {
            int var10 = var4[var9] & 255;
            var11 = 0;
            switch(ClassWriter.TYPE[var10]) {
            case 0:
            case 4:
               ++var9;
               break;
            case 1:
            case 3:
            case 10:
               var9 += 2;
               break;
            case 2:
            case 5:
            case 6:
            case 11:
            case 12:
               var9 += 3;
               break;
            case 7:
               var9 += 5;
               break;
            case 8:
               if (var10 > 201) {
                  var10 = var10 < 218 ? var10 - 49 : var10 - 20;
                  var12 = var9 + readUnsignedShort(var4, var9 + 1);
               } else {
                  var12 = var9 + readShort(var4, var9 + 1);
               }

               var13 = getNewOffset(var5, var6, var9, var12);
               if ((var13 < -32768 || var13 > 32767) && !var7[var9]) {
                  if (var10 != 167 && var10 != 168) {
                     var11 = 5;
                  } else {
                     var11 = 2;
                  }

                  var7[var9] = true;
               }

               var9 += 3;
               break;
            case 9:
               var9 += 5;
               break;
            case 13:
               if (var8 == 1) {
                  var13 = getNewOffset(var5, var6, 0, var9);
                  var11 = -(var13 & 3);
               } else if (!var7[var9]) {
                  var11 = var9 & 3;
                  var7[var9] = true;
               }

               var9 = var9 + 4 - (var9 & 3);
               var9 += 4 * (readInt(var4, var9 + 8) - readInt(var4, var9 + 4) + 1) + 12;
               break;
            case 14:
               if (var8 == 1) {
                  var13 = getNewOffset(var5, var6, 0, var9);
                  var11 = -(var13 & 3);
               } else if (!var7[var9]) {
                  var11 = var9 & 3;
                  var7[var9] = true;
               }

               var9 = var9 + 4 - (var9 & 3);
               var9 += 8 * readInt(var4, var9 + 4) + 8;
               break;
            case 15:
            default:
               var9 += 4;
               break;
            case 16:
               var10 = var4[var9 + 1] & 255;
               if (var10 == 132) {
                  var9 += 6;
               } else {
                  var9 += 4;
               }
            }

            if (var11 != 0) {
               int[] var14 = new int[var5.length + 1];
               int[] var15 = new int[var6.length + 1];
               System.arraycopy(var5, 0, var14, 0, var5.length);
               System.arraycopy(var6, 0, var15, 0, var6.length);
               var14[var5.length] = var9;
               var15[var6.length] = var11;
               var5 = var14;
               var6 = var15;
               if (var11 > 0) {
                  var8 = 3;
               }
            }
         }

         if (var8 < 3) {
            --var8;
         }
      } while(var8 != 0);

      ByteVector var19 = new ByteVector(this.code.length);
      var9 = 0;

      while(true) {
         label205:
         while(var9 < this.code.length) {
            for(int var16 = var5.length - 1; var16 >= 0; --var16) {
               if (var5[var16] == var9 && var16 < var3) {
                  if (var2[var16] > 0) {
                     var19.putByteArray((byte[])null, 0, var2[var16]);
                  } else {
                     var19.length += var2[var16];
                  }

                  var1[var16] = var19.length;
               }
            }

            var11 = var4[var9] & 255;
            int var17;
            int var18;
            int var20;
            switch(ClassWriter.TYPE[var11]) {
            case 0:
            case 4:
               var19.put1(var11);
               ++var9;
               break;
            case 1:
            case 3:
            case 10:
               var19.putByteArray(var4, var9, 2);
               var9 += 2;
               break;
            case 2:
            case 5:
            case 6:
            case 11:
            case 12:
               var19.putByteArray(var4, var9, 3);
               var9 += 3;
               break;
            case 7:
               var19.putByteArray(var4, var9, 5);
               var9 += 5;
               break;
            case 8:
               if (var11 > 201) {
                  var11 = var11 < 218 ? var11 - 49 : var11 - 20;
                  var12 = var9 + readUnsignedShort(var4, var9 + 1);
               } else {
                  var12 = var9 + readShort(var4, var9 + 1);
               }

               var13 = getNewOffset(var5, var6, var9, var12);
               if (var13 >= -32768 && var13 <= 32767) {
                  var19.put1(var11);
                  var19.put2(var13);
               } else {
                  if (var11 == 167) {
                     var19.put1(200);
                  } else if (var11 == 168) {
                     var19.put1(201);
                  } else {
                     var19.put1(var11 <= 166 ? (var11 + 1 ^ 1) - 1 : var11 ^ 1);
                     var19.put2(8);
                     var19.put1(200);
                     var13 -= 3;
                  }

                  var19.put4(var13);
               }

               var9 += 3;
               break;
            case 9:
               var12 = var9 + readInt(var4, var9 + 1);
               var13 = getNewOffset(var5, var6, var9, var12);
               var19.put1(var11);
               var19.put4(var13);
               var9 += 5;
               break;
            case 13:
               var17 = var9;
               var9 = var9 + 4 - (var9 & 3);
               var20 = var19.length;
               var19.put1(170);

               while(var19.length % 4 != 0) {
                  var19.put1(0);
               }

               var12 = var17 + readInt(var4, var9);
               var9 += 4;
               var13 = getNewOffset(var5, var6, var17, var12);
               var19.put4(var13);
               var18 = readInt(var4, var9);
               var9 += 4;
               var19.put4(var18);
               var18 = readInt(var4, var9) - var18 + 1;
               var9 += 4;
               var19.put4(readInt(var4, var9 - 4));

               while(true) {
                  if (var18 <= 0) {
                     continue label205;
                  }

                  var12 = var17 + readInt(var4, var9);
                  var9 += 4;
                  var13 = getNewOffset(var5, var6, var17, var12);
                  var19.put4(var13);
                  --var18;
               }
            case 14:
               var17 = var9;
               var9 = var9 + 4 - (var9 & 3);
               var20 = var19.length;
               var19.put1(171);

               while(var19.length % 4 != 0) {
                  var19.put1(0);
               }

               var12 = var17 + readInt(var4, var9);
               var9 += 4;
               var13 = getNewOffset(var5, var6, var17, var12);
               var19.put4(var13);
               var18 = readInt(var4, var9);
               var9 += 4;
               var19.put4(var18);

               while(true) {
                  if (var18 <= 0) {
                     continue label205;
                  }

                  var19.put4(readInt(var4, var9));
                  var9 += 4;
                  var12 = var17 + readInt(var4, var9);
                  var9 += 4;
                  var13 = getNewOffset(var5, var6, var17, var12);
                  var19.put4(var13);
                  --var18;
               }
            case 15:
            default:
               var19.putByteArray(var4, var9, 4);
               var9 += 4;
               break;
            case 16:
               var11 = var4[var9 + 1] & 255;
               if (var11 == 132) {
                  var19.putByteArray(var4, var9, 6);
                  var9 += 6;
               } else {
                  var19.putByteArray(var4, var9, 4);
                  var9 += 4;
               }
            }
         }

         if (this.catchTable != null) {
            var4 = this.catchTable.data;

            for(var9 = 0; var9 < this.catchTable.length; var9 += 8) {
               writeShort(var4, var9, getNewOffset(var5, var6, 0, readUnsignedShort(var4, var9)));
               writeShort(var4, var9 + 2, getNewOffset(var5, var6, 0, readUnsignedShort(var4, var9 + 2)));
               writeShort(var4, var9 + 4, getNewOffset(var5, var6, 0, readUnsignedShort(var4, var9 + 4)));
            }
         }

         if (this.localVar != null) {
            var4 = this.localVar.data;

            for(var9 = 0; var9 < this.localVar.length; var9 += 10) {
               var12 = readUnsignedShort(var4, var9);
               var13 = getNewOffset(var5, var6, 0, var12);
               writeShort(var4, var9, var13);
               var12 += readUnsignedShort(var4, var9 + 2);
               var13 = getNewOffset(var5, var6, 0, var12) - var13;
               writeShort(var4, var9, var13);
            }
         }

         if (this.lineNumber != null) {
            var4 = this.lineNumber.data;

            for(var9 = 0; var9 < this.lineNumber.length; var9 += 4) {
               writeShort(var4, var9, getNewOffset(var5, var6, 0, readUnsignedShort(var4, var9)));
            }
         }

         this.code = var19;
         return var1;
      }
   }

   static int readUnsignedShort(byte[] var0, int var1) {
      return (var0[var1] & 255) << 8 | var0[var1 + 1] & 255;
   }

   static short readShort(byte[] var0, int var1) {
      return (short)((var0[var1] & 255) << 8 | var0[var1 + 1] & 255);
   }

   static int readInt(byte[] var0, int var1) {
      return (var0[var1] & 255) << 24 | (var0[var1 + 1] & 255) << 16 | (var0[var1 + 2] & 255) << 8 | var0[var1 + 3] & 255;
   }

   static void writeShort(byte[] var0, int var1, int var2) {
      var0[var1] = (byte)(var2 >>> 8);
      var0[var1 + 1] = (byte)var2;
   }

   static int getNewOffset(int[] var0, int[] var1, int var2, int var3) {
      int var4 = var3 - var2;

      for(int var5 = 0; var5 < var0.length; ++var5) {
         if (var2 < var0[var5] && var0[var5] <= var3) {
            var4 += var1[var5];
         } else if (var3 < var0[var5] && var0[var5] <= var2) {
            var4 -= var1[var5];
         }
      }

      return var4;
   }

   protected int getCodeSize() {
      return this.code.length;
   }

   protected byte[] getCode() {
      return this.code.data;
   }

   static {
      int[] var0 = new int[202];
      String var1 = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var0[var2] = var1.charAt(var2) - 69;
      }

      SIZE = var0;
   }
}
