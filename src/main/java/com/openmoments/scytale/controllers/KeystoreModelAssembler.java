package com.openmoments.scytale.controllers;

import com.openmoments.scytale.entities.Keystore;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class KeystoreModelAssembler implements RepresentationModelAssembler<Keystore, EntityModel<Keystore>> {
    @Override
    public EntityModel<Keystore> toModel(Keystore keystore) {
        return EntityModel.of(keystore,
                linkTo(methodOn(KeystoreController.class).one(keystore.getId())).withSelfRel(),
                linkTo(methodOn(KeystoreController.class).all()).withRel("keystores"));
    }
}
