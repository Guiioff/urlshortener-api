package com.devgui.urlshortener.api.v1.doc;

import com.devgui.urlshortener.api.v1.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Redirects",
        description = """
Operações responsáveis pelo redirecionamento de URLs encurtadas.
"""
)
public interface RedirectControllerDoc {

    @Operation(
            summary = "Redirecionar URL encurtada",
            description = """
                Resolve uma chave curta e redireciona o usuário para a URL original.
                
                A cada acesso, o contador de cliques é incrementado e
                informações analíticas do acesso são registradas.
                """,
            responses = {
                    @ApiResponse(
                            responseCode = "302",
                            description = """
                                Redirecionamento realizado com sucesso.
                                O cabeçalho 'Location' conterá a URL original.
                                """,
                            headers = {
                                    @Header(
                                            name = "Location",
                                            description = "URL de destino"
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "URL encurtada não encontrada",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "410",
                            description = "URL expirada ou desativada",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "429",
                            description = "Limite de requisições excedido",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<Void> redirect(

            @Parameter(
                    description = "Chave curta gerada para a URL",
                    example = "aB3xYz"
            )
            String shortKey,
            HttpServletRequest request
    );
}
