package com.mjc.school.service.implementation;


import com.mjc.school.repository.BaseRepository;

import com.mjc.school.repository.implementation.AuthorRepository;
import com.mjc.school.repository.model.AuthorModel;

import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.exceptions.ElementNotFoundException;
import com.mjc.school.service.interfaces.AuthorServiceInterface;
import com.mjc.school.service.mapper.AuthorMapper;
import com.mjc.school.service.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static com.mjc.school.service.exceptions.ErrorCodes.NO_AUTHOR_FOR_NEWS_ID;
import static com.mjc.school.service.exceptions.ErrorCodes.NO_AUTHOR_WITH_PROVIDED_ID;

@Service("authorService")
public class AuthorService implements AuthorServiceInterface {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;


    @Autowired
    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper=authorMapper;

    }

    @Override
    public List<AuthorDtoResponse> readAll(int page, int size, String sortBy) {
        return authorMapper.ModelListToDtoList(authorRepository.readAll(page, size, sortBy));
    }

    @Override
    public AuthorDtoResponse readById(Long id) {
        Optional<AuthorModel> opt = authorRepository.readById(id);
        return opt.map(authorMapper::ModelAuthorToDTO).orElseThrow(() -> new ElementNotFoundException(String.format(NO_AUTHOR_WITH_PROVIDED_ID.getErrorMessage(), id)));
    }

    @Override
    public AuthorDtoResponse create(@Valid AuthorDtoRequest createRequest) {
        AuthorModel authorModel = authorMapper.DtoAuthorToModel(createRequest);
        return authorMapper.ModelAuthorToDTO(authorRepository.create(authorModel));
    }


    @Override
    public AuthorDtoResponse update(@Valid AuthorDtoRequest updateRequest) {
        if(authorRepository.existById(updateRequest.getId())){
            AuthorModel authorModel = authorMapper.DtoAuthorToModel(updateRequest);
            return authorMapper.ModelAuthorToDTO(authorRepository.update(authorModel));
        }

      else { throw new ElementNotFoundException(String.format(NO_AUTHOR_WITH_PROVIDED_ID.getErrorMessage(), updateRequest.getId()));

        }
    }

    @Override
    public boolean deleteById(Long id) {
        if(authorRepository.existById(id)){
        return authorRepository.deleteById(id);
    }
        else {
            throw new ElementNotFoundException(String.format(NO_AUTHOR_WITH_PROVIDED_ID.getErrorMessage(), id));
        }}


    @Override
    public AuthorDtoResponse readAuthorByNewsId(Long newsId) {
        return authorRepository.readAuthorByNewsId(newsId).map(authorMapper::ModelAuthorToDTO).orElseThrow(()-> new ElementNotFoundException(String.format( NO_AUTHOR_FOR_NEWS_ID.getErrorMessage(), newsId)));
    }
}