Benchmark TPC-DS through Spark thriftserver
=========

## Build jar package
```shell
mvn clean package assembly:single
```

## Run jar
```shell
nohup java -jar  tpcds-benchmark-1.0-SNAPSHOT-jar-with-dependencies.jar jdbc:hive2://127.0.0.1:10000 tpcds q1,q2,q3,q4,q5,q6,q7,q8,q9,q10,q11,q12,q13,q14a,q14b,q15,q16,q17,q18,q19,q20,q21,q22,q23a,q23b,q24a,q24b,q25,q26,q27,q28,q29,q30,q31,q32,q33,q34,q35,q36,q37,q38,q39a,q39b,q40,q41,q42,q43,q44,q45,q46,q47,q48,q49,q50,q51,q52,q53,q54,q55,q56,q57,q58,q59,q60,q61,q62,q63,q64,q65,q66,q67,q68,q69,q70,q71,q72,q73,q74,q75,q76,q77,q78,q79,q80,q81,q82,q83,q84,q85,q86,q87,q88,q89,q90,q91,q92,q93,q94,q95,q96,q97,q98,q99 PartialAggregate > benchmark.result &
```

## Example
```shell
java -jar  tpcds-benchmark-1.0-SNAPSHOT-jar-with-dependencies.jar jdbc:hive2://127.0.0.1:10000 tpcds q24a,q38,q74,q82,q87
# Output
Running benchmark: Benchmark q24a
  Running case: Feature disabled
  Stopped after 3 iterations, 2096965 ms
  Running case: Feature enabled
  Stopped after 3 iterations, 208366 ms

OpenJDK 64-Bit Server VM 1.8.0_275-b01 on Linux 5.4.0-96-generic
Intel(R) Xeon(R) Gold 6230R CPU @ 2.10GHz
Benchmark q24a:                           Best Time(ms)   Avg Time(ms)   Stdev(ms)    Rate(M/s)   Per Row(ns)   Relative
------------------------------------------------------------------------------------------------------------------------
Feature disabled                                 460342         698988         NaN          0.0      Infinity       1.0X
Feature enabled                                   48584          69456        1989          0.0      Infinity       9.5X

q24a has finished and the result match: true.
Running benchmark: Benchmark q38
  Running case: Feature disabled
  Stopped after 3 iterations, 118127 ms
  Running case: Feature enabled
  Stopped after 3 iterations, 65854 ms

OpenJDK 64-Bit Server VM 1.8.0_275-b01 on Linux 5.4.0-96-generic
Intel(R) Xeon(R) Gold 6230R CPU @ 2.10GHz
Benchmark q38:                            Best Time(ms)   Avg Time(ms)   Stdev(ms)    Rate(M/s)   Per Row(ns)   Relative
------------------------------------------------------------------------------------------------------------------------
Feature disabled                                  32880          39376        1954          0.0      Infinity       1.0X
Feature enabled                                   19104          21952         NaN          0.0      Infinity       1.7X

q38 has finished and the result match: true.
Running benchmark: Benchmark q74
  Running case: Feature disabled
  Stopped after 3 iterations, 129460 ms
  Running case: Feature enabled
  Stopped after 3 iterations, 59805 ms

OpenJDK 64-Bit Server VM 1.8.0_275-b01 on Linux 5.4.0-96-generic
Intel(R) Xeon(R) Gold 6230R CPU @ 2.10GHz
Benchmark q74:                            Best Time(ms)   Avg Time(ms)   Stdev(ms)    Rate(M/s)   Per Row(ns)   Relative
------------------------------------------------------------------------------------------------------------------------
Feature disabled                                  41289          43153        1781          0.0      Infinity       1.0X
Feature enabled                                   17815          19935         NaN          0.0      Infinity       2.3X

q74 has finished and the result match: true.
Running benchmark: Benchmark q82
  Running case: Feature disabled
  Stopped after 3 iterations, 127246 ms
  Running case: Feature enabled
  Stopped after 3 iterations, 45532 ms

OpenJDK 64-Bit Server VM 1.8.0_275-b01 on Linux 5.4.0-96-generic
Intel(R) Xeon(R) Gold 6230R CPU @ 2.10GHz
Benchmark q82:                            Best Time(ms)   Avg Time(ms)   Stdev(ms)    Rate(M/s)   Per Row(ns)   Relative
------------------------------------------------------------------------------------------------------------------------
Feature disabled                                  38953          42416         NaN          0.0      Infinity       1.0X
Feature enabled                                   11475          15178        2107          0.0      Infinity       3.4X

q82 has finished and the result match: true.
Running benchmark: Benchmark q87
  Running case: Feature disabled
  Stopped after 3 iterations, 110526 ms
  Running case: Feature enabled
  Stopped after 3 iterations, 64030 ms

OpenJDK 64-Bit Server VM 1.8.0_275-b01 on Linux 5.4.0-96-generic
Intel(R) Xeon(R) Gold 6230R CPU @ 2.10GHz
Benchmark q87:                            Best Time(ms)   Avg Time(ms)   Stdev(ms)    Rate(M/s)   Per Row(ns)   Relative
------------------------------------------------------------------------------------------------------------------------
Feature disabled                                  33228          36842        1866          0.0      Infinity       1.0X
Feature enabled                                   19132          21343        1988          0.0      Infinity       1.7X

q87 has finished and the result match: true.
```
