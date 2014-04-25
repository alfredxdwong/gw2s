package info.mornlight.gw2s.android.model.wvw;

import java.util.Date;

/**
 * Created by alfred on 5/22/13.
 */
public class WvwMatch {
    String wvwMatchId;
    int redWorldId;
    int blueWorldId;
    int greenWorldId;
    Date startTime;
    Date endTime;

    public WvwMatch() {
    }

    public WvwMatch(String wvwMatchId, int redWorldId, int blueWorldId, int greenWorldId) {
        this.wvwMatchId = wvwMatchId;
        this.redWorldId = redWorldId;
        this.blueWorldId = blueWorldId;
        this.greenWorldId = greenWorldId;
    }

    public String getWvwMatchId() {
        return wvwMatchId;
    }

    public void setWvwMatchId(String wvwMatchId) {
        this.wvwMatchId = wvwMatchId;
    }

    public int getRedWorldId() {
        return redWorldId;
    }

    public void setRedWorldId(int redWorldId) {
        this.redWorldId = redWorldId;
    }

    public int getBlueWorldId() {
        return blueWorldId;
    }

    public void setBlueWorldId(int blueWorldId) {
        this.blueWorldId = blueWorldId;
    }

    public int getGreenWorldId() {
        return greenWorldId;
    }

    public void setGreenWorldId(int greenWorldId) {
        this.greenWorldId = greenWorldId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}

