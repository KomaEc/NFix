package org.apache.tools.ant.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.TimeComparison;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.Touchable;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.resources.selectors.And;
import org.apache.tools.ant.types.resources.selectors.Date;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.apache.tools.ant.types.resources.selectors.Not;
import org.apache.tools.ant.types.resources.selectors.Or;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.resources.selectors.Type;
import org.apache.tools.ant.types.selectors.SelectorUtils;

public class ResourceUtils {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private static final ResourceSelector NOT_EXISTS = new Not(new Exists());

   public static Resource[] selectOutOfDateSources(ProjectComponent logTo, Resource[] source, FileNameMapper mapper, ResourceFactory targets) {
      return selectOutOfDateSources(logTo, source, mapper, targets, FILE_UTILS.getFileTimestampGranularity());
   }

   public static Resource[] selectOutOfDateSources(ProjectComponent logTo, Resource[] source, FileNameMapper mapper, ResourceFactory targets, long granularity) {
      Union u = new Union();
      u.addAll(Arrays.asList(source));
      ResourceCollection rc = selectOutOfDateSources(logTo, (ResourceCollection)u, mapper, targets, granularity);
      return rc.size() == 0 ? new Resource[0] : ((Union)rc).listResources();
   }

   public static ResourceCollection selectOutOfDateSources(ProjectComponent logTo, ResourceCollection source, FileNameMapper mapper, ResourceFactory targets, long granularity) {
      if (source.size() == 0) {
         logTo.log("No sources found.", 3);
         return Resources.NONE;
      } else {
         ResourceCollection source = Union.getInstance(source);
         logFuture(logTo, source, granularity);
         Union result = new Union();
         Iterator iter = source.iterator();

         while(true) {
            while(iter.hasNext()) {
               Resource sr = (Resource)iter.next();
               String srName = sr.getName();
               srName = srName == null ? srName : srName.replace('/', File.separatorChar);
               String[] targetnames = null;

               try {
                  targetnames = mapper.mapFileName(srName);
               } catch (Exception var14) {
                  logTo.log("Caught " + var14 + " mapping resource " + sr, 3);
               }

               if (targetnames != null && targetnames.length != 0) {
                  Union targetColl = new Union();

                  for(int i = 0; i < targetnames.length; ++i) {
                     targetColl.add(targets.getResource(targetnames[i].replace(File.separatorChar, '/')));
                  }

                  Restrict r = new Restrict();
                  r.add((ResourceSelector)(new And(new ResourceSelector[]{Type.FILE, new Or(new ResourceSelector[]{NOT_EXISTS, new ResourceUtils.Outdated(sr, granularity)})})));
                  r.add((ResourceCollection)targetColl);
                  if (r.size() > 0) {
                     result.add(sr);
                     Resource t = (Resource)((Resource)r.iterator().next());
                     logTo.log(sr.getName() + " added as " + t.getName() + (t.isExists() ? " is outdated." : " doesn't exist."), 3);
                  } else {
                     logTo.log(sr.getName() + " omitted as " + targetColl.toString() + (targetColl.size() == 1 ? " is" : " are ") + " up to date.", 3);
                  }
               } else {
                  logTo.log(sr + " skipped - don't know how to handle it", 3);
               }
            }

            return result;
         }
      }
   }

   public static void copyResource(Resource source, Resource dest) throws IOException {
      copyResource(source, dest, (Project)null);
   }

   public static void copyResource(Resource source, Resource dest, Project project) throws IOException {
      copyResource(source, dest, (FilterSetCollection)null, (Vector)null, false, false, (String)null, (String)null, project);
   }

   public static void copyResource(Resource source, Resource dest, FilterSetCollection filters, Vector filterChains, boolean overwrite, boolean preserveLastModified, String inputEncoding, String outputEncoding, Project project) throws IOException {
      if (!overwrite) {
         long slm = source.getLastModified();
         if (dest.isExists() && slm != 0L && dest.getLastModified() > slm) {
            return;
         }
      }

      boolean filterSetsAvailable = filters != null && filters.hasFilters();
      boolean filterChainsAvailable = filterChains != null && filterChains.size() > 0;
      BufferedReader in;
      BufferedWriter out;
      InputStreamReader isr;
      OutputStreamWriter osw;
      ChainReaderHelper crh;
      Reader rdr;
      if (filterSetsAvailable) {
         in = null;
         out = null;

         try {
            isr = null;
            if (inputEncoding == null) {
               isr = new InputStreamReader(source.getInputStream());
            } else {
               isr = new InputStreamReader(source.getInputStream(), inputEncoding);
            }

            in = new BufferedReader(isr);
            osw = null;
            if (outputEncoding == null) {
               osw = new OutputStreamWriter(dest.getOutputStream());
            } else {
               osw = new OutputStreamWriter(dest.getOutputStream(), outputEncoding);
            }

            out = new BufferedWriter(osw);
            if (filterChainsAvailable) {
               crh = new ChainReaderHelper();
               crh.setBufferSize(8192);
               crh.setPrimaryReader(in);
               crh.setFilterChains(filterChains);
               crh.setProject(project);
               rdr = crh.getAssembledReader();
               in = new BufferedReader(rdr);
            }

            LineTokenizer lineTokenizer = new LineTokenizer();
            lineTokenizer.setIncludeDelims(true);
            rdr = null;

            for(String line = lineTokenizer.getToken(in); line != null; line = lineTokenizer.getToken(in)) {
               if (line.length() == 0) {
                  out.newLine();
               } else {
                  String newline = filters.replaceTokens(line);
                  out.write(newline);
               }
            }
         } finally {
            FileUtils.close((Writer)out);
            FileUtils.close((Reader)in);
         }
      } else if (!filterChainsAvailable && (inputEncoding == null || inputEncoding.equals(outputEncoding)) && (inputEncoding != null || outputEncoding == null)) {
         InputStream in = null;
         OutputStream out = null;

         try {
            in = source.getInputStream();
            out = dest.getOutputStream();
            byte[] buffer = new byte[8192];
            int count = 0;

            do {
               out.write(buffer, 0, count);
               count = in.read(buffer, 0, buffer.length);
            } while(count != -1);
         } finally {
            FileUtils.close(out);
            FileUtils.close(in);
         }
      } else {
         in = null;
         out = null;

         try {
            isr = null;
            if (inputEncoding == null) {
               isr = new InputStreamReader(source.getInputStream());
            } else {
               isr = new InputStreamReader(source.getInputStream(), inputEncoding);
            }

            in = new BufferedReader(isr);
            osw = null;
            if (outputEncoding == null) {
               osw = new OutputStreamWriter(dest.getOutputStream());
            } else {
               osw = new OutputStreamWriter(dest.getOutputStream(), outputEncoding);
            }

            out = new BufferedWriter(osw);
            if (filterChainsAvailable) {
               crh = new ChainReaderHelper();
               crh.setBufferSize(8192);
               crh.setPrimaryReader(in);
               crh.setFilterChains(filterChains);
               crh.setProject(project);
               rdr = crh.getAssembledReader();
               in = new BufferedReader(rdr);
            }

            char[] buffer = new char[8192];

            while(true) {
               int nRead = in.read(buffer, 0, buffer.length);
               if (nRead == -1) {
                  break;
               }

               out.write(buffer, 0, nRead);
            }
         } finally {
            FileUtils.close((Writer)out);
            FileUtils.close((Reader)in);
         }
      }

      if (preserveLastModified && dest instanceof Touchable) {
         setLastModified((Touchable)dest, source.getLastModified());
      }

   }

