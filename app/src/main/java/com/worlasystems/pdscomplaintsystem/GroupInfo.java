package com.worlasystems.pdscomplaintsystem;

//2 GroupInfo
//for GroupInfo you have to crete one XMl file(expandable_group_item.xml)
//inside XML file 1 TextView


import java.util.ArrayList;

class GroupInfo {
    private String name;
    private ArrayList<ChildInfo> list = new ArrayList<ChildInfo>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ChildInfo> getProductList() {
        return list;
    }

    public void setProductList(ArrayList<ChildInfo> productList) {
        this.list = productList;
    }
}





