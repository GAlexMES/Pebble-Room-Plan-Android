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

    Map<Integer,Map<Integer,Map<Integer,String>>> sortedLocationList;
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

        initSortedList();
    }

    private void initSortedList(){
        sortedLocationList = new HashMap<>();
        for(int i = 0; i<2;i++){
            sortedLocationList.put(i,new HashMap<Integer, Map<Integer, String>>());
            for(int d = 2; d<7;d++) {
                sortedLocationList.get(i).put(d,new HashMap<Integer, String>());
            }
        }
    }

    public void addLeson(Lesson lesson){
        lessonList.add(lesson);
        int weekmode = lesson.getWeekmode();
        int dayOfWeek = lesson.getDayOfWeek();
        int lessonOfDay = lesson.getLesson();
        String location = lesson.getLocation();

        sortedLocationList.get(weekmode).get(dayOfWeek).put(lessonOfDay,location);
    }

    public void addLessons(List<Lesson> lessonList){
        for(Lesson l : lessonList){
            addLeson(l);
        }
    }
}
