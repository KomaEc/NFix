package soot.dexpler.instructions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.instruction.formats.Instruction31t;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.ShortType;
import soot.Type;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NumericConstant;
import soot.jimple.Stmt;

public class FillArrayDataInstruction extends PseudoInstruction {
   private static final Logger logger = LoggerFactory.getLogger(FillArrayDataInstruction.class);

   public FillArrayDataInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (!(this.instruction instanceof Instruction31t)) {
         throw new IllegalArgumentException("Expected Instruction31t but got: " + this.instruction.getClass());
      } else {
         Instruction31t fillArrayInstr = (Instruction31t)this.instruction;
         int destRegister = fillArrayInstr.getRegisterA();
         int offset = fillArrayInstr.getCodeOffset();
         int targetAddress = this.codeAddress + offset;
         Instruction referenceTable = body.instructionAtAddress(targetAddress).instruction;
         if (!(referenceTable instanceof ArrayPayload)) {
            throw new RuntimeException("Address " + targetAddress + "refers to an invalid PseudoInstruction.");
         } else {
            ArrayPayload arrayTable = (ArrayPayload)referenceTable;
            Local arrayReference = body.getRegisterLocal(destRegister);
            List<Number> elements = arrayTable.getArrayElements();
            int numElements = elements.size();
            Stmt firstAssign = null;

            for(int i = 0; i < numElements; ++i) {
               ArrayRef arrayRef = Jimple.v().newArrayRef(arrayReference, IntConstant.v(i));
               NumericConstant element = this.getArrayElement((Number)elements.get(i), body, destRegister);
               if (element == null) {
                  break;
               }

               AssignStmt assign = Jimple.v().newAssignStmt(arrayRef, element);
               this.addTags(assign);
               body.add(assign);
               if (i == 0) {
                  firstAssign = assign;
               }
            }

            if (firstAssign == null) {
               firstAssign = Jimple.v().newNopStmt();
               body.add((Unit)firstAssign);
            }

            this.setUnit((Unit)firstAssign);
         }
      }
   }

   private NumericConstant getArrayElement(Number element, DexBody body, int arrayRegister) {
      List<DexlibAbstractInstruction> instructions = body.instructionsBefore(this);
      Set<Integer> usedRegisters = new HashSet();
      usedRegisters.add(arrayRegister);
      Type elementType = null;
      Iterator var7 = instructions.iterator();

      label72:
      while(var7.hasNext()) {
         DexlibAbstractInstruction i = (DexlibAbstractInstruction)var7.next();
         if (usedRegisters.isEmpty()) {
            break;
         }

         Iterator var9 = usedRegisters.iterator();

         int reg;
         while(var9.hasNext()) {
            reg = (Integer)var9.next();
            if (i instanceof NewArrayInstruction) {
               NewArrayInstruction newArrayInstruction = (NewArrayInstruction)i;
               Instruction22c instruction22c = (Instruction22c)newArrayInstruction.instruction;
               if (instruction22c.getRegisterA() == reg) {
                  ArrayType arrayType = (ArrayType)DexType.toSoot((TypeReference)instruction22c.getReference());
                  elementType = arrayType.getElementType();
                  break label72;
               }
            }
         }

         var9 = usedRegisters.iterator();

         int newRegister;
         do {
            if (!var9.hasNext()) {
               continue label72;
            }

            reg = (Integer)var9.next();
            newRegister = i.movesToRegister(reg);
         } while(newRegister == -1);

         usedRegisters.add(newRegister);
         usedRegisters.remove(reg);
      }

      if (elementType == null) {
         logger.warn("Unable to find array type to type array elements! Array was not defined! (obfuscated bytecode?)");
         return null;
      } else {
         Object value;
         if (elementType instanceof BooleanType) {
            value = IntConstant.v(element.intValue());
            IntConstant ic = (IntConstant)value;
            if (ic.value != 0) {
               value = IntConstant.v(1);
            }
         } else if (elementType instanceof ByteType) {
            value = IntConstant.v(element.byteValue());
         } else if (!(elementType instanceof CharType) && !(elementType instanceof ShortType)) {
            if (elementType instanceof DoubleType) {
               value = DoubleConstant.v(Double.longBitsToDouble(element.longValue()));
            } else if (elementType instanceof FloatType) {
               value = FloatConstant.v(Float.intBitsToFloat(element.intValue()));
            } else if (elementType instanceof IntType) {
               value = IntConstant.v(element.intValue());
            } else {
               if (!(elementType instanceof LongType)) {
                  throw new RuntimeException("Invalid Array Type occured in FillArrayDataInstruction: " + elementType);
               }

               value = LongConstant.v(element.longValue());
            }
         } else {
            value = IntConstant.v(element.shortValue());
         }

         return (NumericConstant)value;
      }
   }

   public void computeDataOffsets(DexBody body) {
      if (!(this.instruction instanceof Instruction31t)) {
         throw new IllegalArgumentException("Expected Instruction31t but got: " + this.instruction.getClass());
      } else {
         Instruction31t fillArrayInstr = (Instruction31t)this.instruction;
         int offset = fillArrayInstr.getCodeOffset();
         int targetAddress = this.codeAddress + offset;
         Instruction referenceTable = body.instructionAtAddress(targetAddress).instruction;
         if (!(referenceTable instanceof ArrayPayload)) {
            throw new RuntimeException("Address 0x" + Integer.toHexString(targetAddress) + " refers to an invalid PseudoInstruction (" + referenceTable.getClass() + ").");
         } else {
            ArrayPayload arrayTable = (ArrayPayload)referenceTable;
            int numElements = arrayTable.getArrayElements().size();
            int widthElement = arrayTable.getElementWidth();
            int size = widthElement * numElements / 2;
            this.setDataFirstByte(targetAddress + 3);
            this.setDataLastByte(targetAddress + 3 + size);
            this.setDataSize(size);
         }
      }
   }
}
