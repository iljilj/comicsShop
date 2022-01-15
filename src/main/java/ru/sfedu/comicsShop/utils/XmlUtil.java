package ru.sfedu.comicsShop.utils;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "root")
public class XmlUtil<T> {
    @ElementList(inline = true, required = false)
    private List<T> list;

    public XmlUtil() {
    }

    public XmlUtil(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
