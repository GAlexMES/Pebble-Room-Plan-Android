package de.brennecke.timetableroomplan.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 26.10.2015.
 */
public class TTBL {
    List<Lesson> lessonList;
    List<Integer> timesList;

    public final int LESSON_LENGTH = 90;

    public TTBL(){
        lessonList = new ArrayList<>();
        timesList = new ArrayList<>();
    }


    public void addLeson(Lesson lesson){
        lessonList.add(lesson);
        if(!timesList.contains(lesson.getStart())){
            timesList.add(lesson.getStart());
        }
    }

    public void addLessons(List<Lesson> lessonList){
        for(Lesson l : lessonList){
            addLeson(l);
        }
    }

    public List<Integer> getTimeList(){
        return timesList;
    }

    public List<Lesson> getLessonList(){
        return lessonList;
    }
}
