package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.NullType;
import soot.ShortType;
import soot.Type;
import soot.TypeSwitch;
import soot.baf.InstSwitch;
import soot.baf.PrimitiveCastInst;
import soot.util.Switch;

public class BPrimitiveCastInst extends AbstractInst implements PrimitiveCastInst {
   Type fromType;
   protected Type toType;

   public int getInCount() {
      return 1;
   }

   public int getInMachineCount() {
      return AbstractJasminClass.sizeOfType(this.fromType);
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return AbstractJasminClass.sizeOfType(this.toType);
   }

   public BPrimitiveCastInst(Type fromType, Type toType) {
      if (fromType instanceof NullType) {
         throw new RuntimeException("invalid fromType " + fromType);
      } else {
         this.fromType = fromType;
         this.toType = toType;
      }
   }

   public Object clone() {
      return new BPrimitiveCastInst(this.getFromType(), this.toType);
   }

   public Type getFromType() {
      return this.fromType;
   }

   public void setFromType(Type t) {
      this.fromType = t;
   }

   public Type getToType() {
      return this.toType;
   }

   public void setToType(Type t) {
      this.toType = t;
   }

   public final String getName() {
      TypeSwitch sw;
      this.fromType.apply(sw = new TypeSwitch() {
         public void defaultCase(Type ty) {
            throw new RuntimeException("invalid fromType " + BPrimitiveCastInst.this.fromType);
         }

         public void caseDoubleType(DoubleType ty) {
            if (BPrimitiveCastInst.this.toType.equals(IntType.v())) {
               this.setResult("d2i");
            } else if (BPrimitiveCastInst.this.toType.equals(LongType.v())) {
               this.setResult("d2l");
            } else {
               if (!BPrimitiveCastInst.this.toType.equals(FloatType.v())) {
                  throw new RuntimeException("invalid toType from double: " + BPrimitiveCastInst.this.toType);
               }

               this.setResult("d2f");
            }

         }

         public void caseFloatType(FloatType ty) {
            if (BPrimitiveCastInst.this.toType.equals(IntType.v())) {
               this.setResult("f2i");
            } else if (BPrimitiveCastInst.this.toType.equals(LongType.v())) {
               this.setResult("f2l");
            } else {
               if (!BPrimitiveCastInst.this.toType.equals(DoubleType.v())) {
                  throw new RuntimeException("invalid toType from float: " + BPrimitiveCastInst.this.toType);
               }

               this.setResult("f2d");
            }

         }

         public void caseIntType(IntType ty) {
            this.emitIntToTypeCast();
         }

         public void caseBooleanType(BooleanType ty) {
            this.emitIntToTypeCast();
         }

         public void caseByteType(ByteType ty) {
            this.emitIntToTypeCast();
         }

         public void caseCharType(CharType ty) {
            this.emitIntToTypeCast();
         }

         public void caseShortType(ShortType ty) {
            this.emitIntToTypeCast();
         }

         private void emitIntToTypeCast() {
            if (BPrimitiveCastInst.this.toType.equals(ByteType.v())) {
               this.setResult("i2b");
            } else if (BPrimitiveCastInst.this.toType.equals(CharType.v())) {
               this.setResult("i2c");
            } else if (BPrimitiveCastInst.this.toType.equals(ShortType.v())) {
               this.setResult("i2s");
            } else if (BPrimitiveCastInst.this.toType.equals(FloatType.v())) {
               this.setResult("i2f");
            } else if (BPrimitiveCastInst.this.toType.equals(LongType.v())) {
               this.setResult("i2l");
            } else if (BPrimitiveCastInst.this.toType.equals(DoubleType.v())) {
               this.setResult("i2d");
            } else if (BPrimitiveCastInst.this.toType.equals(IntType.v())) {
               this.setResult("");
            } else {
               if (!BPrimitiveCastInst.this.toType.equals(BooleanType.v())) {
                  throw new RuntimeException("invalid toType from int: " + BPrimitiveCastInst.this.toType);
               }

               this.setResult("");
            }

         }

         public void caseLongType(LongType ty) {
            if (BPrimitiveCastInst.this.toType.equals(IntType.v())) {
               this.setResult("l2i");
            } else if (BPrimitiveCastInst.this.toType.equals(FloatType.v())) {
               this.setResult("l2f");
            } else {
               if (!BPrimitiveCastInst.this.toType.equals(DoubleType.v())) {
                  throw new RuntimeException("invalid toType from long: " + BPrimitiveCastInst.this.toType);
               }

               this.setResult("l2d");
            }

         }
      });
      return (String)sw.getResult();
   }

   public String toString() {
      return this.getName() + this.getParameters();
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).casePrimitiveCastInst(this);
   }
}
