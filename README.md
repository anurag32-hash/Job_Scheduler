# Project Management (Scheduler)

In this part we will use Trie, Red-Black Tree and Priority Queue to implement a Job
scheduler (Project management). The main part of this part are:

```
1 package ProjectManagement;
2 public class Job implements Comparable<Job> {
3 @Override
4 public int compareTo(Job job) {
5 return 0;
6 }
7 }
Listing 13: Job class
```
```
A job can have two status: REQUESTED, COMPLETED.
```
6.1 Specifications

The main component in this part of the assignment is a Job. Each Job will belong to a Project
and created by an User. The name of the Jobs will be unique (this is guaranteed in the test
cases). All the jobs have a running time, i.e. the time required to run this job. The priority of a
job is same as of that its project and a job can only be executed if its running time is less than
the current budget of the Project. Successfully running a Job, will reduce the budget of that
project by running time of the project.

All the projects will be stored in a Trie, using the project name as the key. Project names will
be unique. All the Jobs will be stored in a Priority Queue, specifically a Max-Heap, using
their priorities as the key.


6.2 Commands

A sample input file is shown in Listing 15.

1
USER: Create the user with given user name.
2
PROJECT: Create a project. NAME PRIORITY BUDGET
3
JOB: Create a job. NAME PROJECT USER RUNTIME
4
QUERY: Return the status of the Job queried.
5
ADD: Increase the budget of the project. PROJECT BUDGET
6
EMPTY_LINE: Let the scheduler execute a single JOB.

6.3 Scheduler specifications

The scheduler will execute a single job whenever it will encounter an empty line in the input
specifications. After the end of the INP (input file) file, scheduler will continue to execute
jobs till there are jobs left that can be executed.

Each time the scheduler wants to execute a job, it will do the following:

1
It selects the job with the highest priority from the MAX HEAP.
2
It first check the running time of the Job, say t.
3
It will then fetch the project from the RB-Tree and check its budget, say B.
4
If B ≥ t then it executes the job. Executing a job means:

- Set the status of the job to complete.
- Increase the global time by job time.
- Set the completed time of the job as the current global time.
- Decrease the budget of the project by run-time of the job. i.e. = B - t, where
    is the new budget of the project.

5
If: B < t, then select the next job from the max-heap (where jobs are stored) and try to
execute this.
6
A scheduler will return in following cases:

- It successfully executed a single job.
- There are no jobs to be executed.
- None of the jobs can be executed because of the budget issue.


7
After the execution returns, process the next batch of commands (all the commands
till next EMPTY_LINE or EOF).
8
If there are no more commands in the INP (input file) file, then let the scheduler
execute jobs till there are no jobs left, or no jobs can be executed because of budget
issues. This marks the END of the execution.
9
Print the stats of the current system. See Listing 16.

```
package ProjectManagement;
/**
* DO NOT MODIFY
*/
public interface SchedulerInterface {
/**
* @param cmd Handles Project creation. Input is the command from INP1 file in array format (use space to split it)
*/
void handle_project(String[] cmd);
/**
* @param cmd Handles Job creation. Input is the command from INP1 file in array format (use space to split it)
*/
void handle_job(String[] cmd);
/**
* @param name Handles user creation
*/
void handle_user(String name);
/**
* Returns status of a job
*
* @param key
*/
void handle_query(String key);
/**
* Next cycle, is executed whenever an empty line is found.
*/
void handle_empty_line();
/**
* Executed as a thread to server a job.
*/
void schedule();
/**
* Add budget to a project Input is the command from INP1 file in array format (use space to split it)
*
* @param cmd
*/
void handle_add(String[] cmd);
/**
* If there are no lines in the input commands, but there are jobs which can be executed,let the system run till there are no jobs left (which can be run).
*/
void run_to_completion();
/**
* After execution is done, print the stats of teh system
*/
void print_stats();
}
Listing 14: Interface specification
```

