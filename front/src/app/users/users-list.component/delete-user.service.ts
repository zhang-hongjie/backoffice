import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class DeleteUserService {

    constructor(private http: Http) {
    }

    byId(id: number): Observable<void> {
        return this.http
            .delete('api/users/' + id)
            .map(this.assertSuccess)
            .catch(this.handleError);
    }

    private assertSuccess(res: Response) {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status + ' ' + res.statusText);
        }

        return res;
    }

    private handleError(error: any) {
        return Observable.throw(error.message || 'Server Error');
    }
}
