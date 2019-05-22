package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import polyglot.ast.Import;
import polyglot.ast.Node;
import polyglot.ast.PackageNode;
import polyglot.ast.SourceFile;
import polyglot.ast.TopLevelDecl;
import polyglot.frontend.Source;
import polyglot.types.Context;
import polyglot.types.ImportTable;
import polyglot.types.Named;
import polyglot.types.Package;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class SourceFile_c extends Node_c implements SourceFile {
   protected PackageNode package_;
   protected List imports;
   protected List decls;
   protected ImportTable importTable;
   protected Source source;
   // $FF: synthetic field
   static Class class$polyglot$ast$Import;
   // $FF: synthetic field
   static Class class$polyglot$ast$TopLevelDecl;

   public SourceFile_c(Position pos, PackageNode package_, List imports, List decls) {
      super(pos);
      this.package_ = package_;
      this.imports = TypedList.copyAndCheck(imports, class$polyglot$ast$Import == null ? (class$polyglot$ast$Import = class$("polyglot.ast.Import")) : class$polyglot$ast$Import, true);
      this.decls = TypedList.copyAndCheck(decls, class$polyglot$ast$TopLevelDecl == null ? (class$polyglot$ast$TopLevelDecl = class$("polyglot.ast.TopLevelDecl")) : class$polyglot$ast$TopLevelDecl, true);
   }

   public Source source() {
      return this.source;
   }

   public SourceFile source(Source source) {
      SourceFile_c n = (SourceFile_c)this.copy();
      n.source = source;
      return n;
   }

   public PackageNode package_() {
      return this.package_;
   }

   public SourceFile package_(PackageNode package_) {
      SourceFile_c n = (SourceFile_c)this.copy();
      n.package_ = package_;
      return n;
   }

   public List imports() {
      return Collections.unmodifiableList(this.imports);
   }

   public SourceFile imports(List imports) {
      SourceFile_c n = (SourceFile_c)this.copy();
      n.imports = TypedList.copyAndCheck(imports, class$polyglot$ast$Import == null ? (class$polyglot$ast$Import = class$("polyglot.ast.Import")) : class$polyglot$ast$Import, true);
      return n;
   }

   public List decls() {
      return Collections.unmodifiableList(this.decls);
   }

   public SourceFile decls(List decls) {
      SourceFile_c n = (SourceFile_c)this.copy();
      n.decls = TypedList.copyAndCheck(decls, class$polyglot$ast$TopLevelDecl == null ? (class$polyglot$ast$TopLevelDecl = class$("polyglot.ast.TopLevelDecl")) : class$polyglot$ast$TopLevelDecl, true);
      return n;
   }

   public ImportTable importTable() {
      return this.importTable;
   }

   public SourceFile importTable(ImportTable importTable) {
      SourceFile_c n = (SourceFile_c)this.copy();
      n.importTable = importTable;
      return n;
   }

   protected SourceFile_c reconstruct(PackageNode package_, List imports, List decls) {
      if (package_ == this.package_ && CollectionUtil.equals(imports, this.imports) && CollectionUtil.equals(decls, this.decls)) {
         return this;
      } else {
         SourceFile_c n = (SourceFile_c)this.copy();
         n.package_ = package_;
         n.imports = TypedList.copyAndCheck(imports, class$polyglot$ast$Import == null ? (class$polyglot$ast$Import = class$("polyglot.ast.Import")) : class$polyglot$ast$Import, true);
         n.decls = TypedList.copyAndCheck(decls, class$polyglot$ast$TopLevelDecl == null ? (class$polyglot$ast$TopLevelDecl = class$("polyglot.ast.TopLevelDecl")) : class$polyglot$ast$TopLevelDecl, true);
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      PackageNode package_ = (PackageNode)this.visitChild(this.package_, v);
      List imports = this.visitList(this.imports, v);
      List decls = this.visitList(this.decls, v);
      return this.reconstruct(package_, imports, decls);
   }

   public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
      TypeSystem ts = tb.typeSystem();
      ImportTable it;
      if (this.package_ != null) {
         it = ts.importTable(this.source.name(), this.package_.package_());
      } else {
         it = ts.importTable(this.source.name(), (Package)null);
      }

      tb.setImportTable(it);
      return tb;
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      ImportTable it = tb.importTable();
      tb.setImportTable((ImportTable)null);
      return this.importTable(it);
   }

   public Context enterScope(Context c) {
      c = c.pushSource(this.importTable);
      c = c.pushBlock();
      Iterator i = this.decls.iterator();

      while(i.hasNext()) {
         TopLevelDecl d = (TopLevelDecl)i.next();
         Named n = d.declaration();
         c.addNamed(n);
      }

      return c;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      Set names = new HashSet();
      boolean hasPublic = false;
      Iterator i = this.decls.iterator();

      while(i.hasNext()) {
         TopLevelDecl d = (TopLevelDecl)i.next();
         String s = d.name();
         if (names.contains(s)) {
            throw new SemanticException("Duplicate declaration: \"" + s + "\".", d.position());
         }

         names.add(s);
         if (d.flags().isPublic()) {
            if (hasPublic) {
               throw new SemanticException("The source contains more than one public declaration.", d.position());
            }

            hasPublic = true;
         }
      }

      return this;
   }

   public String toString() {
      return "<<<< " + this.source + " >>>>";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("<<<< " + this.source + " >>>>");
      w.newline(0);
      if (this.package_ != null) {
         w.write("package ");
         this.print(this.package_, w, tr);
         w.write(";");
         w.newline(0);
         w.newline(0);
      }

      Iterator i = this.imports.iterator();

      while(i.hasNext()) {
         Import im = (Import)i.next();
         this.print(im, w, tr);
      }

      if (!this.imports.isEmpty()) {
         w.newline(0);
      }

      i = this.decls.iterator();

      while(i.hasNext()) {
         TopLevelDecl d = (TopLevelDecl)i.next();
         this.print(d, w, tr);
      }

   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
