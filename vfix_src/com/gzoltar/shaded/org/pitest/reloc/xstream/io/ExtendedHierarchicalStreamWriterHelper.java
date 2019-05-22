package com.gzoltar.shaded.org.pitest.reloc.xstream.io;

public class ExtendedHierarchicalStreamWriterHelper {
   public static void startNode(HierarchicalStreamWriter writer, String name, Class clazz) {
      if (writer instanceof ExtendedHierarchicalStreamWriter) {
         ((ExtendedHierarchicalStreamWriter)writer).startNode(name, clazz);
      } else {
         writer.startNode(name);
      }

   }
}
