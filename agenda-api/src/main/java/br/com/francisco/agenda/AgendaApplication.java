package br.com.francisco.agenda;

import br.com.francisco.agenda.model.Contato;
import br.com.francisco.agenda.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendaApplication.class, args);
	}
}

/*
	Maneira interessante de se retornar uma interface.

	@Bean
	public CommandLineRunner teste(@Autowired ContatoRepository repository){
		return args -> {
			Contato contato = new Contato();
			contato.setNome("Antonia Aureni Frota Aguiar");
			contato.setEmail("aureni.frota@gmail.com");
			contato.setFavorito(true);

			repository.save(contato);
		};
	}

 */