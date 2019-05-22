package groovy.inspect.swingui;

import java.util.List;

public interface AstBrowserNodeMaker<T> {
   T makeNode(Object var1);

   T makeNodeWithProperties(Object var1, List<List<String>> var2);
}
