package org.apache.tools.ant.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ChainedMapper extends ContainerMapper {
   public String[] mapFileName(String sourceFileName) {
      List inputs = new ArrayList();
      List results = new ArrayList();
      results.add(sourceFileName);
      FileNameMapper mapper = null;
      Iterator mIter = this.getMappers().iterator();

      while(true) {
         do {
            if (!mIter.hasNext()) {
               return results.size() == 0 ? null : (String[])((String[])results.toArray(new String[results.size()]));
            }

            mapper = (FileNameMapper)((FileNameMapper)mIter.next());
         } while(mapper == null);

         inputs.clear();
         inputs.addAll(results);
         results.clear();
         Iterator it = inputs.iterator();

         while(it.hasNext()) {
            String[] mapped = mapper.mapFileName((String)((String)it.next()));
            if (mapped != null) {
               results.addAll(Arrays.asList(mapped));
            }
         }
      }
   }
}
