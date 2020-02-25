package ysaak.anima.rules;

import ysaak.anima.data.RelationType;

import java.util.Optional;

public final class RelationRules {
    private RelationRules() { /**/ }

    public static Optional<RelationType> getInvertedRelation(final RelationType type) {
        //Vice versa only works when prequel, sequel, alternate setting, or alternate version are selected.
        final RelationType invertedRelation;

        // Prequel / Sequel
        if (type == RelationType.PREQUEL) {
            invertedRelation = RelationType.SEQUEL;
        }
        else if (type == RelationType.SEQUEL) {
            invertedRelation = RelationType.PREQUEL;
        }

        // Parent story / (side story, spin-off, )
        else if (type == RelationType.SIDE_STORY) {
            invertedRelation = RelationType.PARENT_STORY;
        }
        else if (type == RelationType.SPIN_OFF) {
            invertedRelation = RelationType.PARENT_STORY;
        }
        else if (type == RelationType.PARENT_STORY) {
            invertedRelation = null;
        }

        // Full story / summary
        else if (type == RelationType.FULL_STORY) {
            invertedRelation = RelationType.SUMMARY;
        }
        else if (type == RelationType.SUMMARY) {
            invertedRelation = RelationType.FULL_STORY;
        }

        // Others type are equals on both side
        else {
            invertedRelation = type;
        }

        return Optional.ofNullable(invertedRelation);
    }
}
