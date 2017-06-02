export class Utils {

    static toggle(item: any, items: any) {
        const index = items.map((el: any) => el.id).indexOf(item.id);
        if (index > -1) {
            items.splice(index, 1);
        } else {
            items.push(item);
        }
    }

    static remove(item: any, items: any) {
        let index = items.map((el: any) => el.id).indexOf(item.id);
        if (index > -1) {
            items.splice(index, 1);
        }
    }

    static removeDuplicated(item: any, items: any) {
        for (let index = 0; index < items.length; index++) {
            if (items[index] === item) {
                items.splice(index, 1);
                index--;
            }
        }
    }

    static add(item: any, items: any) {
        const index = items.map((el: any) => el.id).indexOf(item.id);
        if (index === -1) {
            items.push(item);
        }
    }
}
