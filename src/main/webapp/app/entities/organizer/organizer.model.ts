import { MarkedItem } from '../marked-item';
export class Organizer {
    constructor(
        public id?: number,
        public name?: string,
        public email?: string,
        public phone?: string,
        public enableBooking?: boolean,
        public markedItem?: MarkedItem,
    ) {
        this.enableBooking = false;
    }
}
