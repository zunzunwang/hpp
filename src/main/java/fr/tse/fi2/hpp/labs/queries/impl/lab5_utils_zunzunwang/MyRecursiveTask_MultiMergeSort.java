package fr.tse.fi2.hpp.labs.queries.impl.lab5_utils_zunzunwang;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
    
    
public class MyRecursiveTask_MultiMergeSort extends RecursiveTask<int[]> {

    private long work_longueur = 0;
    int[] list;

    public MyRecursiveTask_MultiMergeSort(int[] list) {
        this.work_longueur = list.length;
        this.list=list;
    }

    protected int[] compute() {

        //if work is above threshold, break tasks up into smaller tasks
        if(this.work_longueur >20) {
            System.out.println("Splitting workLoad : " + this.work_longueur);

            List< MyRecursiveTask_MultiMergeSort> subtasks =
            new ArrayList< MyRecursiveTask_MultiMergeSort>();
            subtasks.addAll(createSubtasks());

            for(MyRecursiveTask_MultiMergeSort subtask : subtasks){
                subtask.fork();
            }

            long result = 0;
            for(MyRecursiveTask_MultiMergeSort subtask : subtasks) {
                result += subtask.join();
            }
            return result;

        } else {
            System.out.println("Doing workLoad myself: " + this.workLoad);
            return workLoad * 3;
        }
    }

    private List<MyRecursiveTask_MultiMergeSort> createSubtasks() {
        List<MyRecursiveTask_MultiMergeSort> subtasks =
        new ArrayList<MyRecursiveTask_MultiMergeSort>();

        MyRecursiveTask_MultiMergeSort subtask1 = new MyRecursiveTask_MultiMergeSort(this.work_longueur / 2);
        MyRecursiveTask_MultiMergeSort subtask2 = new MyRecursiveTask_MultiMergeSort(this.workLoad / 2);

        subtasks.add(subtask1);
        subtasks.add(subtask2);

        return subtasks;
    }
}