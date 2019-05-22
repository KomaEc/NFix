package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class BootstrapMethodsAttribute {
   static CP attr = new AsciiCP("BootstrapMethods");
   short attr_length = 0;
   short num = 0;
   ArrayList<Pair<MethodHandleCP, CP[]>> list = new ArrayList();

   int addEntry(MethodHandleCP bsm, CP[] argCPs) {
      int i = 0;

      for(Iterator var4 = this.list.iterator(); var4.hasNext(); ++i) {
         Pair<MethodHandleCP, CP[]> pair = (Pair)var4.next();
         MethodHandleCP mh = (MethodHandleCP)pair.getO1();
         CP[] args = (CP[])pair.getO2();
         if (mh.uniq.equals(bsm.uniq)) {
            boolean equal = true;

            for(int j = 0; j < args.length; ++j) {
               CP arg = args[j];
               CP otherArg = argCPs[j];
               if (!arg.uniq.equals(otherArg.uniq)) {
                  equal = false;
                  break;
               }
            }

            if (equal) {
               return i;
            }
         }
      }

      this.list.add(new Pair(bsm, argCPs));
      return this.list.size() - 1;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(attr);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(attr));
      out.writeInt(this.size());
      out.writeShort(this.list.size());
      Iterator var3 = this.list.iterator();

      while(var3.hasNext()) {
         Pair<MethodHandleCP, CP[]> pair = (Pair)var3.next();
         out.writeShort(e.getCPIndex((CP)pair.getO1()));
         CP[] cps = (CP[])pair.getO2();
         out.writeShort(cps.length);
         CP[] var6 = cps;
         int var7 = cps.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            CP cp = var6[var8];
            out.writeShort(e.getCPIndex(cp));
         }
      }

   }

   int size() {
      int size = 2;

      Pair pair;
      for(Iterator var2 = this.list.iterator(); var2.hasNext(); size += ((CP[])pair.getO2()).length * 2) {
         pair = (Pair)var2.next();
         size += 4;
      }

      return size;
   }
}
