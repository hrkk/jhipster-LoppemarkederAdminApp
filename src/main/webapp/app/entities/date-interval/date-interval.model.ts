import { Marked } from '../marked';
export class DateInterval {
    constructor(
        public id?: number,
        public fromDate?: any,
        public toDate?: any,
        public marked?: Marked,
    ) {
    }
}
