import { MarkedItem } from '../marked-item';
export class DateInterval {
    constructor(
        public id?: number,
        public fromDate?: any,
        public toDate?: any,
        public markedItem?: MarkedItem,
    ) {
    }
}
