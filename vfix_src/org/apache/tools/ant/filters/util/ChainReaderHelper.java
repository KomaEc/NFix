package org.apache.tools.ant.filters.util;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.filters.BaseFilterReader;
import org.apache.tools.ant.filters.ChainableReader;
import org.apache.tools.ant.types.AntFilterReader;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Parameterizable;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;

public final class ChainReaderHelper {
   private static final int DEFAULT_BUFFER_SIZE = 8192;
   public Reader primaryReader;
   public int bufferSize = 8192;
   public Vector filterChains = new Vector();
   private Project project = null;
   // $FF: synthetic field
   static Class class$java$io$FilterReader;
   // $FF: synthetic field
   static Class class$java$io$Reader;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$Parameterizable;

   public void setPrimaryReader(Reader rdr) {
      this.primaryReader = rdr;
   }

   public void setProject(Project project) {
      this.project = project;
   }

   public Project getProject() {
      return this.project;
   }

   public void setBufferSize(int size) {
      this.bufferSize = size;
   }

   public void setFilterChains(Vector fchain) {
      this.filterChains = fchain;
   }

   public Reader getAssembledReader() throws BuildException {
      if (this.primaryReader == null) {
         throw new BuildException("primaryReader must not be null.");
      } else {
         Reader instream = this.primaryReader;
         int filterReadersCount = this.filterChains.size();
         Vector finalFilters = new Vector();

         int filtersCount;
         for(filtersCount = 0; filtersCount < filterReadersCount; ++filtersCount) {
            FilterChain filterchain = (FilterChain)this.filterChains.elementAt(filtersCount);
            Vector filterReaders = filterchain.getFilterReaders();
            int readerCount = filterReaders.size();

            for(int j = 0; j < readerCount; ++j) {
               finalFilters.addElement(filterReaders.elementAt(j));
            }
         }

         filtersCount = finalFilters.size();
         if (filtersCount > 0) {
            for(int i = 0; i < filtersCount; ++i) {
               Object o = finalFilters.elementAt(i);
               if (o instanceof AntFilterReader) {
                  AntFilterReader filter = (AntFilterReader)finalFilters.elementAt(i);
                  String className = filter.getClassName();
                  Path classpath = filter.getClasspath();
                  Project pro = filter.getProject();
                  if (className != null) {
                     try {
                        Class clazz = null;
                        if (classpath == null) {
                           clazz = Class.forName(className);
                        } else {
                           AntClassLoader al = pro.createClassLoader(classpath);
                           clazz = Class.forName(className, true, al);
                        }

                        if (clazz != null) {
                           if (!(class$java$io$FilterReader == null ? (class$java$io$FilterReader = class$("java.io.FilterReader")) : class$java$io$FilterReader).isAssignableFrom(clazz)) {
                              throw new BuildException(className + " does not extend java.io.FilterReader");
                           }

                           Constructor[] constructors = clazz.getConstructors();
                           int j = 0;

                           boolean consPresent;
                           for(consPresent = false; j < constructors.length; ++j) {
                              Class[] types = constructors[j].getParameterTypes();
                              if (types.length == 1 && types[0].isAssignableFrom(class$java$io$Reader == null ? (class$java$io$Reader = class$("java.io.Reader")) : class$java$io$Reader)) {
                                 consPresent = true;
                                 break;
                              }
                           }

                           if (!consPresent) {
                              throw new BuildException(className + " does not define a public constructor" + " that takes in a Reader as its " + "single argument.");
                           }

                           Reader[] rdr = new Reader[]{instream};
                           instream = (Reader)constructors[j].newInstance((Object[])rdr);
                           this.setProjectOnObject(instream);
                           if ((class$org$apache$tools$ant$types$Parameterizable == null ? (class$org$apache$tools$ant$types$Parameterizable = class$("org.apache.tools.ant.types.Parameterizable")) : class$org$apache$tools$ant$types$Parameterizable).isAssignableFrom(clazz)) {
                              Parameter[] params = filter.getParams();
                              ((Parameterizable)instream).setParameters(params);
                           }
                        }
                     } catch (ClassNotFoundException var17) {
                        throw new BuildException(var17);
                     } catch (InstantiationException var18) {
                        throw new BuildException(var18);
                     } catch (IllegalAccessException var19) {
                        throw new BuildException(var19);
                     } catch (InvocationTargetException var20) {
                        throw new BuildException(var20);
                     }
                  }
               } else if (o instanceof ChainableReader) {
                  this.setProjectOnObject(o);
                  instream = ((ChainableReader)o).chain(instream);
                  this.setProjectOnObject(instream);
               }
            }
         }

         return instream;
      }
   }

   private void setProjectOnObject(Object obj) {
      if (this.project != null) {
         if (obj instanceof BaseFilterReader) {
            ((BaseFilterReader)obj).setProject(this.project);
         } else {
            this.project.setProjectReference(obj);
         }
      }
   }

   public String readFully(Reader rdr) throws IOException {
      return FileUtils.readFully(rdr, this.bufferSize);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
