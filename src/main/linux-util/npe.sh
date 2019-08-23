
# dependency
echo $(echo path/to/deps/*.jar | tr ' ' ':') >> npe.deps

## organize dependencies into a specific format
sed 's/.\//\//g' npe.deps

# checking difference
diff -u src/main/java/path/to/bug.java path/to/patch >> npe.diff