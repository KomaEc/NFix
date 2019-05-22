package org.apache.tools.ant.types;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.PropertyResource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.regexp.RegexpMatcher;
import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;

public class PropertySet extends DataType implements ResourceCollection {
   private boolean dynamic = true;
   private boolean negate = false;
   private Set cachedNames;
   private Vector ptyRefs = new Vector();
   private Vector setRefs = new Vector();
   private Mapper mapper;
   private boolean noAttributeSet = true;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$PropertySet;

   public void appendName(String name) {
      PropertySet.PropertyRef r = new PropertySet.PropertyRef();
      r.setName(name);
      this.addPropertyref(r);
   }

   public void appendRegex(String regex) {
      PropertySet.PropertyRef r = new PropertySet.PropertyRef();
      r.setRegex(regex);
      this.addPropertyref(r);
   }

   public void appendPrefix(String prefix) {
      PropertySet.PropertyRef r = new PropertySet.PropertyRef();
      r.setPrefix(prefix);
      this.addPropertyref(r);
   }

   public void appendBuiltin(PropertySet.BuiltinPropertySetName b) {
      PropertySet.PropertyRef r = new PropertySet.PropertyRef();
      r.setBuiltin(b);
      this.addPropertyref(r);
   }

   public void setMapper(String type, String from, String to) {
      Mapper m = this.createMapper();
      Mapper.MapperType mapperType = new Mapper.MapperType();
      mapperType.setValue(type);
      m.setType(mapperType);
      m.setFrom(from);
      m.setTo(to);
   }

   public void addPropertyref(PropertySet.PropertyRef ref) {
      this.assertNotReference();
      this.ptyRefs.addElement(ref);
   }

   public void addPropertyset(PropertySet ref) {
      this.assertNotReference();
      this.setRefs.addElement(ref);
   }

   public Mapper createMapper() {
      this.assertNotReference();
      if (this.mapper != null) {
         throw new BuildException("Too many <mapper>s!");
      } else {
         this.mapper = new Mapper(this.getProject());
         return this.mapper;
      }
   }

   public void add(FileNameMapper fileNameMapper) {
      this.createMapper().add(fileNameMapper);
   }

   public void setDynamic(boolean dynamic) {
      this.assertNotReference();
      this.dynamic = dynamic;
   }

   public void setNegate(boolean negate) {
      this.assertNotReference();
      this.negate = negate;
   }

   public boolean getDynamic() {
      return this.isReference() ? this.getRef().dynamic : this.dynamic;
   }

   public Mapper getMapper() {
      return this.isReference() ? this.getRef().mapper : this.mapper;
   }

   private Hashtable getAllSystemProperties() {
      Hashtable ret = new Hashtable();
      Enumeration e = System.getProperties().propertyNames();

      while(e.hasMoreElements()) {
         String name = (String)e.nextElement();
         ret.put(name, System.getProperties().getProperty(name));
      }

      return ret;
   }

   public Properties getProperties() {
      if (this.isReference()) {
         return this.getRef().getProperties();
      } else {
         Set names = null;
         Project prj = this.getProject();
         Hashtable props = prj == null ? this.getAllSystemProperties() : prj.getProperties();
         Enumeration e = this.setRefs.elements();

         PropertySet set;
         while(e.hasMoreElements()) {
            set = (PropertySet)e.nextElement();
            props.putAll(set.getProperties());
         }

         if (!this.getDynamic() && this.cachedNames != null) {
            names = this.cachedNames;
         } else {
            names = new HashSet();
            this.addPropertyNames((Set)names, props);
            e = this.setRefs.elements();

            while(e.hasMoreElements()) {
               set = (PropertySet)e.nextElement();
               ((Set)names).addAll(set.getProperties().keySet());
            }

            if (this.negate) {
               HashSet complement = new HashSet(props.keySet());
               complement.removeAll((Collection)names);
               names = complement;
            }

            if (!this.getDynamic()) {
               this.cachedNames = (Set)names;
            }
         }

         FileNameMapper m = null;
         Mapper myMapper = this.getMapper();
         if (myMapper != null) {
            m = myMapper.getImplementation();
         }

         Properties properties = new Properties();
         Iterator iter = ((Set)names).iterator();

         while(iter.hasNext()) {
            String name = (String)iter.next();
            String value = (String)props.get(name);
            if (value != null) {
               if (m != null) {
                  String[] newname = m.mapFileName(name);
                  if (newname != null) {
                     name = newname[0];
                  }
               }

               properties.setProperty(name, value);
            }
         }

         return properties;
      }
   }

