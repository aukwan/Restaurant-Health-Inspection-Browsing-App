package com.cmpt276.group16.model;

public class Violations {
    private final int violNum;
    private final String severity;
    private final String description;
    //You could choose during parsing if you want the repeat to be a boolean or not
    private final String repeat;

    public Violations(int violNum, String severity, String description, String repeat){
        this.violNum = violNum;
        this.severity = severity;
        this.description = description;
        this.repeat = repeat;
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
