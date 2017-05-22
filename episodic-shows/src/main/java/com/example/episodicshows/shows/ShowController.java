package com.example.episodicshows.shows;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {
    private final ShowService showService;
    private final EpisodeService episodeService;

    public ShowController(ShowService showService, EpisodeService episodeService) {
        this.showService = showService;
        this.episodeService = episodeService;
    }

    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return showService.createShow(show);
    }

    @GetMapping
    public List<Show> getShows() {
        return showService.getShows();
    }

    @PostMapping("/{id}/episodes")
    public ResponseEntity<EpisodeModel> createEpisode(@PathVariable(value = "id") Long showId,
                                                      @RequestBody Episode episode) {
        EpisodeModel created = episodeService.createEpisodeModel(showId, episode);
        if (created == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(created, HttpStatus.OK);
    }

    @GetMapping("/{id}/episodes")
    public List<EpisodeModel> getEpisodesForShow(@PathVariable(value = "id") Long showId) {
        return episodeService.getEpisodesForShow(showId);
    }
}
