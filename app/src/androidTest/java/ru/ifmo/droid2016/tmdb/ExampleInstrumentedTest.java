package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import ru.ifmo.droid2016.tmdb.loader.MoviesPullParser;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ru.ifmo.droid2016.tmdb", appContext.getPackageName());
    }

    @Test
    public void testJsonParser() throws Exception {
        InputStream in = new FileInputStream(new File(
                "C:\\Users\\romag_000\\AndroidStudioProjects\\homework2\\app\\src\\test\\java\\ru\\ifmo\\droid2016\\tmdb\\response.json"));
        try {
            MoviesPullParser.parseMovies(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
