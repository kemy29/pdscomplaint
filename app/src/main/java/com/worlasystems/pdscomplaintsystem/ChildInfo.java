package com.worlasystems.pdscomplaintsystem;

//1 ChildInfo
//for ChildInfo you have to crete one XMl file(expandable_child_item.xml)
//inside XML file 2 TextView(1 is for sequence number And 1 is for item)

public class ChildInfo {
    private String sequence = "";
    private String name = "";

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
