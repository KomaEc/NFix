package org.apache.tools.ant.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.types.Mapper;

public abstract class ContainerMapper implements FileNameMapper {
   private List mappers = new ArrayList();

   public void addConfiguredMapper(Mapper mapper) {
      this.add(mapper.getImplementation());
   }

   public void addConfigured(FileNameMapper fileNameMapper) {
      this.add(fileNameMapper);
   }

   public synchronized void add(FileNameMapper fileNameMapper) {
      if (this != fileNameMapper && (!(fileNameMapper instanceof ContainerMapper) || !((ContainerMapper)fileNameMapper).contains(this))) {
         this.mappers.add(fileNameMapper);
      } else {
         throw new IllegalArgumentException("Circular mapper containment condition detected");
      }
   }

   protected synchronized boolean contains(FileNameMapper fileNameMapper) {
      boolean foundit = false;

      FileNameMapper next;
      for(Iterator iter = this.mappers.iterator(); iter.hasNext() && !foundit; foundit |= next == fileNameMapper || next instanceof ContainerMapper && ((ContainerMapper)next).contains(fileNameMapper)) {
         next = (FileNameMapper)((FileNameMapper)iter.next());
      }

      return foundit;
   }

   public synchronized List getMappers() {
      return Collections.unmodifiableList(this.mappers);
   }

   public void setFrom(String ignore) {
   }

   public void setTo(String ignore) {
   }
}
