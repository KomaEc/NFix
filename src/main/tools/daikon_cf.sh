# Run  application one or more times to create a trace file.
java -cp $DAIKONDIR/daikon.jar:${CLASS_PATH} daikon.Chicory \
     --dtrace-file=an.dtrace.gz <project_main_class>

# Run Daikon on the resulting .dtrace file:
java -cp $DAIKONDIR/daikon.jar daikon.Daikon an.dtrace.gz --no_text_output \
    --config $DAIKONDIR/java/daikon/annotate_nullable.config

# Run the AnnotateNullable tool to create an annotation index file. 
java -cp $DAIKONDIR/daikon.jar daikon.AnnotateNullable an.inv.gz > nullable-annotations.jaif

