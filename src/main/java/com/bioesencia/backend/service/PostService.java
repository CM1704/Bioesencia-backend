package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Post;
import com.bioesencia.backend.repository.PostRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // Crear nuevo post
    public Post crear(Post post) {
        post.setFechaCreacion(LocalDateTime.now());
        return postRepository.save(post);
    }

    // Listar todos los posts
    public List<Post> listarTodos() {
        return postRepository.findAll();
    }

    // Actualizar un post existente
    public Optional<Post> actualizar(Long id, Post postActualizado) {
        return postRepository.findById(id).map(post -> {
            post.setTitulo(postActualizado.getTitulo());
            post.setContenido(postActualizado.getContenido());
            post.setImagen(postActualizado.getImagen());
            // No actualizamos la fecha de creación
            return postRepository.save(post);
        });
    }

    // Eliminar un post por ID
    public void eliminarPorId(Long id) {
        postRepository.deleteById(id);
    }

    // Buscar un post por ID
    public Optional<Post> buscarPorId(Long id) {
        return postRepository.findById(id);
    }
}
