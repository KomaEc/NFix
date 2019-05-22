package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class MethodHandleCP extends CP implements RuntimeConstants {
   public static final int STATIC_METHOD_KIND = 6;
   int kind;
   CP fieldOrMethod;

   public MethodHandleCP(int kind, String ownerName, String fieldOrMethodName, String sig) {
      this.uniq = kind + "$gfd¤" + ownerName + "&%$91&" + fieldOrMethodName + "*(012$" + sig;
      if (kind < 5) {
         this.fieldOrMethod = new FieldCP(ownerName, fieldOrMethodName, sig);
      } else {
         this.fieldOrMethod = new MethodCP(ownerName, fieldOrMethodName, sig);
      }

      this.kind = kind;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.fieldOrMethod);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeByte(15);
      out.writeByte(this.kind);
      out.writeShort(e.getCPIndex(this.fieldOrMethod));
   }
}
