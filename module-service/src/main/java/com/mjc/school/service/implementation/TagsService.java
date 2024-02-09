package com.mjc.school.service.implementation;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.implementation.TagRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.exceptions.ElementNotFoundException;
import com.mjc.school.service.interfaces.TagServiceInterface;
import com.mjc.school.service.mapper.TagMapper;
import com.mjc.school.service.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static com.mjc.school.service.exceptions.ErrorCodes.NO_TAGS_FOR_NEWS_ID;
import static com.mjc.school.service.exceptions.ErrorCodes.NO_TAG_WITH_PROVIDED_ID;

@Service("tagsService")
public class TagsService implements TagServiceInterface {
    private TagRepository tagsRepository;
    private TagMapper tagMapper;

    @Autowired
    public TagsService(TagRepository  tagsRepository, TagMapper tagMapper) {
        this.tagsRepository = tagsRepository;
        this.tagMapper=tagMapper;
    }

    @Override
    public List<TagDtoResponse> readAll(int page, int size, String sortBy) {
        return tagMapper.listModelToDtoList(tagsRepository.readAll(page, size, sortBy));
    }

    @Override
    public TagDtoResponse readById(Long id) {
        Optional<TagModel> opt = tagsRepository.readById(id);
        return opt.map(tagMapper::ModelTagsToDto).orElseThrow(()-> new ElementNotFoundException(String.format(NO_TAG_WITH_PROVIDED_ID.getErrorMessage(), id)));

    }

    @Override
    public TagDtoResponse create(@Valid TagDtoRequest createRequest) {
        TagModel tagModel = tagMapper.DtoTagsToModel(createRequest);
        return  tagMapper.ModelTagsToDto(tagsRepository.create(tagModel));
    }

    @Override
    public TagDtoResponse update(@Valid TagDtoRequest updateRequest) {
        if(tagsRepository.existById(updateRequest.id())){
            TagModel tagModel = tagMapper.DtoTagsToModel(updateRequest);
            return tagMapper.ModelTagsToDto(tagsRepository.update(tagModel));
        }
        else { throw  new ElementNotFoundException(String.format(NO_TAG_WITH_PROVIDED_ID.getErrorMessage(), updateRequest.id()));
    }

        }

    @Override
    public boolean deleteById(Long id) {
        if(tagsRepository.existById(id)){
           return tagsRepository.deleteById(id);
        }
        else { throw  new ElementNotFoundException(String.format(NO_TAG_WITH_PROVIDED_ID.getErrorMessage(), id));
    }


    }

    public List<TagDtoResponse> readListOfTagsByNewsId(Long newsId) {
        if (newsId!=null && newsId>=0){
        return tagMapper.listModelToDtoList(tagsRepository.readListOfTagsByNewsId(newsId));
    }
    else {throw  new ElementNotFoundException(String.format(NO_TAGS_FOR_NEWS_ID.getErrorMessage(), newsId));

    }
    }}

