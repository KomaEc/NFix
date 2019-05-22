package ppg.spec;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;
import ppg.PPGError;
import ppg.atoms.GrammarPart;
import ppg.atoms.Nonterminal;
import ppg.atoms.Precedence;
import ppg.atoms.Production;
import ppg.atoms.SymbolList;
import ppg.util.CodeWriter;

public class CUPSpec extends Spec {
   private Vector productions;
   private Hashtable ntProds;
   private String start;
   private final int NT_NOT_FOUND = -1;

   public CUPSpec(String pkg, Vector imp, Vector codeParts, Vector syms, Vector precedence, String startSym, Vector prods) {
      this.packageName = pkg;
      this.imports = imp;
      this.replaceCode(codeParts);
      this.symbols = syms;
      this.prec = precedence;
      this.start = startSym;
      this.productions = prods;
      this.ntProds = new Hashtable();
      this.hashNonterminals();
   }

   public void setStart(String startSym) {
      if (startSym != null) {
         this.start = startSym;
      }

   }

   private void hashNonterminals() {
      this.ntProds.clear();
      if (this.productions != null) {
         for(int i = 0; i < this.productions.size(); ++i) {
            Production prod = (Production)this.productions.elementAt(i);
            this.ntProds.put(prod.getLHS().getName(), new Integer(i));
         }

      }
   }

   public CUPSpec coalesce() {
      return this;
   }

   public Production findProduction(Production p) {
      Nonterminal nt = p.getLHS();
      int pos = this.errorNotFound(this.findNonterminal(nt), nt);
      Production sourceProd = (Production)this.productions.elementAt(pos);
      Vector sourceRHSList = sourceProd.getRHS();
      Vector rhs = p.getRHS();
      Production result = new Production(nt, new Vector());

      for(int i = 0; i < rhs.size(); ++i) {
         Vector toMatch = (Vector)rhs.elementAt(i);

         for(int j = 0; j < sourceRHSList.size(); ++j) {
            Vector source = (Vector)sourceRHSList.elementAt(j);
            if (Production.isSameProduction(toMatch, source)) {
               Vector clone = new Vector();

               for(int k = 0; k < source.size(); ++k) {
                  clone.addElement(((GrammarPart)source.elementAt(k)).clone());
               }

               result.addToRHS(clone);
               break;
            }
         }
      }

      return result;
   }

   public void removeEmptyProductions() {
      for(int i = 0; i < this.productions.size(); ++i) {
         Production prod = (Production)this.productions.elementAt(i);
         if (prod.getRHS().size() == 0) {
            this.productions.removeElementAt(i);
            --i;
         }
      }

   }

   public Object clone() {
      String newPkgName = this.packageName == null ? null : this.packageName.toString();
      Vector newImports = new Vector();

      for(int i = 0; i < this.imports.size(); ++i) {
         newImports.addElement(((String)this.imports.elementAt(i)).toString());
      }

      Vector newCode = new Vector();
      if (this.actionCode != null) {
         newCode.addElement(this.actionCode);
      }

      if (this.initCode != null) {
         newCode.addElement(this.initCode);
      }

      if (this.parserCode != null) {
         newCode.addElement(this.parserCode);
      }

      if (this.scanCode != null) {
         newCode.addElement(this.scanCode);
      }

      Vector newSymbols = new Vector();

      for(int i = 0; i < this.symbols.size(); ++i) {
         newSymbols.addElement(((SymbolList)this.symbols.elementAt(i)).clone());
      }

      Vector newPrec = new Vector();

      for(int i = 0; i < this.prec.size(); ++i) {
         newPrec.addElement(((Precedence)this.prec.elementAt(i)).clone());
      }

      String newStart = this.start == null ? null : this.start.toString();
      Vector newProductions = new Vector();

      for(int i = 0; i < this.productions.size(); ++i) {
         newProductions.addElement(((Production)this.productions.elementAt(i)).clone());
      }

      return new CUPSpec(newPkgName, newImports, newCode, newSymbols, newPrec, newStart, newProductions);
   }

   public void addSymbols(Vector syms) {
      if (syms != null) {
         for(int i = 0; i < syms.size(); ++i) {
            this.symbols.addElement(syms.elementAt(i));
         }

      }
   }

