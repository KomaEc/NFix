# Project Name
NFix : Assessment of various tools on dicovering/repairing Java Null Pointer Exception

# Mentors
* Quoc-Sang Phan
* Xuan-Bach D. Le

# Project Summary
This project is supported by Google Summer of Code 2019. NFix aims to collect real world java NPE buggy programs, investigate its behaviour and assess the accuracy of various tools that target at discovering or repairing NPE bugs. NFix contains totally 40 real world java benchmarks, together with the analysis result of four different tools :
+ Facebook Infer
+ Coveriy Scan
+ Spotbugs
+ Daikon Invariant Detector and Checker Framework



# Future Work
* Currently NFix contain assessment of the ability to locale fault of various tools. The next step is to do further assessment on the ability to repair automatically.
* The drawbacks of some tools are clear, the next step is to extend or modify current tools.

## Useful Command
```bash
echo $(echo ./*.jar | tr ' ' ':') >> dependency.txt
sed 's/.\//\//g' dependency.txt
insert-annotations-to-source target/classes/nullable-annotations.jaif $(find ./src/main/java/org -name '*.java' | tr '\n' ' ')

```


