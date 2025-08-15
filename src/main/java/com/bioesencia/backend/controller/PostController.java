package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.Post;
import com.bioesencia.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // Crear un nuevo post
    @PostMapping("/crear")
    public Post crearPost(@RequestBody Post post) {
        return postService.crear(post);
    }

    // Listar todos los posts
    @GetMapping("/listar")
    public List<Post> listarPosts() {
        return postService.listarTodos();
    }
    
    @PutMapping("/actualizar/{id}")
    public Optional<Post> actualizarPost(@PathVariable Long id, @RequestBody Post post) {
        return postService.actualizar(id, post);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public void eliminarPost(@PathVariable Long id) {
        postService.eliminarPorId(id);
    }

    @GetMapping("/{id}")
    public Optional<Post> obtenerPost(@PathVariable Long id) {
        return postService.buscarPorId(id);
    }
}
