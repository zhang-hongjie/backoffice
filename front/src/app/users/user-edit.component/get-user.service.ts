import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { User } from '../shared';

@Injectable()
export class GetUserService {

    constructor(private http: Http) {
    }

    byId(id: number): Observable<User> {
        return this.http
            .get('api/users/' + id)
            .map(this.assertSuccess)
            .map((res: Response) => res.json())
            .map(this.deserialize)
            .catch(this.handleError);
    }

    private assertSuccess(res: Response) {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status + ' ' + res.statusText);
        }

        return res;
    }

    private deserialize(data: any) {
        return new User(+data.id, data.email);
    }

    private handleError(error: any) {
        return Observable.throw(error.message || 'Server Error');
    }
}
