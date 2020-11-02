package com.cmpt276.group16.model;

public class Violation {
    private int iconID;
    private int violNum;
    private String severity;
    private String description;
    //You could choose during parsing if you want the repeat to be a boolean or not
    private String repeat;

    public Violation(int violNum, String severity, String description, String repeat){
        this.violNum = violNum;
        this.severity = severity;
        this.description = description;
        this.repeat = repeat;
    }

    public int getIconID() {
        return iconID;
    }

    public int getViolNum() {
        return violNum;
    }

    public String getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return violNum +
                ", " + severity +
                ", " + description +
                ", " + repeat;
    }
}
