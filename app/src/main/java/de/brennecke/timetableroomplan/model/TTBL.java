package de.brennecke.timetableroomplan.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexander on 26.10.2015.
 */
public class TTBL {
    List<Lesson> lessonList;

    Map<Integer, Integer> times;
    public final int LESSON_LENGTH = 90;

    public TTBL(){
        lessonList = new ArrayList<>();

        times = new HashMap<>();
        times.put(0,490);
        times.put(1,600);
        times.put(2,710);
        times.put(3,820);
        times.put(4,930);
    }


    public void addLeson(Lesson lesson){
        lessonList.add(lesson);
    }

    public void addLessons(List<Lesson> lessonList){
        for(Lesson l : lessonList){
            addLeson(l);
        }
    }

    public List<Lesson> getLessonList(){
        return lessonList;
    }
}
