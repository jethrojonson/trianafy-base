package com.salesianostriana.dam.trianafy.dto.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class GetPlaylistDTO {

    private Long id;
    private String name;
    private int numberOfSongs;

}
