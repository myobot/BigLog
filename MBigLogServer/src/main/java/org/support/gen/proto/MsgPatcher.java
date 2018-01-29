package org.support.gen.proto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wangzhanwei
 */
public class MsgPatcher {
    private static final String PATCH_PATTERN_1 = "int mutable_bitField0_";
    private static final String REF_PATTERN_1 = "mutable_bitField0_";
    private static final String PATCH_PATTERN_2 = "int mutable_bitField1_";
    private static final String REF_PATTERN_2 = "mutable_bitField1_";
    private static final String PATCH_PATTERN_3 = "int from_bitField0_";
    private static final String REF_PATTERN_3 = "from_bitField0_";
    private static final String PATCH_PATTERN_4 = "int from_bitField1_";
    private static final String REF_PATTERN_4 = "from_bitField1_";

    public MsgPatcher() {
    }

    private static void patchPatternDefineOne(BufferedReader bufferedReader, OutputStreamWriter writer, String currentLine, String refPattern) throws IOException {
        List<String> codeSnippet = new ArrayList();
        String line = null;
        boolean isUnused = true;

        while ((line = bufferedReader.readLine()) != null) {
            codeSnippet.add(line);
            if (line.contains(refPattern)) {
                isUnused = false;
                break;
            }

            if (line.equals("    }")) {
                break;
            }
        }

        if (!isUnused) {
            writer.write(currentLine + "\n");
        }

        Iterator var7 = codeSnippet.iterator();

        while (var7.hasNext()) {
            String codeLine = (String) var7.next();
            writer.write(codeLine + "\n");
        }

    }

    private static void patchPatternDefineTwo(BufferedReader bufferedReader, OutputStreamWriter writer, String currentLine, String refPatternFirst, String patchPatternSecond, String refPatternSecond) throws IOException {
        List<String> codeSnippet = new ArrayList();
        String nextLine = bufferedReader.readLine();
        boolean isUnused1 = true;
        boolean isUnused2 = false;
        if (nextLine != null && nextLine.contains(patchPatternSecond)) {
            isUnused2 = true;
        }

        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            codeSnippet.add(line);
            if (isUnused1 && line.contains(refPatternFirst)) {
                isUnused1 = false;
            } else if (isUnused2 && line.contains(refPatternSecond)) {
                isUnused2 = false;
            } else if (line.equals("    }")) {
                break;
            }

            if (!isUnused1 && !isUnused2) {
                break;
            }
        }

        if (!isUnused1) {
            writer.write(currentLine + "\n");
        }

        if (!isUnused2) {
            writer.write(nextLine + "\n");
        }

        Iterator var11 = codeSnippet.iterator();

        while (var11.hasNext()) {
            String codeLine = (String) var11.next();
            writer.write(codeLine + "\n");
        }

    }

    public static void main(String[] args) throws Exception {
        String path;
        if (args.length == 0) {
            path = "org/gen";
        } else {
            path = args[0];
        }
        File f = new File(path);
        File[] sources = f.listFiles();
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        OutputStreamWriter writer = null;
        File[] var6 = sources;
        int var7 = sources.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            File source = var6[var8];
            System.out.println("patching " + source.toString());
            File sourceNew = new File(source.toString() + ".tmp");
            sourceNew.createNewFile();

            try {
                reader = new InputStreamReader(new FileInputStream(source), "UTF-8");
                bufferedReader = new BufferedReader(reader);
                writer = new OutputStreamWriter(new FileOutputStream(sourceNew), "UTF-8");
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("int mutable_bitField0_")) {
                        patchPatternDefineTwo(bufferedReader, writer, line, "mutable_bitField0_", "int mutable_bitField1_", "mutable_bitField1_");
                    } else if (line.contains("int from_bitField0_")) {
                        patchPatternDefineTwo(bufferedReader, writer, line, "from_bitField0_", "int from_bitField1_", "from_bitField1_");
                    } else {
                        writer.write(line + "\n");
                    }
                }
            } finally {
                writer.close();
                bufferedReader.close();
                reader.close();
            }

            source.delete();
            sourceNew.renameTo(source);
        }

    }
}
