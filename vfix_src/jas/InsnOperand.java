package jas;

import java.io.DataOutputStream;
import java.io.IOException;

abstract class InsnOperand {
   abstract void write(ClassEnv var1, CodeAttr var2, DataOutputStream var3) throws IOException, jasError;

   abstract int size(ClassEnv var1, CodeAttr var2) throws jasError;

   abstract void resolve(ClassEnv var1);

   void writePrefix(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
   }
}
