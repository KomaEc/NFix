package groovy.lang;

public @interface Grapes {
   Grab[] value();

   boolean initClass() default true;
}
