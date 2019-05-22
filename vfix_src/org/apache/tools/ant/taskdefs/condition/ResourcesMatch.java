package org.apache.tools.ant.taskdefs.condition;

import java.io.IOException;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.ResourceUtils;

public class ResourcesMatch implements Condition {
   private Union resources = null;
   private boolean asText = false;

   public void setAsText(boolean asText) {
      this.asText = asText;
   }

   public void add(ResourceCollection rc) {
      if (rc != null) {
         this.resources = this.resources == null ? new Union() : this.resources;
         this.resources.add(rc);
      }
   }

   public boolean eval() throws BuildException {
      if (this.resources == null) {
         throw new BuildException("You must specify one or more nested resource collections");
      } else {
         if (this.resources.size() > 1) {
            Iterator i = this.resources.iterator();
            Resource r1 = (Resource)i.next();

            for(Resource r2 = null; i.hasNext(); r1 = r2) {
               r2 = (Resource)i.next();

               try {
                  if (!ResourceUtils.contentEquals(r1, r2, this.asText)) {
                     return false;
                  }
               } catch (IOException var5) {
                  throw new BuildException("when comparing resources " + r1.toString() + " and " + r2.toString(), var5);
               }
            }
         }

         return true;
      }
   }
}
