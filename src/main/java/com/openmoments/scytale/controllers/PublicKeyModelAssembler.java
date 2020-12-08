package com.openmoments.scytale.controllers;

import com.openmoments.scytale.entities.PublicKey;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PublicKeyModelAssembler implements RepresentationModelAssembler<PublicKey, EntityModel<PublicKey>> {
    @Override
    public EntityModel<PublicKey> toModel(PublicKey publicKey) {
        Long keystoreId = publicKey.getKeystore().getId();
        return EntityModel.of(publicKey,
                linkTo(methodOn(PublicKeyController.class).one(keystoreId, publicKey.getId())).withSelfRel(),
                linkTo(methodOn(PublicKeyController.class).all(keystoreId)).withRel("keystores"));
    }
}
