import com.dev.sim8500.githapp.app_logic.FilePatchParser;
import com.dev.sim8500.githapp.models.FileLineModel;

import junit.framework.Assert;

import org.junit.Test;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by sbernad on 23.04.16.
 */
public class FilePatchParserTest {

    @Test
    public void shouldThrowExceptionOnNullInput() {
        try {
            FilePatchParser parser = new FilePatchParser(null);
            Assert.fail("FilePatchParser does not throw exception on null input.");
        }
        catch(InvalidParameterException ex) {
            Assert.assertEquals("Input string cannot be null.", ex.getMessage());
        }
    }
    @Test
    public void shouldReturnEmptyListOnEmptyInput() {

        //Given...
        FilePatchParser parserEmpty = new FilePatchParser("");
        FilePatchParser parserEmptyLines = new FilePatchParser("\n\n\n");

        List<FileLineModel> resultEmpty = null;
        List<FileLineModel> resultEmptyLines = null;
        //When...
        try {
            resultEmpty = parserEmpty.parsePatchString();
            resultEmptyLines = parserEmptyLines.parsePatchString();
        }
        catch (ParseException ex) {
            Assert.fail(ex.getMessage());
        }
        //Then...
        validateIfListIsEmpty(resultEmpty);
        validateIfListIsEmpty(resultEmptyLines);
    }

    @Test
    public void shouldReturnEmptyListOnNoPatch() {

        //Given...
        FilePatchParser parserNoPatch = new FilePatchParser("\t blablabla\n+\tqwertyz zytrewq\n-lorem_ipsum\n" +
                                                            "@@ , , @@ \n@@ a,a b,b @@\n\t\tblablabla\n ...");

        List<FileLineModel> resultNoPatch = null;
        //When...
        try {
            resultNoPatch = parserNoPatch.parsePatchString();
        }
        catch (ParseException ex) {
            Assert.fail(ex.getMessage());
        }

        //Then...
        validateIfListIsEmpty(resultNoPatch);
    }

    @Test
    public void shouldThrowExceptionOnInvalidPatch() {
        //Given...
        FilePatchParser parser = new FilePatchParser("@@ -20,6 +20,6 @@\n" +
                "\t\tpublic void testTestTest {\n" +
                "\n" +
                "\n" +
                "-\n" +
                "-\t\tblablabla\n");

        //When...
        try {
            parser.parsePatchString();
            Assert.fail("FilePatchParser does not throw exception on invalid input.");
        }
        catch (ParseException ex) {

        }
    }

    @Test
    public void shouldReturnValidList() {
        //Given...
        FilePatchParser parserValidList = new FilePatchParser("@@ -20,6 +20,6 @@\n" +
                                                              "\t\tpublic void testTestTest {\n" +
                                                              "\n" +
                                                              "\n" +
                                                              "-\n" +
                                                              "-\t\tblablabla\n" +
                                                              "-} \n" +
                                                              "+}\t//test test\n" +
                                                              "+ public void somethingSomething {\n" +
                                                              "+return; }\n" +
                                                              "@@ -44,2 +46,4 @@ public Fragment somethingSomething {\n" +
                                                              "+\t\t\t//test\n" +
                                                              "- //irrelevant comment\n" +
                                                              "+public void somethingNew {\n" +
                                                              "+\t\t\tfail(); }");

        //When...
        List<FileLineModel> validList = null;
        try {
            validList = parserValidList.parsePatchString();
        }
        catch (ParseException ex) {
            Assert.fail(ex.getMessage());
        }

        //Then...
        validateFileLineModel(validList.get(0), 23, "-", FileLineModel.PATCH_STATUS_DELETED);
        validateFileLineModel(validList.get(1), 23, "-\t\tblablabla", FileLineModel.PATCH_STATUS_DELETED);
        validateFileLineModel(validList.get(2), 23, "-} ", FileLineModel.PATCH_STATUS_DELETED);
        validateFileLineModel(validList.get(3), 23, "+}\t//test test", FileLineModel.PATCH_STATUS_ADDED);
        validateFileLineModel(validList.get(4), 24, "+ public void somethingSomething {", FileLineModel.PATCH_STATUS_ADDED);
        validateFileLineModel(validList.get(5), 25, "+return; }", FileLineModel.PATCH_STATUS_ADDED);

        validateFileLineModel(validList.get(6), 47, "+\t\t\t//test", FileLineModel.PATCH_STATUS_ADDED);
        validateFileLineModel(validList.get(7), 48, "- //irrelevant comment", FileLineModel.PATCH_STATUS_DELETED);
        validateFileLineModel(validList.get(8), 48, "+public void somethingNew {", FileLineModel.PATCH_STATUS_ADDED);
        validateFileLineModel(validList.get(9), 49, "+\t\t\tfail(); }", FileLineModel.PATCH_STATUS_ADDED);
    }

    private void validateIfListIsEmpty(List<FileLineModel> list) {

        Assert.assertNotNull(list);
        Assert.assertTrue(list.isEmpty());
    }

    private void validateFileLineModel(FileLineModel fileLine, int lineNumber, String lineContent, @FileLineModel.PatchStatus int lineStatus) {

        Assert.assertNotNull(fileLine);
        Assert.assertEquals(lineNumber, fileLine.lineNumber);
        Assert.assertEquals(lineContent, fileLine.lineContent);
        Assert.assertEquals(lineStatus, fileLine.lineStatus);
    }
}