1 USER Rob
2 USER Harry
3 USER Carry
4 PROJECT IITD.CS.ML.ICML 10 15
5 PROJECT IITD.CS.OS.ASPLOS 9 100
6 PROJECT IITD.CS.TH.SODA 8 100
7 JOB DeepLearning IITD.CS.ML.ICML Rob 10
8 JOB ImageProcessing IITD.CS.ML.ICML Carry 10
9 JOB Pipeline IITD.CS.OS.ASPLOS Harry 10
10 JOB Kmeans IITD.CS.TH.SODA Carry 10
11
12 QUERY Kmeans
13 QUERY Doesnotexists
14
15 JOB DeepLearningNoProject IITD.CS.ML.ICM Rob 10
16 JOB DeepLearningNoUser IITD.CS.ML.ICML Rob2 10
17
18 JOB DeepLearning1 IITD.CS.ML.ICML Rob 10
19 JOB ImageProcessing1 IITD.CS.ML.ICML Carry 10
20 JOB Pipeline1 IITD.CS.OS.ASPLOS Harry 10
21 JOB Kmeans1 IITD.CS.TH.SODA Carry 10
22
23 JOB DeepLearning2 IITD.CS.ML.ICML Rob 10
24 JOB ImageProcessing2 IITD.CS.ML.ICML Carry 10
25 JOB Pipeline2 IITD.CS.OS.ASPLOS Harry 10
26 JOB Kmeans3 IITD.CS.TH.SODA Carry 10
27
28 ADD IITD.CS.ML.ICML 60
29 JOB DeepLearning3 IITD.CS.ML.ICML Rob 10
30 JOB ImageProcessing3 IITD.CS.ML.ICML Carry 10
31 JOB Pipeline3 IITD.CS.OS.ASPLOS Harry 10
32 JOB Kmeans3 IITD.CS.TH.SODA Carry 10
33
34 QUERY Kmeans
35
36 JOB DeepLearning4 IITD.CS.ML.ICML Rob 10
37 JOB ImageProcessing4 IITD.CS.ML.ICML Carry 10
38 JOB Pipeline4 IITD.CS.OS.ASPLOS Harry 10


39 JOB Kmeans4 IITD.CS.TH.SODA Carry 10
40
41 JOB DeepLearning5 IITD.CS.ML.ICML Rob 10
42 JOB ImageProcessing5 IITD.CS.ML.ICML Carry 10
43 JOB Pipeline5 IITD.CS.OS.ASPLOS Harry 10
44 JOB Kmeans5 IITD.CS.TH.SODA Carry 10
45
46 QUERY Kmeans
Listing 15: Input specification

1 Creating user
2 Creating user
3 Creating user
4 Creating project
5 Creating project
6 Creating project
7 Creating job
8 Creating job
9 Creating job
10 Creating job
11 Running code
12 Remaining jobs: 4
13 Executing: DeepLearning from: IITD.CS.ML.ICML
14 Project: IITD.CS.ML.ICML budget remaining: 5
15 Execution cycle completed
16 Querying
17 Kmeans: NOT FINISHED
18 Querying
19 Doesnotexists: NO SUCH JOB
20 Running code
21 Remaining jobs: 3
22 Executing: ImageProcessing from: IITD.CS.ML.ICML
23 Un-sufficient budget.
24 Executing: Pipeline from: IITD.CS.OS.ASPLOS
25 Project: IITD.CS.OS.ASPLOS budget remaining: 90
26 Execution cycle completed
27 Creating job
28 No such project exists. IITD.CS.ML.ICM
29 Creating job
30 No such user exists: Rob
31 Running code
32 Remaining jobs: 1
33 Executing: Kmeans from: IITD.CS.TH.SODA
34 Project: IITD.CS.TH.SODA budget remaining: 90
35 Execution cycle completed
36 Creating job
37 Creating job
38 Creating job
39 Creating job
40 Running code


