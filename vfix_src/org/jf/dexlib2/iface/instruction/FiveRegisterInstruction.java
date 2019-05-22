package org.jf.dexlib2.iface.instruction;

public interface FiveRegisterInstruction extends VariableRegisterInstruction {
   int getRegisterC();

   int getRegisterD();

   int getRegisterE();

   int getRegisterF();

   int getRegisterG();
}
