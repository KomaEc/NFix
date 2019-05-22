package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileNameMapper;

public class PathConvert extends Task {
   private static boolean onWindows = Os.isFamily("dos");
   private Union path = null;
   private Reference refid = null;
   private String targetOS = null;
   private boolean targetWindows = false;
   private boolean setonempty = true;
   private String property = null;
   private Vector prefixMap = new Vector();
   private String pathSep = null;
   private String dirSep = null;
   private Mapper mapper = null;

   public Path createPath() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         Path result = new Path(this.getProject());
         this.add((ResourceCollection)result);
         return result;
      }
   }

   public void add(ResourceCollection rc) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.getPath().add(rc);
      }
   }

   private synchronized Union getPath() {
      if (this.path == null) {
         this.path = new Union();
         this.path.setProject(this.getProject());
      }

      return this.path;
   }

   public PathConvert.MapEntry createMap() {
      PathConvert.MapEntry entry = new PathConvert.MapEntry();
      this.prefixMap.addElement(entry);
      return entry;
   }

   /** @deprecated */
   public void setTargetos(String target) {
      PathConvert.TargetOs to = new PathConvert.TargetOs();
      to.setValue(target);
      this.setTargetos(to);
   }

   public void setTargetos(PathConvert.TargetOs target) {
      this.targetOS = target.getValue();
      this.targetWindows = !this.targetOS.equals("unix") && !this.targetOS.equals("tandem");
   }

   public void setSetonempty(boolean setonempty) {
      this.setonempty = setonempty;
   }

   public void setProperty(String p) {
      this.property = p;
   }

   public void setRefid(Reference r) {
      if (this.path != null) {
         throw this.noChildrenAllowed();
      } else {
         this.refid = r;
      }
   }

   public void setPathSep(String sep) {
      this.pathSep = sep;
   }

   public void setDirSep(String sep) {
      this.dirSep = sep;
   }

   public boolean isReference() {
      return this.refid != null;
   }

   public void execute() throws BuildException {
      Union savedPath = this.path;
      String savedPathSep = this.pathSep;
      String savedDirSep = this.dirSep;

      try {
         if (this.isReference()) {
            Object o = this.refid.getReferencedObject(this.getProject());
            if (!(o instanceof ResourceCollection)) {
               throw new BuildException("refid '" + this.refid.getRefId() + "' does not refer to a resource collection.");
            }

            this.getPath().add((ResourceCollection)o);
         }

         this.validateSetup();
         String fromDirSep = onWindows ? "\\" : "/";
         StringBuffer rslt = new StringBuffer();
         String[] elems = this.path.list();
         if (this.mapper != null) {
            FileNameMapper impl = this.mapper.getImplementation();
            List ret = new ArrayList();
            int i = 0;

            while(true) {
               if (i >= elems.length) {
                  elems = (String[])((String[])ret.toArray(new String[ret.size()]));
                  break;
               }

               String[] mapped = impl.mapFileName(elems[i]);

               for(int m = 0; mapped != null && m < mapped.length; ++m) {
                  ret.add(mapped[m]);
               }

               ++i;
            }
         }

         for(int i = 0; i < elems.length; ++i) {
            String elem = this.mapElement(elems[i]);
            if (i != 0) {
               rslt.append(this.pathSep);
            }

            StringTokenizer stDirectory = new StringTokenizer(elem, fromDirSep, true);

            while(stDirectory.hasMoreTokens()) {
               String token = stDirectory.nextToken();
               rslt.append(fromDirSep.equals(token) ? this.dirSep : token);
            }
         }

         if (this.setonempty || rslt.length() > 0) {
            String value = rslt.toString();
            if (this.property == null) {
               this.log(value);
            } else {
               this.log("Set property " + this.property + " = " + value, 3);
               this.getProject().setNewProperty(this.property, value);
            }
         }
      } finally {
         this.path = savedPath;
         this.dirSep = savedDirSep;
         this.pathSep = savedPathSep;
      }

   }

   private String mapElement(String elem) {
      int size = this.prefixMap.size();
      if (size != 0) {
         for(int i = 0; i < size; ++i) {
            PathConvert.MapEntry entry = (PathConvert.MapEntry)this.prefixMap.elementAt(i);
            String newElem = entry.apply(elem);
            if (newElem != elem) {
               elem = newElem;
               break;
            }
         }
      }

      return elem;
   }

   public void addMapper(Mapper mapper) {
      if (this.mapper != null) {
         throw new BuildException("Cannot define more than one mapper");
      } else {
         this.mapper = mapper;
      }
   }

   public void add(FileNameMapper fileNameMapper) {
      Mapper m = new Mapper(this.getProject());
      m.add(fileNameMapper);
      this.addMapper(m);
   }

   private void validateSetup() throws BuildException {
      if (this.path == null) {
         throw new BuildException("You must specify a path to convert");
      } else {
         String dsep = File.separator;
         String psep = File.pathSeparator;
         if (this.targetOS != null) {
            psep = this.targetWindows ? ";" : ":";
            dsep = this.targetWindows ? "\\" : "/";
         }

         if (this.pathSep != null) {
            psep = this.pathSep;
         }

         if (this.dirSep != null) {
            dsep = this.dirSep;
         }

         this.pathSep = psep;
         this.dirSep = dsep;
      }
   }

   private BuildException noChildrenAllowed() {
      return new BuildException("You must not specify nested elements when using the refid attribute.");
   }

   public static class TargetOs extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"windows", "unix", "netware", "os/2", "tandem"};
      }
   }

   public class MapEntry {
      private String from = null;
      private String to = null;

      public void setFrom(String from) {
         this.from = from;
      }

      public void setTo(String to) {
         this.to = to;
      }

      public String apply(String elem) {
         if (this.from != null && this.to != null) {
            String cmpElem = PathConvert.onWindows ? elem.toLowerCase().replace('\\', '/') : elem;
            String cmpFrom = PathConvert.onWindows ? this.from.toLowerCase().replace('\\', '/') : this.from;
            return cmpElem.startsWith(cmpFrom) ? this.to + elem.substring(this.from.length()) : elem;
         } else {
            throw new BuildException("Both 'from' and 'to' must be set in a map entry");
         }
      }
   }
}
