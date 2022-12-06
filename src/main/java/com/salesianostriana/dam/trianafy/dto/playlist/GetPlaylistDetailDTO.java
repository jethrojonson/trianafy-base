package com.salesianostriana.dam.trianafy.dto.playlist;

import com.salesianostriana.dam.trianafy.dto.song.GetSongDTO;
import com.salesianostriana.dam.trianafy.model.Song;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GetPlaylistDetailDTO {

    private Long id;
    private String name;
    private String description;
    private List<GetSongDTO> songs = new ArrayList<GetSongDTO>();

}
