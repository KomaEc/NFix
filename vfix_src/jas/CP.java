package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class CP {
   String uniq;

   String getUniq() {
      return this.uniq;
   }

   abstract void resolve(ClassEnv var1);

   abstract void write(ClassEnv var1, DataOutputStream var2) throws IOException, jasError;
}
