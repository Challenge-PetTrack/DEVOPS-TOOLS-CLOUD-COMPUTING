package com.fiap.pettrack.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	
	final String AUTORIZACAO = "bearerAuth";
	
	@Bean
	OpenAPI configurarSwagger() {
		return new OpenAPI()
				
				.addSecurityItem(new SecurityRequirement().addList(AUTORIZACAO))
				
				.components(new Components().addSecuritySchemes(AUTORIZACAO, 
						new SecurityScheme()
						.name(AUTORIZACAO)
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")))

				.info(new Info().title("Pet Track")
				.description("Ao contrário das soluções tradicionais, o PetTrack apresenta o Health Score contínuo: um indicador dinâmico que vai de 0 a 100 e acompanha a saúde do pet ao longo do tempo. Ele é atualizado com base em dados clínicos, comportamentais e genéticos, ajudando os tutores a tomar decisões de cuidado antes que os problemas se agravem.")
				.summary("O PetTrack é uma plataforma preditiva e longitudinal que transforma a relação entre tutor, pet e clínica veterinária — de um modelo episódico e reativo para uma experiência contínua, preventiva e inteligente.")
				.version("1.0.0"));
	}

}
