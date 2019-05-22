package com.gzoltar.shaded.org.pitest.reloc.xstream;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterMatcher;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterRegistry;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.javabean.JavaBeanProvider;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.FieldKeySorter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionProvider;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamDriver;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.XppDriver;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.AnyTypePermission;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.TypeHierarchyPermission;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.TypePermission;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.WildcardTypePermission;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.datatype.DatatypeFactory;

public class XStreamer {
   private static final TypePermission[] PERMISSIONS = new TypePermission[]{new TypeHierarchyPermission(ConverterMatcher.class), new TypeHierarchyPermission(Mapper.class), new TypeHierarchyPermission(XStream.class), new TypeHierarchyPermission(ReflectionProvider.class), new TypeHierarchyPermission(JavaBeanProvider.class), new TypeHierarchyPermission(FieldKeySorter.class), new TypeHierarchyPermission(ConverterLookup.class), new TypeHierarchyPermission(ConverterRegistry.class), new TypeHierarchyPermission(HierarchicalStreamDriver.class), new TypeHierarchyPermission(MarshallingStrategy.class), new TypeHierarchyPermission(MarshallingContext.class), new TypeHierarchyPermission(UnmarshallingContext.class), new TypeHierarchyPermission(NameCoder.class), new TypeHierarchyPermission(TypePermission.class), new WildcardTypePermission(new String[]{JVM.class.getPackage().getName() + ".**"}), new TypeHierarchyPermission(DatatypeFactory.class)};

   public String toXML(XStream xstream, Object obj) throws ObjectStreamException {
      StringWriter writer = new StringWriter();

      try {
         this.toXML(xstream, obj, writer);
      } catch (ObjectStreamException var5) {
         throw var5;
      } catch (IOException var6) {
         throw new ConversionException("Unexpected IO error from a StringWriter", var6);
      }

      return writer.toString();
   }

   public void toXML(XStream xstream, Object obj, Writer out) throws IOException {
      XStream outer = new XStream();
      ObjectOutputStream oos = outer.createObjectOutputStream(out);

      try {
         oos.writeObject(xstream);
         oos.flush();
         xstream.toXML(obj, out);
      } finally {
         oos.close();
      }

   }

   public Object fromXML(String xml) throws ClassNotFoundException, ObjectStreamException {
      try {
         return this.fromXML((Reader)(new StringReader(xml)));
      } catch (ObjectStreamException var3) {
         throw var3;
      } catch (IOException var4) {
         throw new ConversionException("Unexpected IO error from a StringReader", var4);
      }
   }

   public Object fromXML(String xml, TypePermission[] permissions) throws ClassNotFoundException, ObjectStreamException {
      try {
         return this.fromXML((Reader)(new StringReader(xml)), (TypePermission[])permissions);
      } catch (ObjectStreamException var4) {
         throw var4;
      } catch (IOException var5) {
         throw new ConversionException("Unexpected IO error from a StringReader", var5);
      }
   }

   public Object fromXML(HierarchicalStreamDriver driver, String xml) throws ClassNotFoundException, ObjectStreamException {
      try {
         return this.fromXML((HierarchicalStreamDriver)driver, (Reader)(new StringReader(xml)));
      } catch (ObjectStreamException var4) {
         throw var4;
      } catch (IOException var5) {
         throw new ConversionException("Unexpected IO error from a StringReader", var5);
      }
   }

   public Object fromXML(HierarchicalStreamDriver driver, String xml, TypePermission[] permissions) throws ClassNotFoundException, ObjectStreamException {
      try {
         return this.fromXML(driver, (Reader)(new StringReader(xml)), permissions);
      } catch (ObjectStreamException var5) {
         throw var5;
      } catch (IOException var6) {
         throw new ConversionException("Unexpected IO error from a StringReader", var6);
      }
   }

   public Object fromXML(Reader xml) throws IOException, ClassNotFoundException {
      return this.fromXML((HierarchicalStreamDriver)(new XppDriver()), (Reader)xml);
   }

   public Object fromXML(Reader xml, TypePermission[] permissions) throws IOException, ClassNotFoundException {
      return this.fromXML(new XppDriver(), (Reader)xml, permissions);
   }

   public Object fromXML(HierarchicalStreamDriver driver, Reader xml) throws IOException, ClassNotFoundException {
      return this.fromXML(driver, xml, new TypePermission[]{AnyTypePermission.ANY});
   }

   public Object fromXML(HierarchicalStreamDriver driver, Reader xml, TypePermission[] permissions) throws IOException, ClassNotFoundException {
      XStream outer = new XStream(driver);

      for(int i = 0; i < permissions.length; ++i) {
         outer.addPermission(permissions[i]);
      }

      HierarchicalStreamReader reader = driver.createReader(xml);
      ObjectInputStream configIn = outer.createObjectInputStream(reader);

      Object var9;
      try {
         XStream configured = (XStream)configIn.readObject();
         ObjectInputStream in = configured.createObjectInputStream(reader);

         try {
            var9 = in.readObject();
         } finally {
            in.close();
         }
      } finally {
         configIn.close();
      }

      return var9;
   }

   public static TypePermission[] getDefaultPermissions() {
      return (TypePermission[])((TypePermission[])PERMISSIONS.clone());
   }
}
