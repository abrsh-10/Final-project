import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './user';
import { DataService } from 'src/app/service/DataService';

@Injectable({
  providedIn: 'root',
})
export class UserService extends DataService<User> {
  constructor(http: HttpClient) {
    super(http);
  }

  protected getUrl(): string {
    return 'http://localhost:8080/api/user/email';
  }
  getByEmail(email: String): Observable<User> {
    const url = `${this.getUrl()}`;
    return this.getById(url, email);
  }
}
