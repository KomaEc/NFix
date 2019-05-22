package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.ResourceCollection;

public abstract class BaseResourceCollectionContainer extends DataType implements ResourceCollection, Cloneable {
   private List rc = new ArrayList();
   private Collection coll = null;
   private boolean cache = true;

   public synchronized void setCache(boolean b) {
      this.cache = b;
   }

   public synchronized boolean isCache() {
      return this.cache;
   }

   public synchronized void clear() throws BuildException {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.rc.clear();
         FailFast.invalidate(this);
         this.coll = null;
         this.setChecked(false);
      }
   }

   public synchronized void add(ResourceCollection c) throws BuildException {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (c != null) {
         this.rc.add(c);
         FailFast.invalidate(this);
         this.coll = null;
         this.setChecked(false);
      }
   }

   public synchronized void addAll(Collection c) throws BuildException {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         try {
            Iterator i = c.iterator();

            while(i.hasNext()) {
               this.add((ResourceCollection)i.next());
            }

         } catch (ClassCastException var3) {
            throw new BuildException(var3);
         }
      }
   }

   public final synchronized Iterator iterator() {
      if (this.isReference()) {
         return ((BaseResourceCollectionContainer)this.getCheckedRef()).iterator();
      } else {
         this.dieOnCircularReference();
         return new FailFast(this, this.cacheCollection().iterator());
      }
   }

   public synchronized int size() {
      if (this.isReference()) {
         return ((BaseResourceCollectionContainer)this.getCheckedRef()).size();
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
         boolean goEarly = true;

         Iterator i;
         for(i = this.rc.iterator(); goEarly && i.hasNext(); goEarly &= ((ResourceCollection)i.next()).isFilesystemOnly()) {
         }

         if (goEarly) {
            return true;
         } else {
            i = this.cacheCollection().iterator();

            do {
               if (!i.hasNext()) {
                  return true;
               }
            } while(i.next() instanceof FileResource);

            return false;
         }
      }
   }

   protected synchronized void dieOnCircularReference(Stack stk, Project p) throws BuildException {
      if (!this.isChecked()) {
         if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
         } else {
            Iterator i = this.rc.iterator();

            while(i.hasNext()) {
               Object o = i.next();
               if (o instanceof DataType) {
                  stk.push(o);
                  invokeCircularReferenceCheck((DataType)o, stk, p);
                  stk.pop();
               }
            }

            this.setChecked(true);
         }

      }
   }

   protected final synchronized List getResourceCollections() {
      this.dieOnCircularReference();
      return Collections.unmodifiableList(this.rc);
   }

   protected abstract Collection getCollection();

   public Object clone() {
      try {
         BaseResourceCollectionContainer c = (BaseResourceCollectionContainer)super.clone();
         c.rc = new ArrayList(this.rc);
         c.coll = null;
         return c;
      } catch (CloneNotSupportedException var2) {
         throw new BuildException(var2);
      }
   }

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
}
