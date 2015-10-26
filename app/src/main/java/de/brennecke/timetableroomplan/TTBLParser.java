package de.brennecke.timetableroomplan;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.brennecke.timetableroomplan.model.Lesson;
import de.brennecke.timetableroomplan.model.TTBL;

/**
 * Created by Alexander on 26.10.2015.
 */
public class TTBLParser {

    private String filePath;
    private InputStream in;
    private static final String NAMESPACE = null;

    public TTBLParser(String filePath, String fileName, Context context) throws IOException {
        this.filePath = filePath;
        try {
            File file = new File(filePath, fileName);
            in = new FileInputStream(file);
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    public TTBL getTTBL() throws XmlPullParserException, IOException {
        TTBL retval = new TTBL();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readTTBL(parser);
        } finally {
            in.close();
        }
    }

    private TTBL readTTBL(XmlPullParser parser) throws XmlPullParserException, IOException {
        TTBL retval = new TTBL();

        parser.require(XmlPullParser.START_TAG, NAMESPACE, "ttbl");
        while (parser.next() != XmlPullParser.END_TAG) {
            String name = parser.getName();
            Log.d("parsing", name);
            if (name.equals("timetable")) {
                retval.addLessons(getTimetable(parser));
            }

        }
        return retval;
    }

    private List<Lesson> getTimetable(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Lesson> retval = new ArrayList();

        parser.require(XmlPullParser.START_TAG, NAMESPACE, "timetable");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("lessons")) {
                retval.addAll(readLessons(parser));
            } else {
                skip(parser);
            }
        }
        return retval;
    }

    private List<Lesson> readLessons(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d("parsing", "start lesson parsing");
        List<Lesson> retval = new ArrayList();

        parser.require(XmlPullParser.START_TAG, NAMESPACE, "lessons");
        while (parser.next() != XmlPullParser.END_TAG) {
            String name = parser.getName();
            if (name.equals("item")) {
                retval.add(getLesson(parser));
            } else {
                skip(parser);
            }
        }
        return retval;
    }

    private Lesson getLesson(XmlPullParser parser) throws IOException, XmlPullParserException {
        Lesson retval = new Lesson();

        parser.require(XmlPullParser.START_TAG, NAMESPACE, "item");
        String tag = parser.getName();
        if (tag.equals("item")) {
            String subjectType = parser.getAttributeValue(null, "subject");
            int dayOfWeekType = Integer.valueOf(parser.getAttributeValue(null, "dayOfWeek"));
            int lessonType = Integer.valueOf(parser.getAttributeValue(null, "lesson"));
            int startType = Integer.valueOf(parser.getAttributeValue(null, "start"));
            int endType = Integer.valueOf(parser.getAttributeValue(null, "end"));
            String locationType = parser.getAttributeValue(null, "location");
            int weekmodeType = Integer.valueOf(parser.getAttributeValue(null, "weekmode"));

            retval.setSubject(subjectType);
            retval.setDayOfWeek(dayOfWeekType);
            retval.setLesson(lessonType);
            retval.setStart(startType);
            retval.setEnd(endType);
            retval.setLocation(locationType);
            retval.setWeekmode(weekmodeType);
        }
        parser.next();
        parser.require(XmlPullParser.END_TAG, NAMESPACE, "item");
        return retval;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
