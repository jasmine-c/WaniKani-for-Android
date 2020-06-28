package tr.xip.wanikani.models.v2.srs;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class SpacedRepetitionSystem implements Serializable {
    private int burning_stage_position;
    private DateTime created_at;
    private String description;
    private String name;
    private int passing_stage_position;
    private ArrayList<Stage> stages;
    private int starting_stage_position;
    private int unlocking_stage_position;

    public SpacedRepetitionSystem(
            int burning_stage_position, DateTime created_at, String description, String name,
            int passing_stage_position, ArrayList<Stage> stages, int starting_stage_position,
            int unlocking_stage_position) {
        this.burning_stage_position = burning_stage_position;
        this.created_at = created_at;
        this.description = description;
        this.name = name;
        this.passing_stage_position = passing_stage_position;
        this.stages = stages;
        this.starting_stage_position = starting_stage_position;
        this.unlocking_stage_position = unlocking_stage_position;
    }
}
