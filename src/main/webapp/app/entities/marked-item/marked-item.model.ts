import { DateInterval } from '../date-interval';
import { Organizer } from '../organizer';
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
        public organizer?: Organizer,
    ) {
        this.enableBooking = false;
    }
}
