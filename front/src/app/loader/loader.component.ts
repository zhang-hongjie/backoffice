import { Component, Input } from '@angular/core';

@Component({
    selector: 'backoffice-loader',
    styles: [require('./loader.component.scss')],
    template: require('./loader.component.html')
})

export class LoaderComponent {

    @Input() show: boolean;
    @Input() customclass: string;
}
