package com.mks.api.common;

public final class XMLResponseDef {
   public static String XML_PROLOG = "<?xml version=\"1.0\"?>";
   public static String XML_ROOT_TAG = "Response";
   public static String XML_CONNECTION_TAG = "App-Connection";
   public static String XML_EXITCODE_TAG = "ExitCode";
   public static String XML_RESULT_TAG = "Result";
   public static String XML_RESULT_FIELD = "resultant";
   public static String XML_SUBITEM_TAG = "Item";
   public static String XML_FIELD_TAG = "Field";
   public static String XML_SUBOPERATION_TAG = "SubRtn";
   public static String XML_SELECTION_TAG = "WorkItems";
   public static String XML_EXCEPTION_TAG = "Exception";
   public static String XML_WORKITEM_TAG = "WorkItem";
   public static String XML_LIST_TAG = "List";
   public static String XML_VALUE_TAG = "Value";
   public static String XML_TOKEN_VALUE_TAG = "TokenValue";
   public static String XML_DISPLAY_VALUE_TAG = "DisplayValue";
   public static String XML_MESSAGE_TAG = "Message";
   public static String XML_APIVERSION_ATTR = "version";
   public static String XML_COMMAND_ATTR = "command";
   public static String XML_COMMAND_APP_ATTR = "app";
   public static String XML_COMMAND_VENDOR_ATTR = "vendor";
   public static String XML_CONNECTION_HOST_ATTR = "server";
   public static String XML_CONNECTION_PORT_ATTR = "port";
   public static String XML_CONNECTION_USER_ATTR = "userID";
   public static String XML_SUBOPERATION_ATTR = "routine";
   public static String XML_ID_ATTR = "id";
   public static String XML_SELECTION_TYPE_ATTR = "selectionType";
   public static String XML_ELEMENT_TYPE_ATTR = "elementType";
   public static String XML_ITEMLIST_ATTR = "item";
   public static String XML_CONTEXT_ATTR = "context";
   public static String XML_DISPLAYID_ATTR = "displayId";
   public static String XML_MODELTYPE_ATTR = "modelType";
   public static String XML_TARGET_MODELTYPE_ATTR = "target.modelType";
   public static String XML_PARENT_ATTR = "parent";
   public static String XML_BEFORE_ATTR = "sibling.before";
   public static String XML_AFTER_ATTR = "sibling.after";
   public static String XML_SEGMENT_ATTR = "segment";
   public static String XML_RELATIONSHIP_FLAG_ATTR = "relationshipFlags";
   public static String XML_EXCEPTIONCLASS_ATTR = "class";
   public static String XML_EXCEPTION_NAME = "exception-name";
   public static String XML_EXCEPTION_INTERNAL_NAME = "internal-classname";
   public static String XML_EXCEPTION_ITEM_ID = "item-id";
   public static String XML_EXCEPTION_ITEM_MODELTYPE = "item-modelType";
   public static String XML_EXCEPTION_ITEM_CONTEXT = "item-context";
   public static String XML_NAME_ATTR = "name";
   public static String XML_DISP_NAME_ATTR = "displayName";
   public static String XML_DATATYPE_ATTR = "dataType";
   public static String XML_DATATYPE_INT = "int";
   public static String XML_DATATYPE_STRING = "string";
   public static String XML_DATATYPE_BOOLEAN = "boolean";
   public static String XML_DATATYPE_DOUBLE = "double";
   public static String XML_DATATYPE_FLOAT = "float";
   public static String XML_DATATYPE_LONG = "long";
   public static String XML_DATATYPE_TIMESTAMP = "datetime";

   private XMLResponseDef() {
   }
}
