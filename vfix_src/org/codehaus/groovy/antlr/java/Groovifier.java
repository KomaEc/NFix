package org.codehaus.groovy.antlr.java;

import org.codehaus.groovy.antlr.GroovySourceAST;
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;
import org.codehaus.groovy.antlr.treewalker.VisitorAdapter;

public class Groovifier extends VisitorAdapter implements GroovyTokenTypes {
   private String[] tokenNames;
   private String currentClassName;
   private boolean cleanRedundantPublic;

   public Groovifier(String[] tokenNames) {
      this(tokenNames, true);
   }

   public Groovifier(String[] tokenNames, boolean cleanRedundantPublic) {
      this.currentClassName = "";
      this.tokenNames = tokenNames;
      this.cleanRedundantPublic = cleanRedundantPublic;
   }

   public void visitClassDef(GroovySourceAST t, int visit) {
      if (visit == 1) {
         this.currentClassName = t.childOfType(84).getText();
      }

   }

   public void visitDefault(GroovySourceAST t, int visit) {
      if (visit == 1) {
         if (t.getType() == 112 && this.cleanRedundantPublic) {
            t.setType(27);
         }

         if (t.getType() == 8) {
            String methodName = t.childOfType(84).getText();
            if (methodName != null && methodName.length() > 0 && methodName.equals(this.currentClassName)) {
               t.setType(45);
            }
         }
      }

   }
}
