package soot.tagkit;

public interface Tag {
   String getName();

   byte[] getValue() throws AttributeValueException;
}
