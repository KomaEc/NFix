package polyglot.ast;

import java.util.List;

public interface SourceCollection extends Node {
   List sources();

   SourceCollection sources(List var1);
}
