package polyglot.ast;

import java.util.List;
import polyglot.frontend.Source;
import polyglot.types.ImportTable;

public interface SourceFile extends Node {
   PackageNode package_();

   SourceFile package_(PackageNode var1);

   List imports();

   SourceFile imports(List var1);

   List decls();

   SourceFile decls(List var1);

   ImportTable importTable();

   SourceFile importTable(ImportTable var1);

   Source source();

   SourceFile source(Source var1);
}
