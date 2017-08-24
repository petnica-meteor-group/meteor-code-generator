package rs.petnica.meteorcodegenerator;

import java.util.ArrayList;

/**
 * Created by vladi on 08.08.2017.
 */

public class CodeGen {

    // DON'T USE: A, P, S, C, K, D
    // USE: _Q_, _W_, E, R, T, Y, U, I, O, F, G, H, J, L, Z, X, V, B, N, M

    public static final String[][] GROUP_PREFIXES = {
            { "E", "R", "T" },
            { "Y", "U", "I" },
            { "M", "F", "G" },
            { "H", "J", "L" },
            { "Z", "X", "V" },
            { "B", "N", "O" }
    };

    public static final String[] ALPHABET = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    public static CodeGen getGroupCodeGen(int group) {
        return codeGens[group];
    }

    private void incCounter() {
        counter++;
        if (counter > 99) {
            prefixIndex++;
            counter = 0;

            if (prefixIndex >= GROUP_PREFIXES[group].length) {
                // Rotate codes (causes code collision)
                prefixIndex = 0;
            }
        }
    }

    private String getCurrentCode() {
        String code = GROUP_PREFIXES[group][prefixIndex];
        code += counter < 10 ? "0" + counter : "" + counter;
        return code;
    }

    public String generate() {
        String code = getCurrentCode();
        lastCode = code;

        incCounter();

        codeLogger.log(code);

        return code;
    }

    public String getLastCode() {
        return lastCode;
    }

    private static CodeGen[] codeGens = new CodeGen[GROUP_PREFIXES.length];
    static {
        for(int group = 0; group < GROUP_PREFIXES.length; group++) {
            codeGens[group] = new CodeGen(group);

            ArrayList<CodeMark> codeMarks;
            if ((codeMarks = CodeLogger.load(group)) != null) {
                String lastCodeLetter = codeMarks.get(codeMarks.size() - 1).getCode();

                for(int index = 0; index < GROUP_PREFIXES[group].length; index++) {
                    if (GROUP_PREFIXES[group][index].equals(lastCodeLetter.substring(0, 1))) {
                        codeGens[group].prefixIndex = index;
                        codeGens[group].counter = Integer.parseInt(lastCodeLetter.substring(1));
                        codeGens[group].lastCode = codeGens[group].getCurrentCode();
                        codeGens[group].incCounter();
                        break;
                    }
                }
            }
        }
    }

    private CodeGen(int group) {
        this.group = group;
        this.codeLogger = new CodeLogger(group);
    }

    private int group;

    private int prefixIndex = 0;
    private int counter = 0;

    private CodeLogger codeLogger;
    private String lastCode = " / ";

}
