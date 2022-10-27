package br.com.francisco.agenda.controller;

import br.com.francisco.agenda.model.Contato;
import br.com.francisco.agenda.repository.ContatoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contatos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ContatoController {

    private final ContatoRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contato salvar( @RequestBody Contato contato ){
        return repository.save(contato);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar( @PathVariable Integer id ){
        repository.deleteById(id);
    }

    @GetMapping
    public List<Contato> listarTodos(){
        return repository.findAll();
    }

    @PatchMapping("/{id}/favorito")
    public void favoritar( @PathVariable Integer id ){
        Optional<Contato> contato = repository.findById(id);
        contato.ifPresent( c -> {
            boolean favorito = c.isFavorito();
            c.setFavorito(!favorito);
            repository.save(c);
        } );
    }

    @PutMapping("{id}/foto")
    public byte[] addFoto(@PathVariable Integer id, @RequestParam("foto") Part arquivo){
        Optional<Contato> contato = repository.findById(id);

        return contato.map( c -> {
            try{
                InputStream is = arquivo.getInputStream();
                byte[] bytes = new byte[ (int) arquivo.getSize() ];
                IOUtils.readFully(is, bytes);
                c.setFoto(bytes);
                repository.save(c);
                is.close();
                return bytes;
            }
            catch (IOException ex){
                return null;
            }
        } ).orElse(null);
    }

    public Page<Contato> contatoPaginado(
            @RequestParam(value = "page", defaultValue = "0") Integer pagina,
            @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina
        ){
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina);
        return repository.findAll(pageRequest);
    }
}
