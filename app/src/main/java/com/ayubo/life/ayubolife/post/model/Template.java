package com.ayubo.life.ayubolife.post.model;

import java.util.ArrayList;
import java.util.List;

public class Template {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public class MediaTemplate extends Template {
        String link;
        String thumb;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }
    }


    public class TextTemplate extends Template {
        String text;
        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
    }

    public class ListTemplate extends Template {
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

    public class PriceTemplate extends Template {
        private String label;
        private String amount;
        private String currency;
        private String symbol;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
    }

    public class TermTemplate extends Template {
        private String link;
        private boolean required;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }
    }

    public class TermAndConditionTemplate extends Template {
        private String terms_action;
        private String terms_meta;
        List<ButtonData> button_array;

        public String getTerms_action() {
            return terms_action;
        }

        public void setTerms_action(String terms_action) {
            this.terms_action = terms_action;
        }

        public String getTerms_meta() {
            return terms_meta;
        }

        public void setTerms_meta(String terms_meta) {
            this.terms_meta = terms_meta;
        }

        public List<ButtonData> getList() {
            return button_array;
        }

        public void setList(List<ButtonData> list) {
            this.button_array = list;

        }

    }

    public class ButtonTemplate extends Template {
        List<ButtonData> list;

        public List<ButtonData> getList() {
            return list;
        }

        public void setList(List<ButtonData> list) {
            this.list = list;
        }
    }

    public class ButtonData {
        private String label;
        private String action;
        private String type;
        private String status;
        private String meta;

        public String getMeta() {
            return meta;
        }

        public void setMeta(String meta) {
            this.meta = meta;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class TableTemplate extends Template {
        List<String> head;
        ArrayList<List<String>> body;

        public List<String> getHead() {
            return head;
        }

        public void setHead(List<String> head) {
            this.head = head;
        }

        public ArrayList<List<String>> getBody() {
            return body;
        }

        public void setBody(ArrayList<List<String>> body) {
            this.body = body;
        }
    }

    public class AuthorTemplate extends Template {
        private AuthorData list;

        public AuthorData getList() {
            return list;
        }

        public void setList(AuthorData list) {
            this.list = list;
        }
    }

    public class ListItemData extends Template {
        private String id;
        private String image;
        private String header;
        private String sub_heading;
        private String status;
        private String status_bubble_color;
        private String action;
        private String meta;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getSub_heading() {
            return sub_heading;
        }

        public void setSub_heading(String sub_heading) {
            this.sub_heading = sub_heading;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus_bubble_color() {
            return status_bubble_color;
        }

        public void setStatus_bubble_color(String status_bubble_color) {
            this.status_bubble_color = status_bubble_color;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getMeta() {
            return meta;
        }

        public void setMeta(String meta) {
            this.meta = meta;
        }
    }
    public class AuthorData extends Template {
        private String image;
        private String main_label;
        private String sub_label;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getMain_label() {
            return main_label;
        }

        public void setMain_label(String main_label) {
            this.main_label = main_label;
        }

        public String getSub_label() {
            return sub_label;
        }

        public void setSub_label(String sub_label) {
            this.sub_label = sub_label;
        }
    }
}


