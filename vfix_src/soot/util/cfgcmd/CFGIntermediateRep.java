package soot.util.cfgcmd;

import soot.Body;
import soot.baf.Baf;
import soot.grimp.Grimp;
import soot.jimple.JimpleBody;
import soot.shimple.Shimple;

public abstract class CFGIntermediateRep extends CFGOptionMatcher.CFGOption {
   public static final CFGIntermediateRep JIMPLE_IR = new CFGIntermediateRep("jimple") {
      public Body getBody(JimpleBody b) {
         return b;
      }
   };
   public static final CFGIntermediateRep BAF_IR = new CFGIntermediateRep("baf") {
      public Body getBody(JimpleBody b) {
         return Baf.v().newBody((Body)b);
      }
   };
   public static final CFGIntermediateRep GRIMP_IR = new CFGIntermediateRep("grimp") {
      public Body getBody(JimpleBody b) {
         return Grimp.v().newBody(b, "gb");
      }
   };
   public static final CFGIntermediateRep SHIMPLE_IR = new CFGIntermediateRep("shimple") {
      public Body getBody(JimpleBody b) {
         return Shimple.v().newBody((Body)b);
      }
   };
   public static final CFGIntermediateRep VIA_SHIMPLE_JIMPLE_IR = new CFGIntermediateRep("viaShimpleJimple") {
      public Body getBody(JimpleBody b) {
         return Shimple.v().newJimpleBody(Shimple.v().newBody((Body)b));
      }
   };
   private static final CFGOptionMatcher irOptions;

   private CFGIntermediateRep(String name) {
      super(name);
   }

   public abstract Body getBody(JimpleBody var1);

   public static CFGIntermediateRep getIR(String name) {
      return (CFGIntermediateRep)irOptions.match(name);
   }

   public static String help(int initialIndent, int rightMargin, int hangingIndent) {
      return irOptions.help(initialIndent, rightMargin, hangingIndent);
   }

   // $FF: synthetic method
   CFGIntermediateRep(String x0, Object x1) {
      this(x0);
   }

   static {
      irOptions = new CFGOptionMatcher(new CFGIntermediateRep[]{JIMPLE_IR, BAF_IR, GRIMP_IR, SHIMPLE_IR, VIA_SHIMPLE_JIMPLE_IR});
   }
}
