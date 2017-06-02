import { Directive, Input, ElementRef, OnChanges } from '@angular/core';

@Directive({
    selector: '[backofficeFocus]'
})
export class FocusDirective implements OnChanges {

    @Input()
    public backofficeFocus: boolean;

    constructor(private element: ElementRef) {}

    ngOnChanges() {
        this.element.nativeElement.focus();
    }
}
