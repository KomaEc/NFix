package groovy.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class Sequence extends ArrayList implements GroovyObject {
   private MetaClass metaClass;
   private Class type;
   private int hashCode;

   public Sequence() {
      this((Class)null);
   }

   public Sequence(Class type) {
      this.metaClass = InvokerHelper.getMetaClass(this.getClass());
      this.type = type;
   }

   public Sequence(Class type, List content) {
      super(content.size());
      this.metaClass = InvokerHelper.getMetaClass(this.getClass());
      this.type = type;
      this.addAll(content);
   }

   public void set(Collection collection) {
      this.checkCollectionType(collection);
      this.clear();
      this.addAll(collection);
   }

   public boolean equals(Object that) {
      return that instanceof Sequence ? this.equals((Sequence)that) : false;
   }

   public boolean equals(Sequence that) {
      if (this.size() == that.size()) {
         for(int i = 0; i < this.size(); ++i) {
            if (!DefaultTypeTransformation.compareEqual(this.get(i), that.get(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         for(int i = 0; i < this.size(); ++i) {
            Object value = this.get(i);
            int hash = value != null ? value.hashCode() : '몾';
            this.hashCode ^= hash;
         }

         if (this.hashCode == 0) {
            this.hashCode = 47806;
         }
      }

      return this.hashCode;
   }

   public int minimumSize() {
      return 0;
   }

   public Class type() {
      return this.type;
   }

   public void add(int index, Object element) {
      this.checkType(element);
      this.hashCode = 0;
      super.add(index, element);
   }

   public boolean add(Object element) {
      this.checkType(element);
      this.hashCode = 0;
      return super.add(element);
   }

   public boolean addAll(Collection c) {
      this.checkCollectionType(c);
      this.hashCode = 0;
      return super.addAll(c);
   }

   public boolean addAll(int index, Collection c) {
      this.checkCollectionType(c);
      this.hashCode = 0;
      return super.addAll(index, c);
   }

   public void clear() {
      this.hashCode = 0;
      super.clear();
   }

   public Object remove(int index) {
      this.hashCode = 0;
      return super.remove(index);
   }

   protected void removeRange(int fromIndex, int toIndex) {
      this.hashCode = 0;
      super.removeRange(fromIndex, toIndex);
   }

   public Object set(int index, Object element) {
      this.hashCode = 0;
      return super.set(index, element);
   }

   public Object invokeMethod(String name, Object args) {
      try {
         return this.getMetaClass().invokeMethod(this, name, args);
      } catch (MissingMethodException var8) {
         List answer = new ArrayList(this.size());
         Iterator iter = this.iterator();

         while(iter.hasNext()) {
            Object element = iter.next();
            Object value = InvokerHelper.invokeMethod(element, name, args);
            answer.add(value);
         }

         return answer;
      }
   }

   public Object getProperty(String property) {
      return this.getMetaClass().getProperty(this, property);
   }

   public void setProperty(String property, Object newValue) {
      this.getMetaClass().setProperty(this, property, newValue);
   }

   public MetaClass getMetaClass() {
      return this.metaClass;
   }

   public void setMetaClass(MetaClass metaClass) {
      this.metaClass = metaClass;
   }

   protected void checkCollectionType(Collection c) {
      if (this.type != null) {
         Iterator iter = c.iterator();

         while(iter.hasNext()) {
            Object element = iter.next();
            this.checkType(element);
         }
      }

   }

   protected void checkType(Object object) {
      if (object == null) {
         throw new NullPointerException("Sequences cannot contain null, use a List instead");
      } else if (this.type != null && !this.type.isInstance(object)) {
         throw new IllegalArgumentException("Invalid type of argument for sequence of type: " + this.type.getName() + " cannot add object: " + object);
      }
   }
}
