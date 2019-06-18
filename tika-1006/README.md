# tika-1006

## Commit
commit d8224a060e879dae36e8f81eb14802d354612742
https://svn.apache.org/viewvc/tika/trunk/tika-parent/pom.xml?revision=1359817&view=markup&pathrev=1397653

## Infer
Can't find relevant position

## VFix
Need external source file. (Can't get active body)

## Type
null check to skip a few lines

## Patch
```bash
*** src/main/java/org/apache/tika/parser/microsoft/ooxml/XWPFWordExtractorDecorator.java	2019-06-18 13:49:27.740938227 +0800
--- origin.txt	2019-06-18 13:49:07.095084869 +0800
***************
*** 124,134 ****
--- 124,136 ----
                  paragraph.getStyleID()
            );
  
+           if (style != null) {
               TagAndStyle tas = WordExtractor.buildParagraphTagAndStyle(
                     style.getName(), paragraph.getPartType() == BodyType.TABLECELL
               );
               tag = tas.getTag();
               styleClass = tas.getStyleClass();
+           }
         }
         
         if(styleClass == null) {

```