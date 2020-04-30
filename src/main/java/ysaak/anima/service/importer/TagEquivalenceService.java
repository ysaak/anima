package ysaak.anima.service.importer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ysaak.anima.dao.repository.TagEquivalenceRepository;
import ysaak.anima.data.importer.Importer;
import ysaak.anima.data.importer.TagEquivalence;
import ysaak.anima.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagEquivalenceService {
    private final TagEquivalenceRepository tagEquivalenceRepository;

    @Autowired
    public TagEquivalenceService(TagEquivalenceRepository tagEquivalenceRepository) {
        this.tagEquivalenceRepository = tagEquivalenceRepository;
    }

    public void save(final Importer importer, final Multimap<String, String> equivalenceMap) {
        Preconditions.checkNotNull(importer);
        Preconditions.checkNotNull(equivalenceMap);

        List<TagEquivalence> equivalenceList = new ArrayList<>();

        for (String tagId : equivalenceMap.keySet()) {

            equivalenceMap.get(tagId).stream()
                    .map(e -> new TagEquivalence(tagId, importer, e))
                    .forEach(equivalenceList::add);
        }

        tagEquivalenceRepository.deleteByImporter(importer);
        if (CollectionUtils.isNotEmpty(equivalenceList)) {
            tagEquivalenceRepository.saveAll(equivalenceList);
        }
    }

    public List<TagEquivalence> findByImporter(final Importer importer) {
        return tagEquivalenceRepository.findByImporter(importer);
    }
}
