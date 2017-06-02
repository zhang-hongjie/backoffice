import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { User } from '../shared';

@Injectable()
export class UpdateUserService {

    constructor(private http: Http) {
    }

    execute(user: User): Observable<void> {
        return this.http
            .put('api/users/' + user.id, JSON.stringify({email: user.email}))
            .catch(this.handleError);
    }

    private handleError(response: Response) {
        if (response.status === 400) {
            return Observable.throw('L\'email existe déjà');
        }

        return Observable.throw(`${response.status} - ${response.statusText}`);
    }
}
