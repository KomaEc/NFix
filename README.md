# NPE Bugs Dataset

## To Use
To compile, type `make`. To clean all files, type `make clean`# NPE Bugs

## 4 Sections
* How do we collect bugs and how do we categorize them. Show that our method in finding bugs is good enough, and the bugs are diverse enough.
* How do static analysis tools perform on localization? If the performance is bad, why? Understand the high-level idea of papers for each static analysis tool
* If possible, how to improve them

## Ideas
* APR tool that use static analysis result only, but not rely on test cases
* Using SPF to remove false positives? And compare ability to localization between static tools and dynamic tools

## Useful Command
```bash
echo $(echo ./*.jar | tr ' ' ':') >> dependency.txt
sed 's/.\//\//g' dependency.txt 
```


