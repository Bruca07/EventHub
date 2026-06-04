package com.academy.eventhub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academy.eventhub.dto.TagDTO;
import com.academy.eventhub.entity.Tag;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.TagMapper;
import com.academy.eventhub.repository.TagRepository;
@Service
public class TagService {
@Autowired
    TagRepository tagRepo;

    @Autowired
    TagMapper mapper;

    public List<TagDTO> findAll() {
        return mapper.toDTOList(tagRepo.findAll());
    }

    public TagDTO findById(int id) {
        Tag tag = tagRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        return mapper.toDTO(tag);
    }

    public TagDTO save(TagDTO dto) {
        Tag tag = mapper.toEntity(dto);
        return mapper.toDTO(tagRepo.save(tag));
    }

    public TagDTO update(int id, TagDTO dto) {
        Tag tag = tagRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tag.setName(dto.getName());
        return mapper.toDTO(tagRepo.save(tag));
    }

    public void delete(int id) {
        if (!tagRepo.existsById(id)) {
            throw new ResourceNotFoundException("Tag not found");
        }
        tagRepo.deleteById(id);
    }
}
