package com.devgui.urlshortener.api.v1.doc;

import com.devgui.urlshortener.api.v1.dto.request.ShortUrlRequest;
import com.devgui.urlshortener.api.v1.dto.response.PageResponse;
import com.devgui.urlshortener.api.v1.dto.response.ShortUrlDetailsResponse;
import com.devgui.urlshortener.api.v1.dto.response.ShortUrlResponse;
import com.devgui.urlshortener.api.v1.dto.response.UrlAnalyticsResponse;
import com.devgui.urlshortener.api.v1.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(
        name = "Short URLs",
        description = """
Operações para criação, consulta, listagem,
desativação e análise de URLs encurtadas.
"""
)
public interface ShortUrlControllerDoc {
    @Operation(
            summary = "Criar URL encurtada",
            description = "Cria uma nova URL encurtada.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "URL criada com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "429",
                            description = "Limite de requisições excedido",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<ShortUrlResponse> shorten(
            @RequestBody(
                    required = true,
                    description = "Dados para criação da URL encurtada",
                    content = @Content(
                            schema = @Schema(implementation = ShortUrlRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "originalUrl": "https://github.com/",
                                          "expiresAt": "2040-06-02T09:00:00Z"
                                        }
                                        """
                            )
                    )
            )
            @Valid
            ShortUrlRequest dto
    );

    @Operation(
            summary = "Buscar URL encurtada por ID",
            description = "Retorna os detalhes completos de uma URL encurtada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "URL encontrada"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "URL não encontrada",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
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
    ResponseEntity<ShortUrlDetailsResponse> getById(
            @Parameter(
                    description = "Identificador único da URL encurtada",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            UUID id
    );

    @Operation(
            summary = "Listar URLs encurtadas",
            description = "Retorna uma lista paginada de URLs encurtadas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parâmetros inválidos",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
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
    ResponseEntity<PageResponse<ShortUrlResponse>> getAll(

            @Parameter(
                    description = "Número da páginaaa"
            )
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            Integer page,

            @Parameter(
                    description = "Quantidade de registros por página"
            )
            @Max(value = 20, message = "Size must not exceed 20")
            Integer size
    );

    @Operation(
            summary = "Desativar URL encurtada",
            description = "Desativa uma URL encurtada impedindo novos acessos.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "URL desativada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "URL não encontrada",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
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
    ResponseEntity<Void> disable(

            @Parameter(
                    description = "Identificador único da URL encurtada",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            UUID id
    );

    @Operation(
            summary = "Consultar analytics da URL",
            description = """
                Retorna estatísticas de acesso da URL encurtada,
                incluindo quantidade de cliques e acessos recentes.
                """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Analytics retornado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "URL não encontrada",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parâmetros inválidos",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
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
    ResponseEntity<UrlAnalyticsResponse> analytics(

            @Parameter(
                    description = "Identificador único da URL encurtada",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            UUID id,

            @Parameter(
                    description = "Quantidade de acessos recentes retornados",
                    example = "5"
            )
            @Max(value = 10, message = "Size must not exceed 10")
            Integer size
    );
}
