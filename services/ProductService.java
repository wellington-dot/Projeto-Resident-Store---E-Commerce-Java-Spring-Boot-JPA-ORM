package com.wsystems.residentstore.services;

import com.wsystems.residentstore.dto.ProductDTO;
import com.wsystems.residentstore.entities.Product;
import com.wsystems.residentstore.repositories.ProductRepository;
import com.wsystems.residentstore.services.exeptions.ResourceNotFoundException;
import com.wsystems.residentstore.services.exeptions.DatabaseException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    //Lendo um produto pelo ID
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new ProductDTO(product);
    }

    //Buscando pelo nome no BD
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByName(String name, Pageable pageable){
        Page<Product> result = repository.searchByName(name, pageable);
        return result.map(x -> new ProductDTO(x));
    }

    //Salvando um produto no BD
    @Transactional
    public ProductDTO insertProduct(ProductDTO dto){
        Product product = new Product();
        copyDtoToProduct(dto, product);
        product = repository.save(product);
        return new ProductDTO(product);
    }

    //Atualizando um registro
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto){
        try {
            Product product = repository.getReferenceById(id);
            copyDtoToProduct(dto, product);
            product = repository.save(product);
            return new ProductDTO(product);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    //Deletando
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try{
            repository.deleteById(id);
        } catch(DataIntegrityViolationException e){
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToProduct(ProductDTO dto, Product product) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImgUrl(dto.getImgUrl());
    }
}
