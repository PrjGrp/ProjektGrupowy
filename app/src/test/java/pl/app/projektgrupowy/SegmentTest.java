package pl.app.projektgrupowy;

import org.junit.Test;

import static org.junit.Assert.*;

import pl.app.projektgrupowy.assets.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SegmentTest {

    @Test
    public void segment_raw_isCorrect() {
        Segment segment = new Segment(1,"pewien tekst źródłowy");
        assertEquals("<trans-unit id=\"1\" xmlns:sap=\"urn:x-sap:sls-mlt\">" +
                        "<source>pewien tekst źródłowy</source><target></target></trans-unit>",
                segment.toString());

        segment.translate("some source text");
        assertEquals("<trans-unit id=\"1\" xmlns:sap=\"urn:x-sap:sls-mlt\">" +
                        "<source>pewien tekst źródłowy</source><target>some source text</target></trans-unit>",
                segment.toString());

        segment.translate("some source text, next try");
        assertEquals("<trans-unit id=\"1\" xmlns:sap=\"urn:x-sap:sls-mlt\">" +
                        "<source>pewien tekst źródłowy</source><target>some source text, next try</target></trans-unit>",
                segment.toString());
    }
}