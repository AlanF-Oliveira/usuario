package com.alan.usuario.business;

import com.alan.usuario.infrastructure.clients.ViaCepClient;
import com.alan.usuario.infrastructure.clients.ViaCepDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService {
    private final ViaCepClient client;

    public ViaCepDTO buscarDadosEndereco(String cep){
            return client.buscaDadosDeEndereco(processarCep(cep));
    }

    public String processarCep(String cep) {

        if (cep == null) {
            throw new IllegalArgumentException("CEP não pode ser nulo");
        }

        String cepFormatado = cep.replaceAll("[^0-9]", "");

        if (!cepFormatado.matches("\\d{8}")) {
            throw new IllegalArgumentException("O cep contém caracteres inválidos, favor verificar");
        }
        return cepFormatado;
    }
}


