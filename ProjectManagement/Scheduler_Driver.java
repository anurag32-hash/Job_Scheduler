package ProjectManagement;

import PriorityQueue.MaxHeap;
import PriorityQueue.PriorityQueueDriverCode;
import RedBlack.RBTree;
import RedBlack.RedBlackNode;
import Trie.Trie ;
import Trie.TrieNode;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;


public class Scheduler_Driver extends Thread implements SchedulerInterface {
    RBTree<Project,Job> rbtree = new RBTree<>();
    MaxHeap<Job> jobheap = new MaxHeap<>();
    Trie<Project> projecttrie = new Trie();
    Trie<User> usertrie = new Trie();
    Trie<Job> totaljobs = new Trie<Job>();
    ArrayList<Job> queuelist = new ArrayList<>();
    ArrayList<Job> unfinishedjobs = new ArrayList<>();
    int time=0;


    public static void main(String[] args) throws IOException {
        Scheduler_Driver scheduler_driver = new Scheduler_Driver();

        File file;
        if (args.length == 0) {
            URL url = PriorityQueueDriverCode.class.getResource("INP");
            file = new File(url.getPath());
        } else {
            file = new File(args[0]);
        }

        scheduler_driver.execute(file);
    }

    public void execute(File file) throws IOException {

        URL url = Scheduler_Driver.class.getResource("INP");
        file = new File(url.getPath());

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.err.println("Input file Not found. "+file.getAbsolutePath());
        }
        String st;
        while ((st = br.readLine()) != null) {
            String[] cmd = st.split(" ");
            if (cmd.length == 0) {
                System.err.println("Error parsing: " + st);
                return;
            }

            switch (cmd[0]) {
                case "PROJECT":
                    handle_project(cmd);
                    break;
                case "JOB":
                    handle_job(cmd);
                    break;
                case "USER":
                    handle_user(cmd[1]);
                    break;
                case "QUERY":
                    handle_query(cmd[1]);
                    break;
                case "":
                    handle_empty_line();
                    break;
                case "ADD":
                    handle_add(cmd);
                    break;
                default:
                    System.err.println("Unknown command: " + cmd[0]);
            }
        }


        run_to_completion();

        print_stats();

    }

    @Override
    public void run() {
        // till there are JOBS
        schedule();
    }

    @Override
    public void handle_project(String[] cmd) {
        System.out.println("Creating project");
        Project project = new Project(cmd[1],Integer.parseInt(cmd[2]),Integer.parseInt(cmd[3]));
        projecttrie.insert(cmd[1],project);

    }

    @Override
    public void handle_job(String[] cmd) {
        System.out.println("Creating job");
        TrieNode<Project> temp = projecttrie.search(cmd[2]);
        TrieNode<User> temp1 = usertrie.search((cmd[3]));
        if(temp==null)
            System.out.println("No such project exists. "+cmd[2]);
        else if(temp1==null)
            System.out.println("No such user exists: "+cmd[3]);
        if(temp != null && temp1 !=null) {
            int priority = temp.getValue().priority;
            Job job = new Job(cmd[1], temp.value, priority, cmd[3], Integer.parseInt(cmd[4]));
            jobheap.insert(job);
            totaljobs.insert(job.name,job);
        }
    }

    @Override
    public void handle_user(String name) {
        System.out.println("Creating user");
        User user = new User(name);
        usertrie.insert(name,user);
    }

    @Override
    public void handle_query(String key) {
        System.out.println("Querying");
        TrieNode<Job> job = totaljobs.search(key);
        if(job==null)
            System.out.println(key + ": NO SUCH JOB");
        else{
            if(job.value.jobstatus.compareTo("COMPLETED")==0)
                System.out.println(key + ": "+job.value.jobstatus);
            else
                System.out.println(key + ": "+"NOT FINISHED");
        }
    }

    @Override
    public void handle_empty_line() {

        System.out.println("Running code");
        System.out.println("Remaining jobs: "+jobheap.queueitems);
        while(jobheap.queueitems!=0){
            Job job = jobheap.extractMax();
            TrieNode<Project> project = projecttrie.search(job.project.name);
            if(job.runtime  <=  project.value.budget){
                System.out.println("Executing: "+job.name+" from: "+project.value.name);
                job.jobstatus="COMPLETED";
                this.time=this.time+job.runtime;
                job.end_time=this.time;
                project.value.budget=project.value.budget-job.runtime;
                System.out.println("Project: "+project.value.name+" budget remaining: "+project.value.budget);
                queuelist.add(job);
                break;
            }
            else{
                rbtree.insert(project.value,job);
                System.out.println("Executing: "+job.name+" from: "+project.value.name);
                System.out.println("Un-sufficient budget.");
                job.jobstatus="REQUESTED";
                unfinishedjobs.add(job);
            }
        }
        System.out.println("Execution cycle completed");
    }


    @Override
    public void run_to_completion() {

        while (jobheap.queueitems != 0) {
            System.out.println("Running code");
            System.out.println("Remaining jobs: " + jobheap.queueitems);
            Job job = jobheap.extractMax();
            TrieNode<Project> project = projecttrie.search(job.project.name);

            while (project.value.budget < job.runtime) {
                System.out.println("Executing: " + job.name + " from: " + project.value.name);
                System.out.println("Un-sufficient budget.");
                job.jobstatus = "REQUESTED";
                unfinishedjobs.add(job);
                rbtree.insert(project.value, job);
                job = jobheap.extractMax();
                project=projecttrie.search(job.project.name);
            }
            System.out.println("Executing: " + job.name + " from: " + project.value.name);
            job.jobstatus = "COMPLETED";
            this.time = this.time + job.runtime;
            job.end_time = this.time;
            project.value.budget = project.value.budget - job.runtime;
            System.out.println("Project: " + project.value.name + " budget remaining: " + project.value.budget);
            queuelist.add(job);
            System.out.println("System execution completed");
        }
    }




    @Override
    public void handle_add(String[] cmd) {
        System.out.println("ADDING Budget");
        TrieNode<Project> project = projecttrie.search(cmd[1]);
        if(project==null) {
            System.out.println("No such projects exists. "+ cmd[1]);
            return;
        }
        else {
            Project p = project.getValue();
            p.budget = p.budget+Integer.parseInt(cmd[2]);
            RedBlackNode<Project,Job> r = rbtree.search(p);
            for(int i=r.value.size()-1;i>=0;i--){
                jobheap.reinsert(r.value.get(i));
                unfinishedjobs.remove(r.value.get(i));
                r.value.remove(r.value.get(i));
            }
        }
    }

    @Override
    public void print_stats() {
        System.out.println("--------------STATS---------------");
        System.out.println("Total jobs done: "+queuelist.size());
        for(int i=0;i<queuelist.size();i++){
            Job job = queuelist.get(i);
            System.out.println("Job{user='"+job.user+"', project='"+job.project.name+"', jobstatus="+job.jobstatus+", execution_time="+job.runtime+", end_time="+job.end_time+", name='"+job.name+"'}");
        }
        System.out.println("------------------------");
        System.out.println("Unfinished jobs: ");
        for(int i=0;i<unfinishedjobs.size();i++){
            Job job = unfinishedjobs.get(i);
            System.out.println("Job{user='"+job.user+"', project='"+job.project.name+"', jobstatus="+job.jobstatus+", execution_time="+job.runtime+", end_time=null"+", name='"+job.name+"'}");
        }
        System.out.println("Total unfinished jobs: "+unfinishedjobs.size());
        System.out.println("--------------STATS DONE---------------");
    }

    @Override
    public void schedule() {

    }
}
