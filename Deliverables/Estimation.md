1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
# Project Estimation  
Authors: Massimiliano Pronesti, Umberto Pepato, Matteo Notarangelo, Davide Mammone

Date: 29/04/2021

Version: 1.0 
# Contents
- [Estimate by product decomposition](#estimate-by-product-decomposition)
- [Estimate by activity decomposition ](#estimate-by-activity-decomposition)
# Estimation approach
<Consider the EZGas  project as described in YOUR requirement document, assume that you are going to develop the project INDEPENDENT of the deadlines of the course>
# Estimate by product decomposition
### 
|             | Estimate                        |             
| ----------- | ------------------------------- |  
| NC =  Estimated number of classes to be developed   |           19                  |             
|  A = Estimated average size per class, in LOC       |             110               | 
| S = Estimated size of project, in LOC (= NC * A) | 2090 |
| E = Estimated effort, in person hours (here use productivity 10 LOC per person hour)  |             209                         |   
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro) | 6270 | 
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ) |         1.3            |               
# Estimate by activity decomposition
### 
|         Activity name    | Estimated effort (person hours)   |             
| ----------- | ------------------------------- | 
|Requirements | 30 |
|Architecture | 10|
|Design | 20 |
|Coding| 130 |
|Integration| 25 |
|Validation| 25 |


# Gantt chart
```plantuml
@startuml
[Requirements] lasts 1 days
then [Architecture and Design] lasts 1 days
then [Coding] lasts 4 days
then [Integration] lasts 1 days
then [Validation] lasts 1 days
@enduml
```
