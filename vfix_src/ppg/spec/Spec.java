package ppg.spec;

import java.util.Vector;
import ppg.PPGError;
import ppg.code.ActionCode;
import ppg.code.Code;
import ppg.code.InitCode;
import ppg.code.ParserCode;
import ppg.code.ScanCode;
import ppg.parse.Unparse;

public abstract class Spec implements Unparse {
   protected String packageName;
   protected Vector imports;
   protected Vector symbols;
   protected Vector prec;
   protected InitCode initCode = null;
   protected ActionCode actionCode = null;
   protected ParserCode parserCode = null;
   protected ScanCode scanCode = null;
   protected PPGSpec child = null;

   public void setPkgName(String pkgName) {
      if (pkgName != null) {
         this.packageName = pkgName;
      }

   }

   public void replaceCode(Vector codeParts) {
      if (codeParts != null) {
         Code code = null;

         for(int i = 0; i < codeParts.size(); ++i) {
            try {
               code = (Code)codeParts.elementAt(i);
               if (code instanceof ActionCode && code != null) {
                  this.actionCode = (ActionCode)code.clone();
               } else if (code instanceof InitCode && code != null) {
                  this.initCode = (InitCode)code.clone();
               } else if (code instanceof ParserCode && code != null) {
                  this.parserCode = (ParserCode)code.clone();
               } else if (code != null) {
                  this.scanCode = (ScanCode)code.clone();
               }
            } catch (Exception var5) {
               System.err.println("ppg:  Spec::replaceCode(): not a code segment found in code Vector: " + (code == null ? "null" : code.toString()));
               System.exit(1);
            }
         }

      }
   }

   public void addImports(Vector imp) {
      if (imp != null) {
         for(int i = 0; i < imp.size(); ++i) {
            this.imports.addElement(imp.elementAt(i));
         }

      }
   }

   public void setChild(PPGSpec childSpec) {
      this.child = childSpec;
   }

   public void parseChain(String basePath) {
   }

   public abstract CUPSpec coalesce() throws PPGError;
}
