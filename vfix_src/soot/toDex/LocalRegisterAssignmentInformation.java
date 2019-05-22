package soot.toDex;

import soot.Local;

public class LocalRegisterAssignmentInformation {
   private Local local;
   private Register register;

   public LocalRegisterAssignmentInformation(Register register, Local local) {
      this.register = register;
      this.local = local;
   }

   public static LocalRegisterAssignmentInformation v(Register register, Local l) {
      return new LocalRegisterAssignmentInformation(register, l);
   }

   public Local getLocal() {
      return this.local;
   }

   public Register getRegister() {
      return this.register;
   }
}
