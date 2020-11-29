package com.openmoments.scytale.controllers;

import com.openmoments.scytale.entities.AuthType;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthTypeModelAssembler implements RepresentationModelAssembler<AuthType, EntityModel<AuthType>> {
    @Override
    public EntityModel<AuthType> toModel(AuthType authType) {
        return EntityModel.of(authType,
                linkTo(methodOn(AuthTypeController.class).one(authType.getId())).withSelfRel(),
                linkTo(methodOn(AuthTypeController.class).all()).withRel("authTypes"));
    }
}
