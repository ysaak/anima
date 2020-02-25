package ysaak.anima.data;

import ysaak.anima.dao.converter.ISerializableEnum;

public enum RelationType implements ISerializableEnum {
/*
sequel
Direct continuation of the story.

prequel
Story that occurred before the original.

alternative setting
Same universe/world/reality/timeline, completely different characters.

alternative version
Same setting, same characters, story is told differently.

side story
Takes place sometime during the parent storyline.

summary
Summarizes full story, may contain additional information.

full story
Full version of the summarized story.

parent story
Parent of all the shows (ex: Naruto TV is the 'parent story' of all its movies).

spin-off
Uses characters of a different series, but is not an alternate setting or story.

adaptation
Manga/Anime adaptation. This field is already vice versa enabled.

other
When nothing else fits
*/
    SEQUEL("SEQ"),
    PREQUEL("PRE"),
    ALTERNATIVE_SETTING("ASE"),
    ALTERNATIVE_VERSION("ASV"),
    SUMMARY("SUM"),
    FULL_STORY("FST"),
    SIDE_STORY("SST"),
    SPIN_OFF("SOF"),
    PARENT_STORY("PST"),
    ADAPTATION("ADA"),
    OTHER("OTH");

    private final String dbCode;

    RelationType(String dbCode) {
        this.dbCode = dbCode;
    }

    @Override
    public String serialize() {
        return dbCode;
    }
}
