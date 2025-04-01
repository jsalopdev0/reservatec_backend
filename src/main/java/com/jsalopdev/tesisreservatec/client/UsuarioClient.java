package com.jsalopdev.tesisreservatec.client;
import com.jsalopdev.tesisreservatec.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${apitecsup.api.url}")
    private String apiUrl;

    public List<Usuario> obtenerUsuarios() {
        return webClientBuilder.build()
                .get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(Usuario[].class)
                .map(Arrays::asList)
                .block();
    }
}
