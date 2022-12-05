package com.salesianostriana.dam.trianafy.dto.song;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GetSongDTO {

    private Long id;
    private String title;
    private String artist;
    private String album;
    private int year;

}
