import { DateInterval } from '../date-interval';
export class MarkedItem {
    constructor(
        public id?: number,
        public name?: string,
        public markedInformation?: string,
        public markedRules?: string,
        public entreInfo?: string,
        public dateExtraInfo?: string,
        public address?: string,
        public latitude?: number,
        public longitude?: number,
        public enableBooking?: boolean,
        public dateInterval?: DateInterval,
    ) {
        this.enableBooking = false;
    }
}
