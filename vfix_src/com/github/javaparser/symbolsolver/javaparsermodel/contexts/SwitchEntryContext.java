package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.SymbolDeclarator;
import java.util.Iterator;
import java.util.List;

public class SwitchEntryContext extends AbstractJavaParserContext<SwitchEntryStmt> {
   public SwitchEntryContext(SwitchEntryStmt wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      SwitchStmt switchStmt = (SwitchStmt)Navigator.requireParentNode(this.wrappedNode);
      ResolvedType type = JavaParserFacade.get(typeSolver).getType(switchStmt.getSelector());
      if (type.isReferenceType() && type.asReferenceType().getTypeDeclaration().isEnum()) {
         if (!(type instanceof ReferenceTypeImpl)) {
            throw new UnsupportedOperationException();
         }

         ReferenceTypeImpl typeUsageOfTypeDeclaration = (ReferenceTypeImpl)type;
         if (typeUsageOfTypeDeclaration.getTypeDeclaration().asEnum().hasEnumConstant(name)) {
            return SymbolReference.solved(typeUsageOfTypeDeclaration.getTypeDeclaration().asEnum().getEnumConstant(name));
         }

         if (typeUsageOfTypeDeclaration.getTypeDeclaration().hasField(name)) {
            return SymbolReference.solved(typeUsageOfTypeDeclaration.getTypeDeclaration().getField(name));
         }
      }

      Iterator var11 = switchStmt.getEntries().iterator();

      while(var11.hasNext()) {
         SwitchEntryStmt seStmt = (SwitchEntryStmt)var11.next();
         Iterator var7 = seStmt.getStatements().iterator();

         while(var7.hasNext()) {
            Statement stmt = (Statement)var7.next();
            SymbolDeclarator symbolDeclarator = JavaParserFactory.getSymbolDeclarator(stmt, typeSolver);
            SymbolReference<? extends ResolvedValueDeclaration> symbolReference = solveWith(symbolDeclarator, name);
            if (symbolReference.isSolved()) {
               return symbolReference;
            }
         }

         if (seStmt == this.wrappedNode) {
            break;
         }
      }

      return this.getParent().solveSymbol(name, typeSolver);
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.getParent().solveMethod(name, argumentsTypes, false, typeSolver);
   }
}
