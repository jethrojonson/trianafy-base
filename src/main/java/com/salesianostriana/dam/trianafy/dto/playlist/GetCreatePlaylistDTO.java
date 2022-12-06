package com.salesianostriana.dam.trianafy.dto.playlist;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetCreatePlaylistDTO {

    private Long id;
    private String name;
    private String description;

}
