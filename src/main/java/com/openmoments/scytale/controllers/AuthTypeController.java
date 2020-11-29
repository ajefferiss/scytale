package com.openmoments.scytale.controllers;

import com.openmoments.scytale.entities.AuthType;
import com.openmoments.scytale.exceptions.AuthTypeNotFoundException;
import com.openmoments.scytale.repositories.AuthTypeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AuthTypeController {

    private final AuthTypeRepository authTypeRepository;
    private final AuthTypeModelAssembler assembler;

    AuthTypeController(AuthTypeRepository authTypeRepository, AuthTypeModelAssembler assembler) {
        this.authTypeRepository = authTypeRepository;
        this.assembler = assembler;
    }

    @GetMapping("/authTypes/{id}")
    EntityModel<AuthType> one(@PathVariable Long id) {
        AuthType authType = authTypeRepository.findById(id).orElseThrow(() -> new AuthTypeNotFoundException(id));
        return assembler.toModel(authType);
    }

    @GetMapping("/authTypes")
    CollectionModel<EntityModel<AuthType>> all() {
        List<EntityModel<AuthType>> authTypes = authTypeRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(authTypes, linkTo(methodOn(AuthTypeController.class).all()).withSelfRel());
    }
}
