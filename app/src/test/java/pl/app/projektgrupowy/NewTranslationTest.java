package pl.app.projektgrupowy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.app.projektgrupowy.assets.NewTranslation;
import pl.app.projektgrupowy.assets.Segment;

public class NewTranslationTest {
    private static final String textValST = "sourceText";
    private static final String textValSL = "sl";
    private static final String textValTL = "tl";
    private static final String textValTitle = "title";

    @Test
    public void new_translation_isCorrect() {
        NewTranslation translation = new NewTranslation();

        translation.sourceText = textValST;
        translation.sourceLanguage = textValSL;
        translation.targetLanguage = textValTL;
        translation.title = textValTitle;

        assertEquals(textValST, translation.sourceText);

        assertEquals(textValSL, translation.sourceLanguage);

        assertEquals(textValTL, translation.targetLanguage);

        assertEquals(textValTitle, translation.title);
    }
}