package models;

import java.util.List;

public interface OnScoreResponse {

    void consumeScores(List<Score> scoreList);
}
