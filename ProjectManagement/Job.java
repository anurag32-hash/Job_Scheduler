package ProjectManagement;

import RedBlack.RedBlackNode;

public class Job implements Comparable<Job> {
    public String name,user;
    public String jobstatus = "REQUESTED";
    public int end_time=0;
    public Project project;
    public int priority,runtime;

    public Job(String name,Project project,int priority,String user,int runtime){
        this.name=name;
        this.project=project;
        this.priority=priority;
        this.user=user;
        this.runtime=runtime;
    }
    @Override
    public int compareTo(Job job) {
        if(this.priority > job.priority){
            return 1;
        }
        else if(this.priority < job.priority){
            return -1;
        }
        else
            return 0;
    }
}