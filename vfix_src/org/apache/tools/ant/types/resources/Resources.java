package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.ResourceCollection;

public class Resources extends DataType implements ResourceCollection {
   public static final ResourceCollection NONE = new ResourceCollection() {
      public boolean isFilesystemOnly() {
         return true;
      }

      public Iterator iterator() {
         return Resources.EMPTY_ITERATOR;
      }

      public int size() {
         return 0;
      }
   };
   public static final Iterator EMPTY_ITERATOR = new Iterator() {
      public Object next() {
         throw new NoSuchElementException();
      }

      public boolean hasNext() {
         return false;
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   };
   private Vector rc;
   private Collection coll;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$ResourceCollection;

   public synchronized void add(ResourceCollection c) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (c != null) {
         if (this.rc == null) {
            this.rc = new Vector();
         }

         this.rc.add(c);
         FailFast.invalidate(this);
         this.coll = null;
         this.setChecked(false);
      }
   }

   public synchronized Iterator iterator() {
      if (this.isReference()) {
         return this.getRef().iterator();
      } else {
         this.validate();
         return new FailFast(this, this.coll.iterator());
      }
   }

   public synchronized int size() {
      if (this.isReference()) {
         return this.getRef().size();
      } else {
         this.validate();
         return this.coll.size();
      }
   }

   public boolean isFilesystemOnly() {
      if (this.isReference()) {
         return this.getRef().isFilesystemOnly();
      } else {
         this.validate();
         Iterator i = this.getNested().iterator();

         do {
            if (!i.hasNext()) {
               return true;
            }
         } while(((ResourceCollection)i.next()).isFilesystemOnly());

         return false;
      }
   }

   public synchronized String toString() {
      if (this.isReference()) {
         return this.getCheckedRef().toString();
      } else if (this.coll != null && !this.coll.isEmpty()) {
         StringBuffer sb = new StringBuffer();

         for(Iterator i = this.coll.iterator(); i.hasNext(); sb.append(i.next())) {
            if (sb.length() > 0) {
               sb.append(File.pathSeparatorChar);
            }
         }

         return sb.toString();
      } else {
         return "";
      }
   }

   protected void dieOnCircularReference(Stack stk, Project p) throws BuildException {
      if (!this.isChecked()) {
         if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
         } else {
            Iterator i = this.getNested().iterator();

            while(i.hasNext()) {
               Object o = i.next();
               if (o instanceof DataType) {
                  invokeCircularReferenceCheck((DataType)o, stk, p);
               }
            }

            this.setChecked(true);
         }

      }
   }

   private ResourceCollection getRef() {
      return (ResourceCollection)this.getCheckedRef(class$org$apache$tools$ant$types$ResourceCollection == null ? (class$org$apache$tools$ant$types$ResourceCollection = class$("org.apache.tools.ant.types.ResourceCollection")) : class$org$apache$tools$ant$types$ResourceCollection, "ResourceCollection");
   }

   private synchronized void validate() {
      this.dieOnCircularReference();
      this.coll = (Collection)(this.coll == null ? new Resources.MyCollection() : this.coll);
   }

   private synchronized List getNested() {
      return (List)(this.rc == null ? Collections.EMPTY_LIST : this.rc);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private class MyCollection extends AbstractCollection {
      private int size = 0;

      MyCollection() {
         for(Iterator rci = Resources.this.getNested().iterator(); rci.hasNext(); this.size += ((ResourceCollection)rci.next()).size()) {
         }

      }

      public int size() {
         return this.size;
      }

      public Iterator iterator() {
         return new Resources.MyCollection.MyIterator();
      }

      private class MyIterator implements Iterator {
         private Iterator rci;
         private Iterator ri;

         private MyIterator() {
            this.rci = Resources.this.getNested().iterator();
            this.ri = null;
         }

         public boolean hasNext() {
            boolean result;
            for(result = this.ri != null && this.ri.hasNext(); !result && this.rci.hasNext(); result = this.ri.hasNext()) {
               this.ri = ((ResourceCollection)this.rci.next()).iterator();
            }

            return result;
         }

         public Object next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return this.ri.next();
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         // $FF: synthetic method
         MyIterator(Object x1) {
            this();
         }
      }
   }
}
