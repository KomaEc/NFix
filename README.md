# Project Name
NFix : Assessment of various tools on dicovering/repairing Java Null Pointer Exception

# Mentors
* Quoc-Sang Phan
* Xuan-Bach D. Le

# Project Summary
This project is supported by Google Summer of Code 2019. NFix aims to collect real world java NPE buggy programs, investigate its behaviour and assess the accuracy of various tools that target at discovering or repairing NPE bugs. NFix contains:
+ Totally 40 real world java benchmarks.
+ Experiments with five different analysis tools:
    + VFix
    + Facebook Infer
    + Coveriy Scan
    + Spotbugs
    + Daikon Invariant Detector and Checker Framework
+ Observation of the weaknesses of those tools.4
+ Overcoming those weaknesses by using invariant inference and static analysis.

# Future Work
* Adding assessment to the ability to automatically repair bugs of different tools
* Building a novel tool that overcome the drawbacks of existing tools.

## Useful Command
```bash
echo $(echo ./*.jar | tr ' ' ':') >> dependency.txt
sed 's/.\//\//g' dependency.txt
insert-annotations-to-source target/classes/nullable-annotations.jaif $(find ./src/main/java/org -name '*.java' | tr '\n' ' ')

```