41 Remaining jobs: 4
42 Executing: DeepLearning1 from: IITD.CS.ML.ICML
43 Un-sufficient budget.
44 Executing: ImageProcessing1 from: IITD.CS.ML.ICML
45 Un-sufficient budget.
46 Executing: Pipeline1 from: IITD.CS.OS.ASPLOS
47 Project: IITD.CS.OS.ASPLOS budget remaining: 80
48 Execution cycle completed
49 Creating job
50 Creating job
51 Creating job
52 Creating job
53 Running code
54 Remaining jobs: 5
55 Executing: DeepLearning2 from: IITD.CS.ML.ICML
56 Un-sufficient budget.
57 Executing: ImageProcessing2 from: IITD.CS.ML.ICML
58 Un-sufficient budget.
59 Executing: Pipeline2 from: IITD.CS.OS.ASPLOS
60 Project: IITD.CS.OS.ASPLOS budget remaining: 70
61 Execution cycle completed
62 ADDING Budget
63 Creating job
64 Creating job
65 Creating job
66 Creating job
67 Running code
68 Remaining jobs: 11
69 Executing: ImageProcessing from: IITD.CS.ML.ICML
70 Project: IITD.CS.ML.ICML budget remaining: 55
71 Execution cycle completed
72 Querying
73 Kmeans: COMPLETED
74 Running code
75 Remaining jobs: 10
76 Executing: DeepLearning1 from: IITD.CS.ML.ICML
77 Project: IITD.CS.ML.ICML budget remaining: 45
78 Execution cycle completed
79 Creating job
80 Creating job
81 Creating job
82 Creating job
83 Running code
84 Remaining jobs: 13
85 Executing: ImageProcessing1 from: IITD.CS.ML.ICML
86 Project: IITD.CS.ML.ICML budget remaining: 35
87 Execution cycle completed
88 Creating job
89 Creating job
90 Creating job


91 Creating job
92 Running code
93 Remaining jobs: 16
94 Executing: DeepLearning2 from: IITD.CS.ML.ICML
95 Project: IITD.CS.ML.ICML budget remaining: 25
96 Execution cycle completed
97 Querying
98 Kmeans: COMPLETED
99 Running code
100 Remaining jobs: 15
101 Executing: ImageProcessing2 from: IITD.CS.ML.ICML
102 Project: IITD.CS.ML.ICML budget remaining: 15
103 System execution completed
104 Running code
105 Remaining jobs: 14
106 Executing: DeepLearning3 from: IITD.CS.ML.ICML
107 Project: IITD.CS.ML.ICML budget remaining: 5
108 System execution completed
109 Running code
110 Remaining jobs: 13
111 Executing: ImageProcessing3 from: IITD.CS.ML.ICML
112 Un-sufficient budget.
113 Executing: DeepLearning4 from: IITD.CS.ML.ICML
114 Un-sufficient budget.
115 Executing: ImageProcessing4 from: IITD.CS.ML.ICML
116 Un-sufficient budget.
117 Executing: DeepLearning5 from: IITD.CS.ML.ICML
118 Un-sufficient budget.
119 Executing: ImageProcessing5 from: IITD.CS.ML.ICML
120 Un-sufficient budget.
121 Executing: Pipeline3 from: IITD.CS.OS.ASPLOS
122 Project: IITD.CS.OS.ASPLOS budget remaining: 60
123 System execution completed
124 Running code
125 Remaining jobs: 7
126 Executing: Pipeline4 from: IITD.CS.OS.ASPLOS
127 Project: IITD.CS.OS.ASPLOS budget remaining: 50
128 System execution completed
129 Running code
130 Remaining jobs: 6
131 Executing: Pipeline5 from: IITD.CS.OS.ASPLOS
132 Project: IITD.CS.OS.ASPLOS budget remaining: 40
133 System execution completed
134 Running code
135 Remaining jobs: 5
136 Executing: Kmeans1 from: IITD.CS.TH.SODA
137 Project: IITD.CS.TH.SODA budget remaining: 80
138 System execution completed
139 Running code
140 Remaining jobs: 4


