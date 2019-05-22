package corg.vfix.sa.vfg.build;

import corg.vfix.fl.stack.StackTrace;
import corg.vfix.sa.vfg.VFG;

public class VFGConstructor {
   public static VFG build() throws Exception {
      VFG vfg = IntraVFGConstructor.buildFromSink(StackTrace.getNullMtd(), StackTrace.getNullStmt(), StackTrace.getNullPointer());
      InterVFGConstructor.build(vfg);
      return vfg;
   }
}
