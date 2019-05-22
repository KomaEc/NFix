package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.ResourceCollection;

public abstract class BaseResourceCollectionWrapper extends DataType implements ResourceCollection, Cloneable {
   private static final String ONE_NESTED_MESSAGE = " expects exactly one nested resource collection.";
   private ResourceCollection rc;
   private Collection coll = null;
   private boolean cache = true;

   public synchronized void setCache(boolean b) {
      this.cache = b;
   }

   public synchronized boolean isCache() {
      return this.cache;
   }

   public synchronized void add(ResourceCollection c) throws BuildException {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (c != null) {
         if (this.rc != null) {
            throw this.oneNested();
         } else {
            this.rc = c;
            this.setChecked(false);
         }
      }
   }

   public final synchronized Iterator iterator() {
      if (this.isReference()) {
         return ((BaseResourceCollectionWrapper)this.getCheckedRef()).iterator();
      } else {
         this.dieOnCircularReference();
         return new FailFast(this, this.cacheCollection().iterator());
      }
   }

   public synchronized int size() {
      if (this.isReference()) {
         return ((BaseResourceCollectionWrapper)this.getCheckedRef()).size();
      } else {
         this.dieOnCircularReference();
         return this.cacheCollection().size();
      }
   }

   public synchronized boolean isFilesystemOnly() {
      if (this.isReference()) {
         return ((BaseResourceCollectionContainer)this.getCheckedRef()).isFilesystemOnly();
      } else {
         this.dieOnCircularReference();
         if (this.rc != null && !this.rc.isFilesystemOnly()) {
            Iterator i = this.cacheCollection().iterator();

            do {
               if (!i.hasNext()) {
                  return true;
               }
            } while(i.next() instanceof FileResource);

            return false;
         } else {
            return true;
         }
      }
   }

   protected synchronized void dieOnCircularReference(Stack stk, Project p) throws BuildException {
      if (!this.isChecked()) {
         if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
         } else {
            if (this.rc instanceof DataType) {
               stk.push(this.rc);
               invokeCircularReferenceCheck((DataType)this.rc, stk, p);
               stk.pop();
            }

            this.setChecked(true);
         }

      }
   }

   protected final synchronized ResourceCollection getResourceCollection() {
      this.dieOnCircularReference();
      if (this.rc == null) {
         throw this.oneNested();
      } else {
         return this.rc;
      }
   }

   protected abstract Collection getCollection();

   public synchronized String toString() {
      if (this.isReference()) {
         return this.getCheckedRef().toString();
      } else if (this.cacheCollection().size() == 0) {
         return "";
      } else {
         StringBuffer sb = new StringBuffer();

         for(Iterator i = this.coll.iterator(); i.hasNext(); sb.append(i.next())) {
            if (sb.length() > 0) {
               sb.append(File.pathSeparatorChar);
            }
         }

         return sb.toString();
      }
   }

   private synchronized Collection cacheCollection() {
      if (this.coll == null || !this.isCache()) {
         this.coll = this.getCollection();
      }

      return this.coll;
   }

   private BuildException oneNested() {
      return new BuildException(super.toString() + " expects exactly one nested resource collection.");
   }
}
