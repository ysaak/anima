package ysaak.anima.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ysaak.anima.data.Entity;
import ysaak.anima.api.dto.PageDto;
import ysaak.anima.exception.ResourceNotFoundException;
import ysaak.anima.service.AbstractCrudService;

import java.net.URI;
import java.util.Optional;

public class AbstractCrudController<ENTITY extends Entity, S extends AbstractCrudService<ENTITY, ?, ?>> {
    final S entityService;

    public AbstractCrudController(S entityService) {
        this.entityService = entityService;
    }

    @PostMapping("/")
    public ResponseEntity<ENTITY> create(@RequestBody ENTITY data) {
        ENTITY dataStored = entityService.save(data);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(data.getId()).toUri();
        return ResponseEntity.created(location).body(dataStored);
    }

    @GetMapping("/")
    public PageDto<ENTITY> findAll(Pageable pageable) {
        return convertToPageDto(entityService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ENTITY findById(@PathVariable String id) throws ResourceNotFoundException {
        Optional<ENTITY> data = entityService.findById(id);
        return data.orElseThrow(() -> new ResourceNotFoundException("id=" + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ENTITY> update(@RequestBody ENTITY data, @PathVariable String id) {

        Optional<ENTITY> genreOptional = entityService.findById(id);

        if (!genreOptional.isPresent())
            return ResponseEntity.notFound().build();

        data.setId(id);

        ENTITY dataStored = entityService.save(data);

        return ResponseEntity.ok(dataStored);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws ResourceNotFoundException {
        entityService.delete(id);
    }

    private <T> PageDto<T> convertToPageDto(Page<T> page) {
        return new PageDto<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages()
        );
    }
}
