import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './model/user';
import {Account} from './model/account';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private API_URL = '/api';

  constructor(private http: HttpClient) {
  }

  checkExistUser(email: string): Observable<boolean> {
    return this.http.get<boolean>(this.API_URL + '/users/check' + '?email=' + email);
  }

  registerUserAccount(account: Account): Observable<User> {
    return this.http.post<User>(this.API_URL + '/users/', account);
  }
}
