package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

public class CodeAttr {
   static CP attr = new AsciiCP("Code");
   short stack_size = 1;
   short num_locals = 1;
   int code_size;
   Vector insns = new Vector();
   Hashtable insn_pc;
   Catchtable ctb = null;
   LineTableAttr ltab;
   LocalVarTableAttr lvar;
   Vector generic = new Vector();
   Vector sootAttrNames = new Vector();
   Vector sootAttrValues = new Vector();
   Hashtable labels;

   public void setCatchtable(Catchtable ctb) {
      this.ctb = ctb;
   }

   public void setLineTable(LineTableAttr ltab) {
      this.ltab = ltab;
   }

   public void setLocalVarTable(LocalVarTableAttr lvar) {
      this.lvar = lvar;
   }

   public void addGenericAttr(GenericAttr g) {
      this.generic.addElement(g);
   }

   public void addSootCodeAttr(String name, String value) {
      this.sootAttrNames.addElement(name);
      this.sootAttrValues.addElement(value);
   }

   Label getLabel(String name) {
      Label lab = (Label)this.labels.get(name);
      if (lab == null) {
         lab = new Label(name);
         this.labels.put(name, lab);
      }

      return lab;
   }

   public void setLabelTable(Hashtable labelTable) {
      this.labels = labelTable;
   }

   private int processSootAttributes() {
      Hashtable labelToPc = new Hashtable();
      int totalSize = 0;
      Enumeration enumeration = this.sootAttrValues.elements();
      Enumeration nameEnum = this.sootAttrNames.elements();

      while(enumeration.hasMoreElements()) {
         String attrValue = (String)enumeration.nextElement();
         String attrName = (String)nameEnum.nextElement();
         boolean isLabel = false;
         StringTokenizer st = new StringTokenizer(attrValue, "%", true);

         while(st.hasMoreTokens()) {
            String token = (String)st.nextElement();
            if (token.equals("%")) {
               isLabel = !isLabel;
            } else if (isLabel) {
               Integer i = (Integer)labelToPc.get(token);

               try {
                  if (i == null) {
                     labelToPc.put(token, new Integer(this.getPc(this.getLabel(token))));
                  }
               } catch (jasError var12) {
                  throw new RuntimeException(var12.toString());
               }
            }
         }

         byte[] data = CodeAttributeDecoder.decode(attrValue, labelToPc);
         GenericAttr ga = new GenericAttr(attrName, data);
         totalSize += ga.size();
         this.addGenericAttr(ga);
      }

      this.sootAttrNames.removeAllElements();
      this.sootAttrValues.removeAllElements();
      return totalSize;
   }

   public void addInsn(Insn insn) {
      this.insns.addElement(insn);
   }

   public void setStackSize(short stack_size) {
      this.stack_size = stack_size;
   }

   public void setVarSize(short num_vars) {
      this.num_locals = num_vars;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(attr);
      Enumeration gen = this.insns.elements();

      while(gen.hasMoreElements()) {
         Insn i = (Insn)((Insn)gen.nextElement());
         i.resolve(e);
      }

      if (this.ctb != null) {
         this.ctb.resolve(e);
      }

      if (this.ltab != null) {
         this.ltab.resolve(e);
      }

      if (this.lvar != null) {
         this.lvar.resolve(e);
      }

      gen = this.generic.elements();

      while(gen.hasMoreElements()) {
         GenericAttr gattr = (GenericAttr)gen.nextElement();
         gattr.resolve(e);
      }

   }

   public int getPc(Insn i) throws jasError {
      if (this.insn_pc == null) {
         throw new jasError("Internal error, insn_pc has not been initialized");
      } else {
         Integer tmp;
         if (i instanceof Label) {
            tmp = (Integer)((Integer)this.insn_pc.get(((Label)i).id));
         } else {
            tmp = (Integer)((Integer)this.insn_pc.get(i));
         }

         if (tmp == null) {
            throw new jasError(i + " has not been added to the code");
         } else {
            return tmp;
         }
      }
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      int code_size = 0;
      this.insn_pc = new Hashtable();

      Insn now;
      for(Enumeration en = this.insns.elements(); en.hasMoreElements(); code_size += now.size(e, this)) {
         now = (Insn)((Insn)en.nextElement());
         if (now instanceof Label) {
            this.insn_pc.put(((Label)now).id, new Integer(code_size));
         } else {
            this.insn_pc.put(now, new Integer(code_size));
         }
      }

      int total_size = code_size;
      if (this.ctb != null) {
         total_size = code_size + this.ctb.size();
      }

      if (this.ltab != null) {
         total_size += this.ltab.size();
      }

      if (this.lvar != null) {
         total_size += this.lvar.size();
      }

      GenericAttr gattr;
      Enumeration en;
      for(en = this.generic.elements(); en.hasMoreElements(); total_size += gattr.size()) {
         gattr = (GenericAttr)((GenericAttr)en.nextElement());
      }

      total_size += this.processSootAttributes();
      total_size += 12;
      out.writeShort(e.getCPIndex(attr));
      out.writeInt(total_size);
      out.writeShort(this.stack_size);
      out.writeShort(this.num_locals);
      out.writeInt(code_size);
      en = this.insns.elements();

      while(en.hasMoreElements()) {
         Insn now = (Insn)((Insn)en.nextElement());
         now.write(e, this, out);
      }

      if (this.ctb != null) {
         this.ctb.write(e, this, out);
      } else {
         out.writeShort(0);
      }

      short extra = 0;
      if (this.ltab != null) {
         ++extra;
      }

      if (this.lvar != null) {
         ++extra;
      }

      extra = (short)(extra + this.generic.size());
      out.writeShort(extra);
      if (this.ltab != null) {
         this.ltab.write(e, this, out);
      }

      if (this.lvar != null) {
         this.lvar.write(e, this, out);
      }

      Enumeration gen = this.generic.elements();

      while(gen.hasMoreElements()) {
         GenericAttr gattr = (GenericAttr)gen.nextElement();
         gattr.write(e, out);
      }

   }

   public String toString() {
      return "<#code-attr>";
   }
}
