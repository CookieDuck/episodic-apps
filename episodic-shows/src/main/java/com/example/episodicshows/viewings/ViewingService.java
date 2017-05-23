package com.example.episodicshows.viewings;

import com.example.episodicshows.shows.Episode;
import com.example.episodicshows.shows.EpisodeService;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.shows.ShowService;
import com.example.episodicshows.user.User;
import com.example.episodicshows.user.UserService;
import com.example.episodicshows.utils.MapperUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ViewingService {
    private final ViewingRepository viewingRepo;
    private final UserService userService;
    private final ShowService showService;
    private final EpisodeService episodeService;

    public ViewingService(ViewingRepository viewingRepo, UserService userService, ShowService showService, EpisodeService episodeService) {
        this.viewingRepo = viewingRepo;
        this.userService = userService;
        this.showService = showService;
        this.episodeService = episodeService;
    }

    List<ViewingModel> getRecentlyWatched(Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return Collections.emptyList();
        }

        List<Viewing> viewings = viewingRepo.findAllByUserId(user.getId());

        // find out which episodes to get
        Set<Long> episodeIds = viewings.stream().map(Viewing::getEpisodeId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(episodeIds)) {
            return Collections.emptyList();
        }
        List<Episode> episodes = episodeService.getEpisodes(episodeIds);
        Map<Long, Episode> episodeMap = episodes.stream().collect(Collectors.toMap(Episode::getId, Function.identity()));

        // of those episodes, find out which shows to get
        Set<Long> showIds = episodes.stream().map(Episode::getShowId).collect(Collectors.toSet());
        List<Show> shows = showService.getShows(showIds);
        Map<Long, Show> showMap = shows.stream().collect(Collectors.toMap(Show::getId, Function.identity()));

        // combine it all into ViewingModel for each Viewing
        List<ViewingModel> viewingModels = viewings.stream().map(v -> {
            Show show = showMap.get(v.getShowId());
            Episode episode = episodeMap.get(v.getEpisodeId());
            return MapperUtils.map(show, episode, v.getUpdatedAt(), v.getTimecode());
        }).collect(Collectors.toList());


        return viewingModels;
    }

    void update(Long userId, ViewingPatch patch) {
//        Long episodeId = patch.getEpisodeId();
//        Viewing mostRecent = viewingRepo.findByUserIdAndEpisodeId(userId, episodeId);
//        Episode episode = episodeService.getEpisode(episodeId);
//        Long showId = episode.getShowId();
//        if (mostRecent == null) {
//            Viewing viewing = new Viewing(userId, showId, episodeId, patch.getUpdatedAt(), patch.getTimecode());
//            viewingRepo.save(viewing);
//        } else {
//            mostRecent.setUpdatedAt(patch.getUpdatedAt());
//            mostRecent.setTimecode(patch.getTimecode());
//            viewingRepo.save(mostRecent);
//        }
        updateViewing(userId, patch.getEpisodeId(), patch.getUpdatedAt(), patch.getTimecode());
    }

    public void updateProgress(ProgressMessage progressMessage) {
        Long userId = progressMessage.getUserId();
        if (userId == null || userService.getUser(userId) == null) {
            return;
        }
        Long episodeId = progressMessage.getEpisodeId();
        if (episodeId == null || episodeService.getEpisode(episodeId) == null) {
            return;
        }

        updateViewing(userId, episodeId, progressMessage.getCreatedAt(), progressMessage.getOffset());
    }

    private void updateViewing(Long userId, Long episodeId, LocalDateTime updatedAt, int timecode) {
        Viewing mostRecent = viewingRepo.findByUserIdAndEpisodeId(userId, episodeId);
        Episode episode = episodeService.getEpisode(episodeId);
        Long showId = episode.getShowId();
        if (mostRecent == null) {
            Viewing viewing = new Viewing(userId, showId, episodeId, updatedAt, timecode);
            viewingRepo.save(viewing);
        } else {
            mostRecent.setUpdatedAt(updatedAt);
            mostRecent.setTimecode(timecode);
            viewingRepo.save(mostRecent);
        }
    }
}
