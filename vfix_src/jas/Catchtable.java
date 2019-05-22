package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class Catchtable {
   Vector entries = new Vector();

   public void addEntry(CatchEntry entry) {
      this.entries.addElement(entry);
   }

   public void addEntry(Label start, Label end, Label handler, CP cat) {
      this.addEntry(new CatchEntry(start, end, handler, cat));
   }

   void resolve(ClassEnv e) {
      Enumeration en = this.entries.elements();

      while(en.hasMoreElements()) {
         CatchEntry ce = (CatchEntry)((CatchEntry)en.nextElement());
         ce.resolve(e);
      }

   }

   int size() {
      return 8 * this.entries.size();
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      out.writeShort(this.entries.size());
      Enumeration en = this.entries.elements();

      while(en.hasMoreElements()) {
         CatchEntry entry = (CatchEntry)((CatchEntry)en.nextElement());
         entry.write(e, ce, out);
      }

   }
}
