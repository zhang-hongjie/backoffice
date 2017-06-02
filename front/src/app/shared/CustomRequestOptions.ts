import { Injectable } from '@angular/core';
import { Headers, RequestOptions } from '@angular/http';
import { AuthService } from './AuthService';

@Injectable()
export class CustomRequestOptions extends RequestOptions {
  constructor(authService: AuthService) {
    super({
        headers: new Headers({
            'Content-Type': 'application/json; charset=utf-8',
            'Accept': 'application/json',
            'Authorization' : 'Bearer ' + authService.get()
        })
    });
  }
}
