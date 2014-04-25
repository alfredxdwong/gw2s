package info.mornlight.gw2s.android.model.wvw;

import java.util.List;

/**
 * Created by alfred on 5/22/13.
 */
public class WvwMatchDetails {
    private String matchId;
    private List<Integer> scores;
    private List<WvwMap> maps;

    public WvwMatchDetails() {
    }

    public WvwMatchDetails(String matchId, List<Integer> scores, List<WvwMap> maps) {
        this.matchId = matchId;
        this.scores = scores;
        this.maps = maps;
    }

    public String getMatchId() {
        return matchId;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public List<WvwMap> getMaps() {
        return maps;
    }
}
