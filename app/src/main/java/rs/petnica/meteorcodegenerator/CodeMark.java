package rs.petnica.meteorcodegenerator;

import java.util.Date;

/**
 * Created by vladi on 08.08.2017.
 */

public class CodeMark {

    public CodeMark(String code, String time) {
        this.code = code;
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public String getTime() {
        return time;
    }

    private String code;
    private String time;
}
