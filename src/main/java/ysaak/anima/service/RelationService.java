package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.dao.repository.RelationRepository;
import ysaak.anima.data.Element;
import ysaak.anima.data.Relation;
import ysaak.anima.data.RelationType;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.exception.NoDataFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RelationService implements IAnimaComponent {

    private final RelationRepository relationRepository;

    @Autowired
    public RelationService(RelationRepository relationRepository) {
        this.relationRepository = relationRepository;
    }

    public void createRelation(String elementId, String relatedElementId, RelationType type) throws DataValidationException {
        List<Relation> relationToSaveList = new ArrayList<>();

        final Relation relation = new Relation(
                null,
                new Element(elementId),
                new Element(relatedElementId),
                type
        );

        validate(relation);

        relationToSaveList.add(relation);

        final RelationType invertedRelation = getInvertedRelation(type);
        if (invertedRelation != null) {

            final Relation relatedRelation = new Relation(
                    null,
                    new Element(relatedElementId),
                    new Element(elementId),
                    invertedRelation
            );

            relationToSaveList.add(relatedRelation);
        }

        relationRepository.saveAll(relationToSaveList);
    }

    private void validate(Relation relation) throws DataValidationException {
        Preconditions.checkNotNull(relation);
        validator().validate(relation);

        // TODO check uniqueness
    }

    private RelationType getInvertedRelation(final RelationType type) {
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

        return invertedRelation;
    }

    public void deleteRelation(String relationId) throws NoDataFoundException {
        final Relation relation = relationRepository.findById(relationId)
                .orElseThrow(() -> new NoDataFoundException("No relation found with id " + relationId));

        relationRepository.delete(relation);

        final RelationType invertedRelationType = getInvertedRelation(relation.getType());

        if (invertedRelationType != null) {
            Optional<Relation> invertedRelation = relationRepository.findByElementsAndType(
                    relation.getRelatedElement().getId(),
                    relation.getElement().getId(),
                    invertedRelationType
            );

            invertedRelation.ifPresent(relationRepository::delete);
        }
    }
}