   public void dropSymbol(String gs) throws PPGError {
      boolean dropped = false;

      for(int i = 0; i < this.symbols.size(); ++i) {
         SymbolList list = (SymbolList)this.symbols.elementAt(i);
         dropped = dropped || list.dropSymbol(gs);
      }

   }

   public void dropProductions(Production p) {
      Nonterminal nt = p.getLHS();
      int pos = this.errorNotFound(this.findNonterminal(nt), nt);
      Production prod = (Production)this.productions.elementAt(pos);
      prod.drop(p);
   }

   public void dropProductions(Nonterminal nt) {
      int pos = this.errorNotFound(this.findNonterminal(nt), nt);
      Production prod = (Production)this.productions.elementAt(pos);
      prod.drop((Production)prod.clone());
   }

   public void dropAllProductions(String nt) {
      int pos = this.findNonterminal(nt);
      if (pos != -1) {
         this.productions.removeElementAt(pos);
         this.hashNonterminals();
      }
   }

   public void addProductions(Production p) {
      Nonterminal nt = p.getLHS();
      int pos = this.findNonterminal(nt);
      if (pos == -1) {
         this.ntProds.put(nt.getName(), new Integer(this.productions.size()));
         this.productions.addElement(p);
      } else {
         Production prod = (Production)this.productions.elementAt(pos);
         prod.add(p);
      }

   }

   private int findNonterminal(Nonterminal nt) {
      return this.findNonterminal(nt.getName());
   }

   private int findNonterminal(String nt) {
      Integer pos = (Integer)this.ntProds.get(nt);
      return pos == null ? -1 : pos;
   }

   private int errorNotFound(int i, Nonterminal nt) {
      if (i == -1) {
         System.err.println("ppg: nonterminal " + nt + " not found.");
         System.exit(1);
      }

      return i;
   }

   public void unparse(CodeWriter cw) {
      cw.begin(0);
      if (this.packageName != null) {
         cw.write("package " + this.packageName + ";");
         cw.newline();
         cw.newline();
      }

      int i;
      for(i = 0; i < this.imports.size(); ++i) {
         cw.write("import " + (String)this.imports.elementAt(i) + ";");
         cw.newline();
      }

      if (this.imports.size() > 0) {
         cw.newline();
      }

      if (this.actionCode != null) {
         cw.write(this.actionCode.toString());
      }

      if (this.initCode != null) {
         cw.write(this.initCode.toString());
      }

      if (this.parserCode != null) {
         cw.write(this.parserCode.toString());
      }

      if (this.scanCode != null) {
         cw.write(this.scanCode.toString());
      }

      cw.newline();

      for(i = 0; i < this.symbols.size(); ++i) {
         cw.write(((SymbolList)this.symbols.elementAt(i)).toString());
         cw.newline();
      }

      cw.newline();

      for(i = 0; i < this.prec.size(); ++i) {
         cw.write(((Precedence)this.prec.elementAt(i)).toString());
         cw.newline();
      }

      cw.newline();
      if (this.start != null) {
         cw.write("start with " + this.start + ";");
         cw.newline();
         cw.newline();
      }

      for(i = 0; i < this.productions.size(); ++i) {
         ((Production)this.productions.elementAt(i)).unparse(cw);
      }

      cw.newline();
      cw.end();
   }

   public void export(PrintStream out) throws Exception {
      out.println("package " + this.packageName + ";");
      out.println();

      int i;
      for(i = 0; i < this.imports.size(); ++i) {
         out.println("import " + (String)this.imports.elementAt(i) + ";");
      }

      out.println();
      if (this.actionCode != null) {
         out.println(this.actionCode.toString());
      }

      if (this.initCode != null) {
         out.println(this.initCode.toString());
      }

      if (this.parserCode != null) {
         out.println(this.parserCode.toString());
      }

      if (this.scanCode != null) {
         out.println(this.scanCode.toString());
      }

      out.println();

      for(i = 0; i < this.symbols.size(); ++i) {
         out.println(((SymbolList)this.symbols.elementAt(i)).toString());
      }

      out.println();

      for(i = 0; i < this.prec.size(); ++i) {
         out.println(((Precedence)this.prec.elementAt(i)).toString());
      }

      out.println();
      out.println("start with " + this.start + ";");
      out.println();

      for(i = 0; i < this.productions.size(); ++i) {
         out.println(((Production)this.productions.elementAt(i)).toString());
      }

      out.println();
      out.flush();
      out.close();
   }
}
