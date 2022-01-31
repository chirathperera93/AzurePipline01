package com.ayubo.life.ayubolife.post.model;

import java.util.List;

public class NativePost {

    private String type;
    private String id;
    private String name;
    private List<Template> template;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Template> getTemplate() {
        return template;
    }

    public void setTemplate(List<Template> template) {
        this.template = template;
    }
}