   public static void setLastModified(Touchable t, long time) {
      t.touch(time < 0L ? System.currentTimeMillis() : time);
   }

   public static boolean contentEquals(Resource r1, Resource r2, boolean text) throws IOException {
      if (r1.isExists() != r2.isExists()) {
         return false;
      } else if (!r1.isExists()) {
         return true;
      } else if (!r1.isDirectory() && !r2.isDirectory()) {
         if (r1.equals(r2)) {
            return true;
         } else if (!text && r1.getSize() != r2.getSize()) {
            return false;
         } else {
            return compareContent(r1, r2, text) == 0;
         }
      } else {
         return false;
      }
   }

   public static int compareContent(Resource r1, Resource r2, boolean text) throws IOException {
      if (r1.equals(r2)) {
         return 0;
      } else {
         boolean e1 = r1.isExists();
         boolean e2 = r2.isExists();
         if (!e1 && !e2) {
            return 0;
         } else if (e1 != e2) {
            return e1 ? 1 : -1;
         } else {
            boolean d1 = r1.isDirectory();
            boolean d2 = r2.isDirectory();
            if (d1 && d2) {
               return 0;
            } else if (!d1 && !d2) {
               return text ? textCompare(r1, r2) : binaryCompare(r1, r2);
            } else {
               return d1 ? -1 : 1;
            }
         }
      }
   }

   private static int binaryCompare(Resource r1, Resource r2) throws IOException {
      InputStream in1 = null;
      BufferedInputStream in2 = null;

      try {
         in1 = new BufferedInputStream(r1.getInputStream());
         in2 = new BufferedInputStream(r2.getInputStream());

         int b1;
         for(b1 = in1.read(); b1 != -1; b1 = in1.read()) {
            int b2 = in2.read();
            if (b1 != b2) {
               int var6 = b1 > b2 ? 1 : -1;
               return var6;
            }
         }

         b1 = in2.read() == -1 ? 0 : -1;
         return b1;
      } finally {
         FileUtils.close((InputStream)in1);
         FileUtils.close((InputStream)in2);
      }
   }

   private static int textCompare(Resource r1, Resource r2) throws IOException {
      BufferedReader in1 = null;
      BufferedReader in2 = null;

      int var10;
      try {
         in1 = new BufferedReader(new InputStreamReader(r1.getInputStream()));
         in2 = new BufferedReader(new InputStreamReader(r2.getInputStream()));

         for(String expected = in1.readLine(); expected != null; expected = in1.readLine()) {
            String actual = in2.readLine();
            if (!expected.equals(actual)) {
               int var6 = expected.compareTo(actual);
               return var6;
            }
         }

         var10 = in2.readLine() == null ? 0 : -1;
      } finally {
         FileUtils.close((Reader)in1);
         FileUtils.close((Reader)in2);
      }

      return var10;
   }

   private static void logFuture(ProjectComponent logTo, ResourceCollection rc, long granularity) {
      long now = System.currentTimeMillis() + granularity;
      Date sel = new Date();
      sel.setMillis(now);
      sel.setWhen(TimeComparison.AFTER);
      Restrict future = new Restrict();
      future.add((ResourceSelector)sel);
      future.add(rc);
      Iterator iter = future.iterator();

      while(iter.hasNext()) {
         logTo.log("Warning: " + ((Resource)iter.next()).getName() + " modified in the future.", 1);
      }

   }

   private static final class Outdated implements ResourceSelector {
      private Resource control;
      private long granularity;

      private Outdated(Resource control, long granularity) {
         this.control = control;
         this.granularity = granularity;
      }

      public boolean isSelected(Resource r) {
         return SelectorUtils.isOutOfDate(this.control, r, this.granularity);
      }

      // $FF: synthetic method
      Outdated(Resource x0, long x1, Object x2) {
         this(x0, x1);
      }
   }
}
