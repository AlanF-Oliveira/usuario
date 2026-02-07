package com.alan.usuario.controller;

import com.alan.usuario.business.UsuarioService;
import com.alan.usuario.business.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;
    @PostMapping
    public ResponseEntity <UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO UsuarioDTO){
        return  ResponseEntity.ok(usuarioService.salvaUsuario(UsuarioDTO));
    }
}