   private void addPropertyNames(Set names, Hashtable properties) {
      Enumeration e = this.ptyRefs.elements();

      while(true) {
         while(e.hasMoreElements()) {
            PropertySet.PropertyRef r = (PropertySet.PropertyRef)e.nextElement();
            if (r.name != null) {
               if (properties.get(r.name) != null) {
                  names.add(r.name);
               }
            } else if (r.prefix != null) {
               Enumeration p = properties.keys();

               while(p.hasMoreElements()) {
                  String name = (String)p.nextElement();
                  if (name.startsWith(r.prefix)) {
                     names.add(name);
                  }
               }
            } else if (r.regex != null) {
               RegexpMatcherFactory matchMaker = new RegexpMatcherFactory();
               RegexpMatcher matcher = matchMaker.newRegexpMatcher();
               matcher.setPattern(r.regex);
               Enumeration p = properties.keys();

               while(p.hasMoreElements()) {
                  String name = (String)p.nextElement();
                  if (matcher.matches(name)) {
                     names.add(name);
                  }
               }
            } else {
               if (r.builtin == null) {
                  throw new BuildException("Impossible: Invalid PropertyRef!");
               }

               if (r.builtin.equals("all")) {
                  names.addAll(properties.keySet());
               } else if (r.builtin.equals("system")) {
                  names.addAll(System.getProperties().keySet());
               } else {
                  if (!r.builtin.equals("commandline")) {
                     throw new BuildException("Impossible: Invalid builtin attribute!");
                  }

                  names.addAll(this.getProject().getUserProperties().keySet());
               }
            }
         }

         return;
      }
   }

   protected PropertySet getRef() {
      return (PropertySet)this.getCheckedRef(class$org$apache$tools$ant$types$PropertySet == null ? (class$org$apache$tools$ant$types$PropertySet = class$("org.apache.tools.ant.types.PropertySet")) : class$org$apache$tools$ant$types$PropertySet, "propertyset");
   }

   public final void setRefid(Reference r) {
      if (!this.noAttributeSet) {
         throw this.tooManyAttributes();
      } else {
         super.setRefid(r);
      }
   }

   protected final void assertNotReference() {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.noAttributeSet = false;
      }
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      TreeMap sorted = new TreeMap(this.getProperties());
      Iterator i = sorted.entrySet().iterator();

      while(i.hasNext()) {
         Entry e = (Entry)i.next();
         if (b.length() != 0) {
            b.append(", ");
         }

         b.append(e.getKey().toString());
         b.append("=");
         b.append(e.getValue().toString());
      }

      return b.toString();
   }

   public Iterator iterator() {
      final Enumeration e = this.getProperties().propertyNames();
      return new Iterator() {
         public boolean hasNext() {
            return e.hasMoreElements();
         }

         public Object next() {
            return new PropertyResource(PropertySet.this.getProject(), (String)e.nextElement());
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int size() {
      return this.isReference() ? this.getRef().size() : this.getProperties().size();
   }

   public boolean isFilesystemOnly() {
      return this.isReference() && this.getRef().isFilesystemOnly();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class BuiltinPropertySetName extends EnumeratedAttribute {
      static final String ALL = "all";
      static final String SYSTEM = "system";
      static final String COMMANDLINE = "commandline";

      public String[] getValues() {
         return new String[]{"all", "system", "commandline"};
      }
   }

   public static class PropertyRef {
      private int count;
      private String name;
      private String regex;
      private String prefix;
      private String builtin;

      public void setName(String name) {
         this.assertValid("name", name);
         this.name = name;
      }

      public void setRegex(String regex) {
         this.assertValid("regex", regex);
         this.regex = regex;
      }

      public void setPrefix(String prefix) {
         this.assertValid("prefix", prefix);
         this.prefix = prefix;
      }

      public void setBuiltin(PropertySet.BuiltinPropertySetName b) {
         String pBuiltIn = b.getValue();
         this.assertValid("builtin", pBuiltIn);
         this.builtin = pBuiltIn;
      }

      private void assertValid(String attr, String value) {
         if (value != null && value.length() >= 1) {
            if (++this.count != 1) {
               throw new BuildException("Attributes name, regex, and prefix are mutually exclusive");
            }
         } else {
            throw new BuildException("Invalid attribute: " + attr);
         }
      }

      public String toString() {
         return "name=" + this.name + ", regex=" + this.regex + ", prefix=" + this.prefix + ", builtin=" + this.builtin;
      }
   }
}
