import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'backofficeEnumPipe'
})
export class EnumPipe implements PipeTransform {
    transform(enumName: any): any {
        let values: string[] = [];

        for (let enumValue in enumName) {
            if (enumName.hasOwnProperty(enumValue)) {
                values.push(enumValue);
            }
        }

        return values;
    }
}
