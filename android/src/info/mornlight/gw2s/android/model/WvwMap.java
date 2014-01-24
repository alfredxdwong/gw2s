package info.mornlight.gw2s.android.model;

import java.util.List;

public class WvwMap {
    WvwMapType type;
    List<Integer> scores;
    List<WvwObjective> objectives;
    List<Bonus> bonuses;

    public WvwMap() {

    }
    public WvwMap(WvwMapType type, List<Integer> scores, List<WvwObjective> objectives) {
        this.type = type;
        this.scores = scores;
        this.objectives = objectives;
    }

    public WvwMapType getType() {
        return type;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public List<WvwObjective> getObjectives() {
        return objectives;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }
}
