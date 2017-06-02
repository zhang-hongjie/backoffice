import { Observable } from 'rxjs/Observable';
import { ActivatedRoute, Params } from '@angular/router';

export class FakeActivatedRoute extends ActivatedRoute {
    params: Observable<Params>;

    constructor(parameters?: { [key: string]: any; }) {
        super();
        this.params = Observable.of(parameters);
    }
}
