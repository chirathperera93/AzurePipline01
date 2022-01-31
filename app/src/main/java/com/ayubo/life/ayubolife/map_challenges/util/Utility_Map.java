package com.ayubo.life.ayubolife.map_challenges.util;

import com.ayubo.life.ayubolife.model.CardsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

public class Utility_Map {


    public static ArrayList<CardsEntity> getCardView_ObjectList(String cards){
        String completed_steps, completed_days, completed_avg, remaining_steps, remaining_days, remaining_avg;
        String name, type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId;
        ArrayList<CardsEntity> mapCardsList=null; mapCardsList = new ArrayList<CardsEntity>();
        name = "";reletedId = "";
        type = "";
        text = "";
        image = "";
        heading = "";
        heading2 = "";
        link = "";
        status = "";
        value = "";
        min_val = "";
        max_val = "";
        desc = "";
        title = "";
        subtitle = "";
        meta = "";
        action="";
        JSONArray myCardsLists = null;

        if (cards.length() > 10) {

            try {
                myCardsLists = new JSONArray(cards);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        for (int i = 0; i < myCardsLists.length(); i++) {

            JSONObject childJson = null;
            try {
                childJson = (JSONObject) myCardsLists.get(i);
            } catch (JSONException e) {

                e.printStackTrace();
            }

            type = childJson.optString("type").toString();

            if (type.equals("main_card_2")) {
                heading = childJson.optString("heading").toString();
                name = childJson.optString("name").toString();
                status = childJson.optString("weather_status").toString();
                image = childJson.optString("weather_icon").toString();
                value = childJson.optString("degree").toString();
                desc = childJson.optString("description").toString();
                max_val = childJson.optString("max_degree").toString();
                min_val = childJson.optString("min_degree").toString();

                completed_steps = childJson.optString("completed_steps").toString();
                completed_days = childJson.optString("completed_days").toString();
                completed_avg = childJson.optString("completed_avg").toString();

                remaining_steps = childJson.optString("remaining_steps").toString();
                remaining_days = childJson.optString("remaining_days").toString();
                remaining_avg = childJson.optString("remaining_avg").toString();
                link = childJson.optString("link").toString();

                title = childJson.optString("title").toString();
                subtitle = childJson.optString("subtitle").toString();
                meta = childJson.optString("meta").toString();
                action = childJson.optString("action").toString();
                if((completed_steps.equals("")) || (completed_steps.equals("-")) ){
                    completed_steps="0";
                }
                if((remaining_steps.equals("")) || (remaining_steps.equals("-")) ){
                    remaining_steps="0";
                }
                int num = Integer.parseInt(completed_steps);
                String completed_steps_with_comma = NumberFormat.getIntegerInstance().format(num);
                int num_completed_avg = Integer.parseInt(completed_avg);
                String completed_avg_with_comma = NumberFormat.getIntegerInstance().format(num_completed_avg);
                int remaining_steps_in_int = Integer.parseInt(remaining_steps);
                String remaining_steps_with_comma = NumberFormat.getIntegerInstance().format(remaining_steps_in_int);
                int num_remaining_avg = Integer.parseInt(remaining_avg);
                String remaining_avg_with_comma = NumberFormat.getIntegerInstance().format(num_remaining_avg);
                String completedTextArray = completed_steps_with_comma + "_" + completed_days + "_" + completed_avg_with_comma + "/Day";
                String remainingTextArray = remaining_steps_with_comma + "_" + remaining_days + "_" + remaining_avg_with_comma + "/Day";

                mapCardsList.add(new CardsEntity(type, text, image, heading, name, link, status, value, remainingTextArray, completedTextArray, desc,title,subtitle,action,meta,reletedId));

            }

            else if (type.equals("main_card")) {

                name = childJson.optString("name").toString();
                status = childJson.optString("weather_status").toString();
                image = childJson.optString("weather_icon").toString();
                value = childJson.optString("degree").toString();
                desc = childJson.optString("description").toString();
                max_val = childJson.optString("max_degree").toString();
                min_val = childJson.optString("min_degree").toString();

                completed_steps = childJson.optString("completed_steps").toString();
                completed_days = childJson.optString("completed_days").toString();
                completed_avg = childJson.optString("completed_avg").toString();

                remaining_steps = childJson.optString("remaining_steps").toString();
                remaining_days = childJson.optString("remaining_days").toString();
                remaining_avg = childJson.optString("remaining_avg").toString();
                link = childJson.optString("link").toString();

                title = childJson.optString("title").toString();
                subtitle = childJson.optString("subtitle").toString();
                meta = childJson.optString("meta").toString();

                if((completed_steps.equals("")) || (completed_steps.equals("-")) ){
                    completed_steps="0";
                }
                if((remaining_steps.equals("")) || (remaining_steps.equals("-")) ){
                    remaining_steps="0";
                }
                int num = Integer.parseInt(completed_steps);
                String completed_steps_with_comma = NumberFormat.getIntegerInstance().format(num);
                int num_completed_avg = Integer.parseInt(completed_avg);
                String completed_avg_with_comma = NumberFormat.getIntegerInstance().format(num_completed_avg);
                int remaining_steps_in_int = Integer.parseInt(remaining_steps);
                String remaining_steps_with_comma = NumberFormat.getIntegerInstance().format(remaining_steps_in_int);
                int num_remaining_avg = Integer.parseInt(remaining_avg);
                String remaining_avg_with_comma = NumberFormat.getIntegerInstance().format(num_remaining_avg);
                String completedTextArray = completed_steps_with_comma + "_" + completed_days + "_" + completed_avg_with_comma + "/Day";
                String remainingTextArray = remaining_steps_with_comma + "_" + remaining_days + "_" + remaining_avg_with_comma + "/Day";

                mapCardsList.add(new CardsEntity(type, text, image, heading, name, link, status, value, remainingTextArray, completedTextArray, desc,title,subtitle,action,meta,reletedId));

            }
            else if (type.equals("html_card")) {
                String html = childJson.optString("html").toString();
                link = childJson.optString("link").toString();
                mapCardsList.add(new CardsEntity(type, html, "", "", "", link, "", "", "", "", "","","",action,"",reletedId));
            }
            else if (type.equals("waiting_to_start")) {
                value = childJson.optString("remaining_days").toString();
                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            } else if (type.equals("complete")) {

                heading = childJson.optString("heading").toString();
                value = childJson.optString("completed_days").toString();
                text = childJson.optString("text").toString();
                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            } else if (type.equals("incomplete")) {
                value = childJson.optString("completed_days").toString();
                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            } else if (type.equals("statistics")) {
                value = childJson.optString("completed_avg").toString();
                min_val = childJson.optString("collected_treatures").toString();
                max_val = childJson.optString("collected_entries").toString();
                link = childJson.optString("link").toString();

                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            } else if (type.equals("weather")) {

                name = childJson.optString("name").toString();
                status = childJson.optString("weather_status").toString();
                image = childJson.optString("weather_icon").toString();
                value = childJson.optString("degree").toString();
                desc = childJson.optString("description").toString();
                max_val = childJson.optString("max_degree").toString();
                min_val = childJson.optString("min_degree").toString();
                link = childJson.optString("link").toString();
                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            } else if (type.equals("location")) {
                image = childJson.optString("icon").toString();
                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            } else if (type.equals("banner")) {
                heading = childJson.optString("heading").toString();
                text = childJson.optString("text").toString();
                image = childJson.optString("image").toString();
                link = childJson.optString("link").toString();
                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            }else if (type.equals("native_leaderboard")) {
                title = childJson.optString("title").toString();
                subtitle = childJson.optString("subtitle").toString();
                image = childJson.optString("image").toString();
                meta = childJson.optString("meta").toString();
                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            }
            else if (type.equals("add_box")) {
                heading = childJson.optString("heading").toString();
                heading2 = childJson.optString("subheading").toString();
                text = childJson.optString("text").toString();
                image = childJson.optString("image").toString();
                link = childJson.optString("link").toString();
                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            } else if (type.equals("next_box")) {

                heading = childJson.optString("name").toString();
                value = childJson.optString("stepstonext").toString();

                image = childJson.optString("icon").toString();
                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            } else if (type.equals("plain_banner")) {
                heading = childJson.optString("heading").toString();
                text = childJson.optString("text").toString();
                image = childJson.optString("icon").toString();
                link = childJson.optString("link").toString();
                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            } else if (type.equals("share_box")) {

                text = childJson.optString("city").toString();
                value = childJson.optString("steps").toString();
                min_val = childJson.optString("days").toString();
                status = childJson.optString("status").toString();
                link = childJson.optString("share_image").toString();
                image = childJson.optString("bg_img").toString();

                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc,title,subtitle,action,meta,reletedId));
            } else {

            }
        }
        return mapCardsList;
    }
}
