package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.MoviesPullParser;
import ru.ifmo.droid2016.tmdb.model.Movie;

import static org.junit.Assert.assertEquals;
import static ru.ifmo.droid2016.tmdb.R.raw.response;

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

        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        InputStream in = resources.openRawResource(response);

        List<Movie> movies = MoviesPullParser.parseMovies(in);
        for (Movie m : movies) {
            System.out.println(String.format("%s %s %s %s", m.posterPath, m.originalTitle,
                    m.overviewText, m.localizedTitle));
        }
    }
}
