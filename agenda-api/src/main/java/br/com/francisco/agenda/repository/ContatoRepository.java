package br.com.francisco.agenda.repository;

import br.com.francisco.agenda.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {
}
