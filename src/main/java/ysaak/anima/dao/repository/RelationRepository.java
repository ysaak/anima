package ysaak.anima.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.data.Relation;
import ysaak.anima.data.RelationType;

import java.util.Optional;

@Repository
public interface RelationRepository extends CrudRepository<Relation, String> {

    @Query("SELECT r FROM Relation r WHERE r.element.id = :elementId AND r.relatedElement.id = :relatedElementId AND r.type = :type")
    Optional<Relation> findByElementsAndType(String elementId, String relatedElementId, RelationType type);
}
