package com.wsystems.residentstore.controllers;

import com.wsystems.residentstore.dto.ProductDTO;
import com.wsystems.residentstore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService service;

    //Buscando produto por ID
    @GetMapping(value="/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    //Buscando por nome
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findByName(@RequestParam(name = "name", defaultValue = "") String name, Pageable pageable){
        Page<ProductDTO> dto = service.findByName(name, pageable);
        return ResponseEntity.ok(dto);
    }

    //Salvando produto e manipulando resposta 201
    @PostMapping
    public ResponseEntity<ProductDTO> insertProduct(@Valid @RequestBody ProductDTO dto){
        dto = service.insertProduct(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    //Atualizando um registro
    @PutMapping(value="/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id,@Valid @RequestBody ProductDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    //Deletando
    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
