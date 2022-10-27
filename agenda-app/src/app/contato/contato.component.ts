import { Component, OnInit } from '@angular/core';
import { ContatoService } from '../contato.service';
import { Contato } from './contato';

/** Necessários para se trabalhar com formulários reativos */
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

import { MatDialog } from '@angular/material/dialog';
import { ContatoDetalheComponent } from '../contato-detalhe/contato-detalhe.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-contato',
  templateUrl: './contato.component.html',
  styleUrls: ['./contato.component.css']
})
export class ContatoComponent implements OnInit {

  formulario!: FormGroup;

  contatos: Contato[] = [];

  colunas: string[] = ['foto', 'id', 'nome', 'email', 'favorito'];

  constructor(
    private service: ContatoService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {

    /** Perceba que o validador aplica uma classe css ao input deixando ele vermelho, caso a validação detecte algum erro */
    
    this.montarFormulario();
    this.listarContatos();
  }

  listarContatos(): void{
    this.service.list().subscribe( resposta => {
      this.contatos = resposta;
    } );
  
  }

  montarFormulario(): void{
    this.formulario = this.fb.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    })
  }
  
  favoritar(contato: Contato): void{
    contato.favorito = !contato.favorito;
    this.service.favoritar(contato).subscribe();
  }

  submit(){
    /** Verifica se os campos isoladamente estão válidos */
    // this.formulario.get('nome')?.errors == null ? this.isFormularioValido.nome = false : this.isFormularioValido.nome = true;
    // this.formulario.get('email')?.errors?.['required'] ? this.isFormularioValido.emailRequired = true : this.isFormularioValido.emailRequired = false;
    // this.formulario.get('email')?.errors?.['email'] ? this.isFormularioValido.emailValido = true : this.isFormularioValido.emailValido = false;

    // console.log(this.formulario.controls['nome']);

    /** Verifica se o formulário como um todo está válido */
    // const isValid = this.formulario.valid;
    
    const formValues = this.formulario.value;

    this.service.save(formValues).subscribe( (resposta) => {
      let lista: Contato[] = [...this.contatos, resposta];
      this.contatos = lista;
      this.snackBar.open('Contato foi adicionado', 'Sucesso!', {duration: 2000});
    } );
    this.formulario.reset();
  }

  uploadFoto(event: any, contato: Contato): void{
    console.log(event);
    const files = event.target.files;

    if(files){
      const foto = files[0];
      const formData: FormData = new FormData();
      formData.append('foto', foto);

      this.service.upload(contato, formData).subscribe( resposta => {
        this.listarContatos();
      } );
    }
  }

  visualizarContato(contato: Contato): void{
    this.dialog.open( ContatoDetalheComponent, {
      width: '400px',
      height: '450px',
      data: contato
    } )
  }

}