141 Executing: Kmeans3 from: IITD.CS.TH.SODA
142 Project: IITD.CS.TH.SODA budget remaining: 70
143 System execution completed
144 Running code
145 Remaining jobs: 3
146 Executing: Kmeans3 from: IITD.CS.TH.SODA
147 Project: IITD.CS.TH.SODA budget remaining: 60
148 System execution completed
149 Running code
150 Remaining jobs: 2
151 Executing: Kmeans4 from: IITD.CS.TH.SODA
152 Project: IITD.CS.TH.SODA budget remaining: 50
153 System execution completed
154 Running code
155 Remaining jobs: 1
156 Executing: Kmeans5 from: IITD.CS.TH.SODA
157 Project: IITD.CS.TH.SODA budget remaining: 40
158 System execution completed
159 --------------STATS---------------
160 Total jobs done: 19
161 Job{user=’Rob’, project=’IITD.CS.ML.ICML’, jobstatus=COMPLETED, execution_tim
e=10, end_time=10, name=’DeepLearning’}
162 Job{user=’Harry’, project=’IITD.CS.OS.ASPLOS’, jobstatus=COMPLETED, execution_
time=10, end_time=20, name=’Pipeline’}
163 Job{user=’Carry’, project=’IITD.CS.TH.SODA’, jobstatus=COMPLETED, execution_ti
me=10, end_time=30, name=’Kmeans’}
164 Job{user=’Harry’, project=’IITD.CS.OS.ASPLOS’, jobstatus=COMPLETED, execution_
time=10, end_time=40, name=’Pipeline1’}
165 Job{user=’Harry’, project=’IITD.CS.OS.ASPLOS’, jobstatus=COMPLETED, execution_
time=10, end_time=50, name=’Pipeline2’}
166 Job{user=’Carry’, project=’IITD.CS.ML.ICML’, jobstatus=COMPLETED, execution_ti
me=10, end_time=60, name=’ImageProcessing’}
167 Job{user=’Rob’, project=’IITD.CS.ML.ICML’, jobstatus=COMPLETED, execution_tim
e=10, end_time=70, name=’DeepLearning1’}
168 Job{user=’Carry’, project=’IITD.CS.ML.ICML’, jobstatus=COMPLETED, execution_ti
me=10, end_time=80, name=’ImageProcessing1’}
169 Job{user=’Rob’, project=’IITD.CS.ML.ICML’, jobstatus=COMPLETED, execution_tim
e=10, end_time=90, name=’DeepLearning2’}
170 Job{user=’Carry’, project=’IITD.CS.ML.ICML’, jobstatus=COMPLETED, execution_ti
me=10, end_time=100, name=’ImageProcessing2’}
171 Job{user=’Rob’, project=’IITD.CS.ML.ICML’, jobstatus=COMPLETED, execution_tim
e=10, end_time=110, name=’DeepLearning3’}
172 Job{user=’Harry’, project=’IITD.CS.OS.ASPLOS’, jobstatus=COMPLETED, execution_
time=10, end_time=120, name=’Pipeline3’}
173 Job{user=’Harry’, project=’IITD.CS.OS.ASPLOS’, jobstatus=COMPLETED, execution_
time=10, end_time=130, name=’Pipeline4’}
174 Job{user=’Harry’, project=’IITD.CS.OS.ASPLOS’, jobstatus=COMPLETED, execution_
time=10, end_time=140, name=’Pipeline5’}
175 Job{user=’Carry’, project=’IITD.CS.TH.SODA’, jobstatus=COMPLETED, execution_ti
me=10, end_time=150, name=’Kmeans1’}


176 Job{user=’Carry’, project=’IITD.CS.TH.SODA’, jobstatus=COMPLETED, execution_ti
me=10, end_time=160, name=’Kmeans3’}
177 Job{user=’Carry’, project=’IITD.CS.TH.SODA’, jobstatus=COMPLETED, execution_ti
me=10, end_time=170, name=’Kmeans3’}
178 Job{user=’Carry’, project=’IITD.CS.TH.SODA’, jobstatus=COMPLETED, execution_ti
me=10, end_time=180, name=’Kmeans4’}
179 Job{user=’Carry’, project=’IITD.CS.TH.SODA’, jobstatus=COMPLETED, execution_ti
me=10, end_time=190, name=’Kmeans5’}
180 ------------------------
181 Unfinished jobs:
182 Job{user=’Carry’, project=’IITD.CS.ML.ICML’, jobstatus=REQUESTED, execution_ti
me=10, end_time=null, name=’ImageProcessing3’}
183 Job{user=’Rob’, project=’IITD.CS.ML.ICML’, jobstatus=REQUESTED, execution_time
=10, end_time=null, name=’DeepLearning4’}
184 Job{user=’Carry’, project=’IITD.CS.ML.ICML’, jobstatus=REQUESTED, execution_ti
me=10, end_time=null, name=’ImageProcessing4’}
185 Job{user=’Rob’, project=’IITD.CS.ML.ICML’, jobstatus=REQUESTED, execution_time
=10, end_time=null, name=’DeepLearning5’}
186 Job{user=’Carry’, project=’IITD.CS.ML.ICML’, jobstatus=REQUESTED, execution_ti
me=10, end_time=null, name=’ImageProcessing5’}
187 Total unfinished jobs: 5
188 --------------STATS DONE---------------


