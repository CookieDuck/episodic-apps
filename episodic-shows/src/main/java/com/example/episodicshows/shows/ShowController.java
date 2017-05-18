package com.example.episodicshows.shows;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {
    private final ShowService service;

    public ShowController(ShowService service) {
        this.service = service;
    }

    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return service.createShow(show);
    }

    @GetMapping
    public List<Show> getShows() {
        return service.getShows();
    }

    @PostMapping("/{id}/episodes")
    public ResponseEntity<EpisodeModel> createEpisode(@PathVariable(value = "id") Long showId,
                                                      @RequestBody Episode episode) {
        Episode created = service.createEpisode(showId, episode);
        if (created == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        EpisodeModel model = MapperUtils.map(created);
        return new ResponseEntity(model, HttpStatus.OK);
    }

    @GetMapping("/{id}/episodes")
    public List<EpisodeModel> getEpisodesForShow(@PathVariable(value = "id") Long showId) {
        return MapperUtils.map(service.getEpisodesForShow(showId));
    }
}
