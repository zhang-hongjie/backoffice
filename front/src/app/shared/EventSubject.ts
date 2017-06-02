import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/Rx';

@Injectable()
export class EventSubject extends BehaviorSubject<string> {
}
