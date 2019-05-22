package difflib;

import java.util.List;

public interface DiffAlgorithm {
   Patch diff(Object[] var1, Object[] var2);

   Patch diff(List<?> var1, List<?> var2);
}
