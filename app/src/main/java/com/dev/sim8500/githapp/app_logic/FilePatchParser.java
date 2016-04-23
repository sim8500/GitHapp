package com.dev.sim8500.githapp.app_logic;

import com.dev.sim8500.githapp.models.FileLineModel;

import java.io.File;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sbernad on 18.04.16.
 */
public class FilePatchParser {

    String[] patchLines = null;

    protected class PatchLineInfo {
        public int patchIndex;
        public int fileOffset;
        public int patchRange;
        public String patchSuffixLine;
    }

    public FilePatchParser(String patch) {

        if(patch == null)
            throw new InvalidParameterException("Input string cannot be null.");

        patchLines = patch.split("\n");
    }

    public List<FileLineModel> parsePatchString() throws ParseException {

        List<FileLineModel> parsedLines = new ArrayList<>();

        PatchLineInfo patchInfo = findNextPatchStart(0);
        while(patchInfo != null) {

            int patchIndex = patchInfo.patchIndex;
            final String patchEntry = patchLines[patchIndex];

            String patchLine = null;
            if(!patchInfo.patchSuffixLine.isEmpty()) {
                patchLine = patchInfo.patchSuffixLine;
            }
            else {
                ++patchIndex;

                patchLine = tryToGetNextLine(patchIndex, patchEntry);
            }

            while(patchInfo.patchRange > 0) {

                if(patchLine.startsWith("-")) {
                    // removed line
                    FileLineModel flm = new FileLineModel();
                    flm.lineNumber = patchInfo.fileOffset;
                    flm.lineContent = patchLine;
                    flm.lineStatus = FileLineModel.PATCH_STATUS_DELETED;
                    parsedLines.add(flm);
                }
                else if(patchLine.startsWith("+")) {
                    // added line
                    --patchInfo.patchRange;
                    FileLineModel flm = new FileLineModel();
                    flm.lineNumber = patchInfo.fileOffset++;
                    flm.lineContent = patchLine;
                    flm.lineStatus = FileLineModel.PATCH_STATUS_ADDED;
                    parsedLines.add(flm);
                }
                else {
                    --patchInfo.patchRange;
                    ++patchInfo.fileOffset;
                }

                if(patchInfo.patchRange > 0) {
                    ++patchIndex;

                    patchLine = tryToGetNextLine(patchIndex, patchEntry);
                }
            }

            patchInfo = findNextPatchStart(patchIndex);
        }

        return parsedLines;
    }

    protected String tryToGetNextLine(int patchIndex, final String patchEntry) throws ParseException {

        if(patchIndex >= patchLines.length)
            throw new ParseException(String.format("Missing lines for patch entry: %s", patchEntry), patchIndex);

        return patchLines[patchIndex];
    }

    protected PatchLineInfo findNextPatchStart(int startIndex) {

        PatchLineInfo result = null;
        if(startIndex >= patchLines.length) {
            return null;
        }

        int resIndex = startIndex;

        Pattern p = Pattern.compile("^\\Q@@ -\\E(\\d+),(\\d+)\\s\\+(\\d+),(\\d+)\\Q @@\\E(.*)");
        Matcher matcher = p.matcher(patchLines[resIndex]);
        while(resIndex < patchLines.length && !matcher.matches())
        {
            ++resIndex;
            if(resIndex < patchLines.length) {
                matcher = p.matcher(patchLines[resIndex]);
            }
        }

        if(resIndex < patchLines.length) {
            result = new PatchLineInfo();
            result.patchIndex = resIndex;
            result.fileOffset = Integer.valueOf(matcher.group(3));
            result.patchRange = Integer.valueOf(matcher.group(4));
            result.patchSuffixLine = matcher.group(5);
        }

        return result;
    }
}
