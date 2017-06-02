import { Http, Request, RequestOptionsArgs, Response, RequestOptions, ConnectionBackend, Headers} from '@angular/http';
import { Router} from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { AuthService } from './AuthService';

import { CustomRequestOptions } from './CustomRequestOptions';


export class CustomHttp extends Http {

    constructor(
        private backend: ConnectionBackend,
        private defaultOptions: RequestOptions,
        private _router: Router,
        private authService: AuthService
    ) {
        super(backend, defaultOptions);
    }

    request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.request(url, options));
    }

    get(url: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.get(url, options));
    }

    post(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.post(url, body, this.getRequestOptionArgs(options)));
    }

    put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.put(url, body, this.getRequestOptionArgs(options)));
    }

    delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.delete(url, options));
    }

    getRequestOptionArgs(options?: RequestOptionsArgs): RequestOptionsArgs {
        if (!options) {
            options = new CustomRequestOptions(this.authService);
        }
        if (options && !options.headers) {
            options.headers = new Headers();
        }
        return options;
    }

    intercept(observable: Observable<Response>): Observable<Response> {
        return observable.catch((err, source) => {
            if (err.status  === 401) {
                this.authService.set(null);
                this._router.navigate(['/login']);
                return Observable.empty();
            } else {
                return Observable.throw(err);
            }
        });

    }
}
