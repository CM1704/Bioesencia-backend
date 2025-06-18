package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Taller;
import com.bioesencia.backend.repository.TallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TallerService {

    private final TallerRepository tallerRepository;

    public Taller registrar(Taller taller) {
        taller.setActivo(true);
        return tallerRepository.save(taller);
    }

    public List<Taller> listar() {
        return tallerRepository.findAll();
    }

    public Optional<Taller> buscarPorId(Long id) {
        return tallerRepository.findById(id);
    }
}
