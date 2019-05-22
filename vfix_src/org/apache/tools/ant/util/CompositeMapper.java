package org.apache.tools.ant.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class CompositeMapper extends ContainerMapper {
   public String[] mapFileName(String sourceFileName) {
      HashSet results = new HashSet();
      FileNameMapper mapper = null;
      Iterator mIter = this.getMappers().iterator();

      while(mIter.hasNext()) {
         mapper = (FileNameMapper)((FileNameMapper)mIter.next());
         if (mapper != null) {
            String[] mapped = mapper.mapFileName(sourceFileName);
            if (mapped != null) {
               results.addAll(Arrays.asList(mapped));
            }
         }
      }

      return results.size() == 0 ? null : (String[])((String[])results.toArray(new String[results.size()]));
   }
}
