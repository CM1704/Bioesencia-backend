package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Taller;
import com.bioesencia.backend.repository.TallerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TallerService {

    private final TallerRepository tallerRepository;

    public List<Taller> findAll() {
        return tallerRepository.findAll();
    }

    public Optional<Taller> findById(Long id) {
        return tallerRepository.findById(id);
    }

    public Taller save(@Valid Taller taller) {
        return tallerRepository.save(taller);
    }

    public void deleteById(Long id) {
        tallerRepository.deleteById(id);
    }
}
