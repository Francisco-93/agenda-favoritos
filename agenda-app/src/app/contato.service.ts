import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Contato } from './contato/contato';

@Injectable({
  providedIn: 'root'
})
export class ContatoService {

  url: string = environment.apiBaseUrl;

  constructor(
    private http: HttpClient
  ) { }

  save(contato: Contato): Observable<Contato>{
    return this.http.post<Contato>(this.url, contato);
  }

  list(): Observable<Contato[]>{
    return this.http.get<Contato[]>(this.url);
  }

  favoritar(contato: Contato): Observable<any>{
    return this.http.patch<any>(`${this.url}/${contato.id}/favorito`, null);
  }

  upload(contato: Contato, formData: FormData): Observable<any>{
    return this.http.put<any>(`${this.url}/${contato.id}/foto`, formData, { responseType: 'blob' as 'json' });
  }

}
