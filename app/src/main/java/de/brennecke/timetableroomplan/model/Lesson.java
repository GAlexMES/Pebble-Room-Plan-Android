package de.brennecke.timetableroomplan.model;

/**
 * Created by Alexander on 26.10.2015.
 */
public class Lesson {

    private String subject;
    private int dayOfWeek;
    private int lesson;
    private int start;
    private int end;
    private String location;
    private int weekmode;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getWeekmode() {
        return weekmode;
    }

    public void setWeekmode(int weekmode) {
        this.weekmode = weekmode;
    }
}
