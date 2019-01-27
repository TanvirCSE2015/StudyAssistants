package com.ashik.justice.developer.studyassistants;

public class ListRequest {
    private String fire_id;
private String user_image;
private String user_name;

    public ListRequest(String fire_id, String user_image, String user_name) {
        this.fire_id = fire_id;
        this.user_image = user_image;
        this.user_name = user_name;
    }

    public String getFire_id() {
        return fire_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getUser_name() {
        return user_name;
    }
}
