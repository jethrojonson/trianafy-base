package com.salesianostriana.dam.trianafy.dto.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSongDTO {

    private String title;
    private String album;
    private int year;
    private Long artistID;

}
