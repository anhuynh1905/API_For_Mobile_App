package com.ltdd.mobile_api.payload;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MediaResponse {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String url;
    @SerializedName("public")
    private boolean isPublic;
}
