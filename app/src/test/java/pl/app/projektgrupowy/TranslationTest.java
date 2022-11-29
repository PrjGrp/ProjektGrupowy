package pl.app.projektgrupowy;

import org.junit.Test;

import static org.junit.Assert.*;

import pl.app.projektgrupowy.assets.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TranslationTest {
    public Translation testTranslation = new Translation("Tytuł przykładowy", "To jest tłumaczenie, chcę sprawdzić, czy dobrze działa. A zwłaszcza, czy dobrze segmentuje.", "pl-PL", "en-GB");

    /*

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

    @Test
    public void translation_segmentation_length_isCorrect() {
        assertEquals(2, testTranslation.segments.length);
    }

    @Test
    public void translation_segmentation_segments_areCorrect() {
        assertEquals("<trans-unit id=\"0\" xmlns:sap=\"urn:x-sap:sls-mlt\"><source>" +
                "To jest tłumaczenie, chcę sprawdzić, czy dobrze działa." +
                "</source><target></target></trans-unit>", testTranslation.segments[0].toString());
        assertEquals("<trans-unit id=\"1\" xmlns:sap=\"urn:x-sap:sls-mlt\"><source>" +
                " A zwłaszcza, czy dobrze segmentuje." +
                "</source><target></target></trans-unit>", testTranslation.segments[1].toString());
    }

    @Test
    public void translation_toString_before_translating_isCorrect() {
        StringBuilder translationString = new StringBuilder();

        translationString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xliff xmlns=\"urn:oasis:names:tc:xliff:document:1.2\" version=\"1.2\">" +
                "<file datatype=\"plaintext\" original=\"Tytuł przykładowy\" source-language=\"en\" " +
                "target-language=\"de\"><body>");
        translationString.append("<trans-unit id=\"0\" xmlns:sap=\"urn:x-sap:sls-mlt\"><source>" +
                "To jest tłumaczenie, chcę sprawdzić, czy dobrze działa." +
                "</source><target></target></trans-unit>");
        translationString.append("<trans-unit id=\"1\" xmlns:sap=\"urn:x-sap:sls-mlt\"><source>" +
                " A zwłaszcza, czy dobrze segmentuje." +
                "</source><target></target></trans-unit>");
        translationString.append("</body></file></xliff>");

        assertEquals(translationString.toString(), testTranslation.toString());
    }

    @Test
    public void translation_toString_after_translating_isCorrect() {
        StringBuilder translationString = new StringBuilder();

        translationString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xliff xmlns=\"urn:oasis:names:tc:xliff:document:1.2\" version=\"1.2\">" +
                "<file datatype=\"plaintext\" original=\"Tytuł przykładowy\" source-language=\"en\" " +
                "target-language=\"de\"><body>");
        translationString.append("<trans-unit id=\"0\" xmlns:sap=\"urn:x-sap:sls-mlt\"><source>" +
                "To jest tłumaczenie, chcę sprawdzić, czy dobrze działa." +
                "</source><target>This is a translation, I want to check if it works correctly.</target></trans-unit>");
        translationString.append("<trans-unit id=\"1\" xmlns:sap=\"urn:x-sap:sls-mlt\"><source>" +
                " A zwłaszcza, czy dobrze segmentuje." +
                "</source><target> Especially, if the segmentation is done right.</target></trans-unit>");
        translationString.append("</body></file></xliff>");

        testTranslation.segments[0].translate("This is a translation, I want to check if it works correctly.");
        testTranslation.segments[1].translate(" Especially, if the segmentation is done right.");

        assertEquals(translationString.toString(), testTranslation.toString());
    }

    @Test
    public void translation_getTargetText_isCorrect() {
        assertEquals("This is a translation, I want to check if it works correctly. " +
                "Especially, if the segmentation is done right.", testTranslation.getTargetText());
    }


    */
}