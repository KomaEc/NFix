package ppg.atoms;

import java.util.Vector;
import ppg.parse.Unparse;
import ppg.util.CodeWriter;

public class Production implements Unparse {
   private Nonterminal lhs;
   private Vector rhs;
   private static String HEADER = "ppg [nterm]: ";

   public Production(Nonterminal lhs, Vector rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
   }

   public Nonterminal getLHS() {
      return this.lhs;
   }

   public void setLHS(Nonterminal nt) {
      this.lhs = nt;
   }

   public Vector getRHS() {
      return this.rhs;
   }

   public Object clone() {
      return new Production((Nonterminal)this.lhs.clone(), (Vector)this.rhs.clone());
   }

   public void drop(Production prod) {
      Vector toDrop = prod.getRHS();

      for(int i = 0; i < toDrop.size(); ++i) {
         Vector target = (Vector)toDrop.elementAt(i);

         for(int j = 0; j < this.rhs.size(); ++j) {
            Vector source = (Vector)this.rhs.elementAt(j);
            if (isSameProduction(target, source)) {
               this.rhs.removeElementAt(j);
               break;
            }

            if (j == this.rhs.size() - 1) {
               System.err.println(HEADER + "no match found for production:");
               System.err.print(prod.getLHS() + " ::= ");

               for(int k = 0; k < target.size(); ++k) {
                  System.err.print(target.elementAt(k) + " ");
               }

               System.exit(1);
            }
         }
      }

   }

   public static boolean isSameProduction(Vector u, Vector v) {
      int uIdx = 0;
      int vIdx = 0;
      GrammarPart ug = null;
      GrammarPart vg = null;

      while(uIdx < u.size() && vIdx < v.size()) {
         ug = (GrammarPart)u.elementAt(uIdx);
         if (ug instanceof SemanticAction) {
            ++uIdx;
         } else {
            vg = (GrammarPart)v.elementAt(vIdx);
            if (vg instanceof SemanticAction) {
               ++vIdx;
            } else {
               if (!ug.equals(vg)) {
                  return false;
               }

               ++uIdx;
               ++vIdx;
            }
         }
      }

      if (uIdx == u.size() && vIdx == v.size()) {
         return true;
      } else if (uIdx < u.size()) {
         while(uIdx < u.size()) {
            ug = (GrammarPart)u.elementAt(uIdx);
            if (!(ug instanceof SemanticAction)) {
               return false;
            }

            ++uIdx;
         }

         return true;
      } else {
         while(vIdx < v.size()) {
            vg = (GrammarPart)v.elementAt(vIdx);
            if (!(vg instanceof SemanticAction)) {
               return false;
            }

            ++vIdx;
         }

         return true;
      }
   }

   public void union(Production prod) {
      Vector additional = prod.getRHS();
      this.union(additional);
   }

   public void union(Vector prodList) {
      for(int i = 0; i < prodList.size(); ++i) {
         Vector toAdd = (Vector)prodList.elementAt(i);

         for(int j = 0; j < this.rhs.size(); ++j) {
            Vector current = (Vector)this.rhs.elementAt(i);
            if (isSameProduction(toAdd, current)) {
               break;
            }

            if (j == this.rhs.size() - 1) {
               this.rhs.addElement(toAdd);
            }
         }
      }

   }

   public void add(Production prod) {
      Vector additional = prod.getRHS();

      for(int i = 0; i < additional.size(); ++i) {
         this.rhs.addElement(additional.elementAt(i));
      }

   }

   public void addToRHS(Vector rhsPart) {
      this.rhs.addElement(rhsPart);
   }

   private void assertSameLHS(Production prod, String function) {
      if (!prod.getLHS().equals(this.lhs)) {
         System.err.println(HEADER + "nonterminals do not match in Production." + function + "(): current is " + this.lhs + ", given: " + prod.getLHS());
         System.exit(1);
      }

   }

   public void unparse(CodeWriter cw) {
      cw.begin(0);
      cw.write(this.lhs.toString() + " ::=");
      cw.allowBreak(3);

      for(int i = 0; i < this.rhs.size(); ++i) {
         Vector rhs_part = (Vector)this.rhs.elementAt(i);

         for(int j = 0; j < rhs_part.size(); ++j) {
            cw.write(" ");
            ((GrammarPart)rhs_part.elementAt(j)).unparse(cw);
         }

         if (i < this.rhs.size() - 1) {
            cw.allowBreak(0);
            cw.write(" | ");
         }
      }

      cw.write(";");
      cw.newline();
      cw.newline();
      cw.end();
   }

   public String toString() {
      String result = this.lhs.toString();
      result = result + " ::=";

      for(int i = 0; i < this.rhs.size(); ++i) {
         Vector rhs_part = (Vector)this.rhs.elementAt(i);

         for(int j = 0; j < rhs_part.size(); ++j) {
            result = result + " " + rhs_part.elementAt(j).toString();
         }

         if (i < this.rhs.size() - 1) {
            result = result + " | ";
         }
      }

      return result + ";";
   }
}
