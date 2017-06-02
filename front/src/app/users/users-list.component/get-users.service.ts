import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { User } from '../shared';

@Injectable()
export class GetUsersService {

    constructor(private http: Http) {
    }

    all(): Observable<User[]> {
        return this.http
            .get('api/users')
            .map(this.assertSuccess)
            .map((res: Response) => res.json() || [])
            .map(this.buildUsers)
            .catch(this.handleError);
    }

    private assertSuccess(res: Response) {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status + ' ' + res.statusText);
        }

        return res;
    }

    private buildUsers(datas: any) {
        return datas.map((data: any) =>
            new User(+data.id, data.email)
        );
    }

    private handleError(error: any) {
        return Observable.throw(error.message || 'Server Error');
    }
}
