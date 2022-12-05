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
    private static final String sourceText = "To jest tłumaczenie, chcę sprawdzić, czy dobrze działa." +
            " A zwłaszcza, czy dobrze segmentuje.";

    private static final String targetText = "This is a translation, I want to check if it works correctly." +
            " Especially, if the segmentation is done right.";

    private static final String translationSegment0 = "This is a translation, I want to check if it works correctly.";

    private static final String translationSegment1 = " Especially, if the segmentation is done right.";

    private static final String translationHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<xliff xmlns=\"urn:oasis:names:tc:xliff:document:1.2\" version=\"1.2\">" +
            "<file datatype=\"plaintext\" original=\"Tytuł przykładowy\" source-language=\"pl-PL\" " +
            "target-language=\"en-US\"><body>";

    private static final String translationFooter = "</body></file></xliff>";

    private static final Translation testTranslation = new Translation("Tytuł przykładowy",
            sourceText, Translation.POLISH, Translation.AMERICAN_ENGLISH);

    private static final String segment0BeforeTrans = "<trans-unit id=\"0\" xmlns:sap=\"urn:x-sap:sls-mlt\"><source>" +
            "To jest tłumaczenie, chcę sprawdzić, czy dobrze działa.</source><target></target></trans-unit>";

    private static final String segment1BeforeTrans = "<trans-unit id=\"1\" xmlns:sap=\"urn:x-sap:sls-mlt\"><source>" +
            " A zwłaszcza, czy dobrze segmentuje.</source><target></target></trans-unit>";

    private static final String segment0AfterTrans = "<trans-unit id=\"0\" xmlns:sap=\"urn:x-sap:sls-mlt\"><source>" +
            "To jest tłumaczenie, chcę sprawdzić, czy dobrze działa.</source>" +
            "<target>" + translationSegment0 + "</target></trans-unit>";

    private static final String segment1AfterTrans = "<trans-unit id=\"1\" xmlns:sap=\"urn:x-sap:sls-mlt\"><source>" +
            " A zwłaszcza, czy dobrze segmentuje.</source>" +
            "<target>" + translationSegment1 + "</target></trans-unit>";

    @Test
    public void translation_segmentation_length_isCorrect() {
        assertEquals(2, testTranslation.segments.length);
    }

    @Test
    public void translation_segmentation_segments_areCorrect() {
        assertEquals(segment0BeforeTrans, testTranslation.segments[0].toString());
        assertEquals(segment1BeforeTrans, testTranslation.segments[1].toString());
    }

    @Test
    public void translation_toString_before_translating_isCorrect() {
        StringBuilder translationString = new StringBuilder();

        translationString.append(translationHeader);
        translationString.append(segment0BeforeTrans);
        translationString.append(segment1BeforeTrans);
        translationString.append(translationFooter);

        assertEquals(translationString.toString(), testTranslation.toString());
    }

    @Test
    public void translation_toString_after_translating_isCorrect() {
        StringBuilder translationString = new StringBuilder();

        translationString.append(translationHeader);
        translationString.append(segment0AfterTrans);
        translationString.append(segment1AfterTrans);
        translationString.append(translationFooter);

        testTranslation.segments[0].translate(translationSegment0);
        testTranslation.segments[1].translate(translationSegment1);

        assertEquals(translationString.toString(), testTranslation.toString());
    }

    @Test
    public void translation_getTargetText_isCorrect() {
        assertEquals(targetText, testTranslation.getTargetText());
    }

    @Test
    public void translation_parseXliff_untranslated_isCorrect() {
        StringBuilder translationString = new StringBuilder();

        translationString.append(translationHeader);
        translationString.append(segment0BeforeTrans);
        translationString.append(segment1BeforeTrans);
        translationString.append(translationFooter);

        Translation translationParsed = Translation.parseXliff(translationString.toString());

        assertEquals(testTranslation.toString(), translationParsed.toString());

    }

    @Test
    public void translation_parseXliff_translated_isCorrect() {
        StringBuilder translationString = new StringBuilder();

        translationString.append(translationHeader);
        translationString.append(segment0AfterTrans);
        translationString.append(segment1AfterTrans);
        translationString.append(translationFooter);

        testTranslation.segments[0].translate(translationSegment0);
        testTranslation.segments[1].translate(translationSegment1);

        Translation translationParsed = Translation.parseXliff(translationString.toString());

        assertEquals(testTranslation.toString(), translationParsed.toString());
    }

    @Test
    public void translation_parseXliff_source_isCorrect() {
        StringBuilder translationString = new StringBuilder();

        translationString.append(translationHeader);
        translationString.append(segment0AfterTrans);
        translationString.append(segment1AfterTrans);
        translationString.append(translationFooter);

        testTranslation.segments[0].translate(translationSegment0);
        testTranslation.segments[1].translate(translationSegment1);

        Translation translationParsed = Translation.parseXliff(translationString.toString());

        assertEquals(testTranslation.getSourceText(), translationParsed.getSourceText());
    }


}