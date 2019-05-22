package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.AbstractCollectionConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;

public class SubjectConverter extends AbstractCollectionConverter {
   public SubjectConverter(Mapper mapper) {
      super(mapper);
   }

   public boolean canConvert(Class type) {
      return type.equals(Subject.class);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      Subject subject = (Subject)source;
      this.marshalPrincipals(subject.getPrincipals(), writer, context);
      this.marshalPublicCredentials(subject.getPublicCredentials(), writer, context);
      this.marshalPrivateCredentials(subject.getPrivateCredentials(), writer, context);
      this.marshalReadOnly(subject.isReadOnly(), writer);
   }

   protected void marshalPrincipals(Set principals, HierarchicalStreamWriter writer, MarshallingContext context) {
      writer.startNode("principals");
      Iterator iter = principals.iterator();

      while(iter.hasNext()) {
         Object principal = iter.next();
         this.writeItem(principal, context, writer);
      }

      writer.endNode();
   }

   protected void marshalPublicCredentials(Set pubCredentials, HierarchicalStreamWriter writer, MarshallingContext context) {
   }

   protected void marshalPrivateCredentials(Set privCredentials, HierarchicalStreamWriter writer, MarshallingContext context) {
   }

   protected void marshalReadOnly(boolean readOnly, HierarchicalStreamWriter writer) {
      writer.startNode("readOnly");
      writer.setValue(String.valueOf(readOnly));
      writer.endNode();
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      Set principals = this.unmarshalPrincipals(reader, context);
      Set publicCredentials = this.unmarshalPublicCredentials(reader, context);
      Set privateCredentials = this.unmarshalPrivateCredentials(reader, context);
      boolean readOnly = this.unmarshalReadOnly(reader);
      return new Subject(readOnly, principals, publicCredentials, privateCredentials);
   }

   protected Set unmarshalPrincipals(HierarchicalStreamReader reader, UnmarshallingContext context) {
      return this.populateSet(reader, context);
   }

   protected Set unmarshalPublicCredentials(HierarchicalStreamReader reader, UnmarshallingContext context) {
      return Collections.EMPTY_SET;
   }

   protected Set unmarshalPrivateCredentials(HierarchicalStreamReader reader, UnmarshallingContext context) {
      return Collections.EMPTY_SET;
   }

   protected boolean unmarshalReadOnly(HierarchicalStreamReader reader) {
      reader.moveDown();
      boolean readOnly = Boolean.getBoolean(reader.getValue());
      reader.moveUp();
      return readOnly;
   }

   protected Set populateSet(HierarchicalStreamReader reader, UnmarshallingContext context) {
      Set set = new HashSet();
      reader.moveDown();

      while(reader.hasMoreChildren()) {
         reader.moveDown();
         Object elementl = this.readItem(reader, context, set);
         reader.moveUp();
         set.add(elementl);
      }

      reader.moveUp();
      return set;
   }
}
