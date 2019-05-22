package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;

public abstract class PseudoInstruction extends DexlibAbstractInstruction {
   int dataFirstByte = -1;
   int dataLastByte = -1;
   int dataSize = -1;
   byte[] data = null;
   boolean loaded = false;

   public PseudoInstruction(Instruction instruction, int codeAddress) {
      super(instruction, codeAddress);
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public void setLoaded(boolean loaded) {
      this.loaded = loaded;
   }

   public byte[] getData() {
      return this.data;
   }

   protected void setData(byte[] data) {
      this.data = data;
   }

   public int getDataFirstByte() {
      if (this.dataFirstByte == -1) {
         throw new RuntimeException("Error: dataFirstByte was not set!");
      } else {
         return this.dataFirstByte;
      }
   }

   protected void setDataFirstByte(int dataFirstByte) {
      this.dataFirstByte = dataFirstByte;
   }

   public int getDataLastByte() {
      if (this.dataLastByte == -1) {
         throw new RuntimeException("Error: dataLastByte was not set!");
      } else {
         return this.dataLastByte;
      }
   }

   protected void setDataLastByte(int dataLastByte) {
      this.dataLastByte = dataLastByte;
   }

   public int getDataSize() {
      if (this.dataSize == -1) {
         throw new RuntimeException("Error: dataFirstByte was not set!");
      } else {
         return this.dataSize;
      }
   }

   protected void setDataSize(int dataSize) {
      this.dataSize = dataSize;
   }

   public abstract void computeDataOffsets(DexBody var1);
}
