package com.alan.usuario.controller;

import com.alan.usuario.business.UsuarioService;
import com.alan.usuario.business.dto.EnderecoDTO;
import com.alan.usuario.business.dto.TelefoneDTO;
import com.alan.usuario.business.dto.UsuarioDTO;
import com.alan.usuario.infrastructure.security.JwtUtil;
import com.alan.usuario.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "Cadastro e login de usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Cadastrar usuário", description = "Realiza o cadastro de um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar usuário")
    })
    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @Operation(summary = "Login", description = "Realiza o login e retorna o token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário ou senha inválidos")
    })
    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken((usuarioDTO.getEmail()),
                        usuarioDTO.getSenha())
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    @Operation(summary = "Buscar usuário", description = "Busca usuário pelo email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioEmail(email));
    }

    @Operation(summary = "Deletar usuário", description = "Remove usuário pelo email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) {
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Atualizar dados do usuário", description = "Atualiza os dados do usuário logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizarDadosUsuario(@RequestBody UsuarioDTO usuarioDTO,
                                                            @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, usuarioDTO));
    }

    @Operation(summary = "Atualizar endereço", description = "Atualiza endereço pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizarEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                         @RequestParam("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, enderecoDTO));
    }

    @Operation(summary = "Atualizar telefone", description = "Atualiza telefone pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telefone atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Telefone não encontrado")
    })
    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizarTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                         @RequestParam("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, telefoneDTO));
    }

    @Operation(summary = "Cadastrar endereço", description = "Cadastra um novo endereço para o usuário logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço cadastrado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    @PostMapping("/endereco")
    public ResponseEntity<EnderecoDTO> cadastrarEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, enderecoDTO));
    }

    @Operation(summary = "Cadastrar telefone", description = "Cadastra um novo telefone para o usuário logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telefone cadastrado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    @PostMapping("/telefone")
    public ResponseEntity<TelefoneDTO> cadastrarTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastrarTelefone(token, telefoneDTO));
    }
}
