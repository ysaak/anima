package ysaak.anima.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.data.importer.Importer;
import ysaak.anima.data.importer.TagEquivalence;

import java.util.List;

@Repository
public interface TagEquivalenceRepository extends CrudRepository<TagEquivalence, String> {
    List<TagEquivalence> findByImporter(Importer importer);
    void deleteByImporter(Importer importer);
}
