# Debug
Kafka Streams Produced Wrong (duplicated) Results with Simple Windowed Aggregation Case



```java
events
      .map((k, v) -> KeyValue.pair(v.split(",")[1], v.split(",")[0]))
      .groupByKey()
      .windowedBy(TimeWindows.of(TimeUnit.SECONDS.toMillis(10)))
      .aggregate(
        ArrayList::new,
        (type, id, eventList) -> {
          eventList.add(id);
          return eventList;
        },
        Materialized.with(stringSerde, arraySerde)
      )
      .toStream((k,v) -> k.key())
      .mapValues((v)-> String.join(",", v))
      .to("ks-debug-output", Produced.with(stringSerde, stringSerde));
```

## Input

<pre>
1,t6
2,t1
3,t7
4,t5
5,t5
6,t6
7,t6
8,t4
9,t6
10,t7
11,t6
12,t5
13,t6
14,t4
15,t4
16,t2
17,t7
18,t6
19,t3
20,t7
21,t1
22,t5
23,t5
24,t6
25,t6
26,t4
27,t4
28,t3
29,t2
30,t5
31,t1
32,t1
33,t1
34,t1
35,t2
36,t4
37,t3
38,t3
39,t6
40,t6
41,t1
42,t4
43,t4
44,t6
45,t6
46,t7
47,t7
48,t3
49,t1
50,t6
51,t1
52,t4
53,t6
54,t7
55,t1
56,t1
57,t1
58,t5
59,t6
60,t7
61,t6
62,t4
63,t5
64,t1
65,t3
66,t1
67,t3
68,t3
69,t5
70,t1
71,t6
72,t5
73,t6
74,t1
75,t7
76,t5
77,t3
78,t1
79,t4
80,t3
81,t6
82,t2
83,t6
84,t2
85,t4
86,t7
87,t4
88,t6
89,t5
90,t6
91,t4
92,t3
93,t4
94,t6
95,t2
96,t2
97,t7
98,t4
99,t3
100,t3
</pre>

## Output

<pre>
t6	1
t1	2
t7	3
t5	4
t5	4,5
t6	6
t6	6,7
t4	8
t6	9
t7	10
t6	9,11
t5	12
t6	13
t4	14
t4	14,15
t2	16
t7	17
t6	18
t3	19
t7	20
t1	21
t5	22
t5	22,23
t6	24
t6	24,25
t4	26
t4	26,27
t3	28
t2	29
t5	30
t1	31
t1	32
t1	32,33
t1	32,33,34
t2	35
t4	36
t3	37
t3	37,38
t6	39
t6	39,40
t1	41
t4	42
t4	42,43
t6	44
t6	44,45
t7	46
t7	46,47
t3	48
t1	49
t6	50
t1	49,51
t4	52
t6	53
t7	54
t1	55
t1	56
t1	56,57
t5	58
t6	59
t7	60
t6	59,61
t4	62
t5	63
t1	64
t3	65
t1	66
t3	67
t3	67,68
t5	69
t1	70
t6	71
t5	72
t6	73
t1	74
t7	75
t5	76
t3	77
t1	78
t4	79
t3	80
t6	81
t2	82
t6	83
t2	82,84
t4	85
t7	86
t4	87
t6	88
t5	89
t6	90
t4	91
t3	92
t4	93
t6	94
t2	95
t2	96
t7	97
t4	98
t3	99
t3	99,100
</pre>
