package com.example.tomreaddle.kundalik.entity;

public class note {

    int id;
    String title;
    String emoji;
    String feeling;
    String description;
    String time;

    public note(){
        
    }

    public note(int id , String title , String emoji , String feeling , String description , String time){
        this.id = id;
        this.title = title;
        this.emoji= emoji;
        this.feeling = feeling;
        this.description = description;
        this.time = time;
    }

    public note(String title , String emoji , String feeling , String description , String time){
        this.title = title;
        this.emoji = emoji;
        this.feeling = feeling;
        this.description = description;
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

}
