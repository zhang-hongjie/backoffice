import { Pipe, PipeTransform } from '@angular/core';
import { TableSortColumn } from './table-sort-column';

@Pipe({
    name: 'backofficeTableSortPipe',
    pure: false
})
export class TableSortPipe implements PipeTransform {
    transform(list: Array<any>, column: string, sortBy: TableSortColumn): Array<any> {
        if (!sortBy.column) {
            return list;
        }

        if (sortBy.oldColumn === sortBy.column) {
            sortBy.column = '';
            return list.reverse();
        }

        sortBy.oldColumn = sortBy.column;
        sortBy.column = '';

        let extract: (object: any) => any;

        if (sortBy.oldColumn.endsWith('.length')) {
            extract = object => object[sortBy.oldColumn.replace('.length', '')].length;
        } else {
            extract = object => object[sortBy.oldColumn];
        }

        list.sort(function (objectA: any, objectB: any) {
            if (extract(objectA) < extract(objectB)) {
                return -1;
            } else if (extract(objectA) > extract(objectB)) {
                return 1;
            } else {
                return 0;
            }
        });
        return list;
    }

}
