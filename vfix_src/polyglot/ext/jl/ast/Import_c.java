package polyglot.ext.jl.ast;

import polyglot.ast.Import;
import polyglot.ast.Node;
import polyglot.main.Options;
import polyglot.types.ImportTable;
import polyglot.types.Named;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.util.StringUtil;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class Import_c extends Node_c implements Import {
   protected Import.Kind kind;
   protected String name;

   public Import_c(Position pos, Import.Kind kind, String name) {
      super(pos);
      this.name = name;
      this.kind = kind;
   }

   public String name() {
      return this.name;
   }

   public Import name(String name) {
      Import_c n = (Import_c)this.copy();
      n.name = name;
      return n;
   }

   public Import.Kind kind() {
      return this.kind;
   }

   public Import kind(Import.Kind kind) {
      Import_c n = (Import_c)this.copy();
      n.kind = kind;
      return n;
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      ImportTable it = tb.importTable();
      if (this.kind == Import.CLASS) {
         it.addClassImport(this.name);
      } else if (this.kind == Import.PACKAGE) {
         it.addPackageImport(this.name);
      }

      return this;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      if (this.kind == Import.PACKAGE && tc.typeSystem().packageExists(this.name)) {
         return this;
      } else {
         String pkgName = StringUtil.getFirstComponent(this.name);
         if (!tc.typeSystem().packageExists(pkgName)) {
            throw new SemanticException("Package \"" + pkgName + "\" not found.", this.position());
         } else {
            Named nt = tc.typeSystem().forName(this.name);
            if (nt instanceof Type) {
               Type t = (Type)nt;
               if (t.isClass()) {
                  tc.typeSystem().classAccessibleFromPackage(t.toClass(), tc.context().package_());
               }
            }

            return this;
         }
      }
   }

   public String toString() {
      return "import " + this.name + (this.kind == Import.PACKAGE ? ".*" : "");
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (!Options.global.fully_qualified_names) {
         w.write("import ");
         w.write(this.name);
         if (this.kind == Import.PACKAGE) {
            w.write(".*");
         }

         w.write(";");
         w.newline(0);
      }

   }
}
