import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class CreateUserService {

    constructor(private http: Http) {
    }

    execute(email: string): Observable<void> {
        return this.http
            .post('api/users', JSON.stringify({email: email}))
            .catch(this.handleError);
    }

    private handleError(response: Response) {
        if (response.status === 400) {
            return Observable.throw('L\'email existe déjà');
        }

        return Observable.throw(`${response.status} - ${response.statusText}`);
    }
}
