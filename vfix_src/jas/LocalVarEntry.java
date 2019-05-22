package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class LocalVarEntry {
   Label start;
   Label end;
   CP name;
   CP sig;
   int slot;

   public LocalVarEntry(Label startLabel, Label endLabel, String name, String sig, int slot) {
      this.start = startLabel;
      this.end = endLabel;
      this.name = new AsciiCP(name);
      this.sig = new AsciiCP(sig);
      this.slot = slot;
   }

   public LocalVarEntry(Label startLabel, Label endLabel, CP name, CP sig, int slot) {
      this.start = startLabel;
      this.end = endLabel;
      this.name = name;
      this.sig = sig;
      this.slot = slot;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.name);
      e.addCPItem(this.sig);
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      this.start.writeOffset(ce, (Insn)null, out);
      this.end.writeOffset(ce, this.start, out);
      out.writeShort(e.getCPIndex(this.name));
      out.writeShort(e.getCPIndex(this.sig));
      out.writeShort((short)this.slot);
   }
}
